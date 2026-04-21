package com.taskengine.worker;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.model.Task;
import com.taskengine.queue.TaskQueue;
import java.util.logging.Logger;

public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    private final TaskQueue queue;
    private final MetricsTracker metrics;

    // CRITICAL: must be volatile. Without it, JVM can cache per-thread.
    // The WorkerPool sets this to false on a different thread.
    // volatile ensures this worker thread sees the update immediately.
    private volatile boolean running = true;

    public Worker(TaskQueue queue, MetricsTracker metrics) {
        this.queue   = queue;
        this.metrics = metrics;
    }

    @Override
    public void run() {
        // Drain the queue even after shutdown signal.
        // Loop continues while: running=true OR there are still tasks left.
        while (running || queue.size() > 0) {
            try {
                Task task = queue.poll(); // waits up to 2 seconds

                if (task == null) {
                    // Timeout — no task arrived. Loop back and re-check running.
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
            // Simulate work based on task type
            simulateWork(task.getType());

            // 10% random failure to simulate real-world errors
            if (Math.random() < 0.1) {
                throw new RuntimeException("Simulated processing failure");
            }

            long elapsed = System.currentTimeMillis() - startTime;
            metrics.recordCompleted(elapsed);
            logger.fine("Completed: " + task);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            metrics.recordFailed();
            handleFailure(task, e);
        }
    }

    private void simulateWork(String type) throws InterruptedException {
        long sleepMs = switch (type) {
            case "PARSE"    -> 100 + (long)(Math.random() * 200); // 100-300ms
            case "VALIDATE" -> 50  + (long)(Math.random() * 100); // 50-150ms
            case "STORE"    -> 200 + (long)(Math.random() * 200); // 200-400ms
            default         -> 100;
        };
        Thread.sleep(sleepMs);
    }

    private void handleFailure(Task task, Exception e) {
        if (task.getRetryCount() < 3) {
            task.incrementRetry();
            long backoffMs = 200L * (1L << task.getRetryCount()); // 200, 400, 800ms
            logger.warning("Retrying " + task + " in " + backoffMs + "ms (attempt " + task.getRetryCount() + ")");
            try {
                Thread.sleep(backoffMs); // exponential backoff
                queue.submit(task);      // resubmit to queue
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        } else {
            logger.severe("Dead letter: " + task + " — " + e.getMessage());
            metrics.recordDeadLetter();
        }
    }

    // Called by WorkerPool during shutdown
    public void stop() {
        this.running = false;
    }
}