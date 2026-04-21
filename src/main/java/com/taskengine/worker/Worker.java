package com.taskengine.worker;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.model.Task;
import com.taskengine.queue.TaskQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    private final TaskQueue queue;
    private final MetricsTracker metrics;
    private final ScheduledExecutorService retryScheduler;
    private final AtomicInteger pendingRetries;

    private volatile boolean running = true;

    public Worker(TaskQueue queue, MetricsTracker metrics,
                  ScheduledExecutorService retryScheduler, AtomicInteger pendingRetries) {
        this.queue          = queue;
        this.metrics        = metrics;
        this.retryScheduler = retryScheduler;
        this.pendingRetries = pendingRetries;
    }

    @Override
    public void run() {
        while (running || queue.size() > 0 || pendingRetries.get() > 0) {
            try {
                Task task = queue.poll(); // waits up to 2 seconds

                if (task == null) {
                    continue;
                }

                processTask(task);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info(Thread.currentThread().getName() + " shutting down.");
    }

    private void processTask(Task task) {
        long startTime = System.currentTimeMillis();
        try {
            simulateWork(task.getType());

            if (Math.random() < 0.1) {
                throw new RuntimeException("Simulated processing failure");
            }

            long elapsed = System.currentTimeMillis() - startTime;
            metrics.recordCompleted(elapsed);
            logger.fine("Completed: " + task);

        } catch (InterruptedException e) {
            // Worker was force-interrupted during simulateWork (forced shutdown).
            // We must account for this task — dead-letter it rather than silently losing it.
            metrics.recordDeadLetter();
            logger.warning("Force-interrupted, dead-lettering: " + task);
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            metrics.recordFailed();
            handleFailure(task, e);
        }
    }

    private void simulateWork(String type) throws InterruptedException {
        long sleepMs = switch (type) {
            case "PARSE"    -> 100 + (long)(Math.random() * 200);
            case "VALIDATE" -> 50  + (long)(Math.random() * 100);
            case "STORE"    -> 200 + (long)(Math.random() * 200);
            default         -> 100;
        };
        Thread.sleep(sleepMs);
    }

    private void handleFailure(Task task, Exception e) {
        if (task.getRetryCount() < 3) {
            task.incrementRetry();
            long backoffMs = 200L * (1L << task.getRetryCount()); // 400, 800, 1600ms
            logger.warning("Retrying " + task + " in " + backoffMs + "ms (attempt " + task.getRetryCount() + ")");

            // Increment BEFORE scheduling — no window where counter is 0 but task
            // hasn't landed yet, which would let workers exit prematurely.
            pendingRetries.incrementAndGet();

            retryScheduler.schedule(() -> {
                queue.submit(task);
                // Decrement AFTER the task is in the queue.
                pendingRetries.decrementAndGet();
            }, backoffMs, TimeUnit.MILLISECONDS);

        } else {
            logger.severe("Dead letter: " + task + " — " + e.getMessage());
            metrics.recordDeadLetter();
        }
    }

    public void stop() {
        this.running = false;
    }
}