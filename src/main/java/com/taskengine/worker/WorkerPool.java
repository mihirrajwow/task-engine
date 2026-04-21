package com.taskengine.worker;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.queue.TaskQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class WorkerPool {

    private static final Logger logger = Logger.getLogger(WorkerPool.class.getName());

    private final int threadCount;
    private final TaskQueue queue;
    private final MetricsTracker metrics;

    private ExecutorService executor;
    private ScheduledExecutorService retryScheduler;

    // Tracks tasks that are mid-flight in the retryScheduler (scheduled but not yet
    // back in the queue). Workers include this in their exit condition to avoid
    // leaving before retried tasks have someone to process them.
    private final AtomicInteger pendingRetries = new AtomicInteger(0);

    private final List<Worker> workers = new ArrayList<>();

    public WorkerPool(int threadCount, TaskQueue queue, MetricsTracker metrics) {
        this.threadCount = threadCount;
        this.queue       = queue;
        this.metrics     = metrics;
    }

    public void start() {
        executor       = Executors.newFixedThreadPool(threadCount);
        retryScheduler = Executors.newScheduledThreadPool(2);

        for (int i = 0; i < threadCount; i++) {
            Worker worker = new Worker(queue, metrics, retryScheduler, pendingRetries);
            workers.add(worker);
            executor.submit(worker);
        }
        logger.info("WorkerPool started with " + threadCount + " threads.");
    }

    public void shutdown() {
        logger.info("Initiating graceful shutdown...");

        workers.forEach(Worker::stop);
        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                logger.warning("Workers did not finish in 30s — forcing shutdown.");
                // shutdownNow() interrupts workers. Any task a worker was mid-processing
                // gets an InterruptedException in simulateWork(), which Worker.processTask()
                // catches and counts as a dead-letter — so it won't be silently lost.
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Any tasks still queued in the retryScheduler that never fired are tasks
        // that failed processing and were waiting for their backoff delay.
        // shutdownNow() returns those pending Runnables. Each one represents one task
        // that will never be processed — count each as a dead-letter.
        List<Runnable> abandonedRetries = retryScheduler.shutdownNow();
        if (!abandonedRetries.isEmpty()) {
            logger.warning("Abandoning " + abandonedRetries.size() + " scheduled retries — counting as dead letters.");
            for (Runnable ignored : abandonedRetries) {
                metrics.recordDeadLetter();
                pendingRetries.decrementAndGet();
            }
        }

        try {
            retryScheduler.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("WorkerPool shutdown complete.");
    }
}