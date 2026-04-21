package com.taskengine.worker;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.queue.TaskQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class WorkerPool {

    private static final Logger logger = Logger.getLogger(WorkerPool.class.getName());

    private final int threadCount;
    private final TaskQueue queue;
    private final MetricsTracker metrics;

    private ExecutorService executor;
    private final List<Worker> workers = new ArrayList<>();

    public WorkerPool(int threadCount, TaskQueue queue, MetricsTracker metrics) {
        this.threadCount = threadCount;
        this.queue       = queue;
        this.metrics     = metrics;
    }

    public void start() {
        // Fixed pool = predictable resource usage.
        // Cached pool creates unbounded threads under spike load (bad for production).
        executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Worker worker = new Worker(queue, metrics);
            workers.add(worker);
            executor.submit(worker);
        }
        logger.info("WorkerPool started with " + threadCount + " threads.");
    }

    public void shutdown() {
        logger.info("Initiating graceful shutdown...");

        // Step 1: Signal all workers to stop accepting new tasks
        workers.forEach(Worker::stop);

        // Step 2: Stop accepting new tasks to executor
        executor.shutdown();

        // Step 3: Wait up to 30 seconds for workers to drain the queue
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                // Step 4: Force kill if workers are stuck
                logger.warning("Workers did not finish in 30s — forcing shutdown.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("WorkerPool shutdown complete.");
    }
}