package com.taskengine.producer;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.model.Task;
import com.taskengine.model.TaskPriority;
import com.taskengine.queue.TaskQueue;
import java.util.logging.Logger;

public class Producer implements Runnable {

    private static final Logger logger = Logger.getLogger(Producer.class.getName());

    private static final String[] TYPES = {"PARSE", "VALIDATE", "STORE"};
    private static final TaskPriority[] PRIORITIES = TaskPriority.values();

    private final TaskQueue queue;
    private final MetricsTracker metrics;
    private final int totalTasks;
    private final int tasksPerSecond;

    public Producer(TaskQueue queue, MetricsTracker metrics, int totalTasks, int tasksPerSecond) {
        this.queue          = queue;
        this.metrics        = metrics;
        this.totalTasks     = totalTasks;
        this.tasksPerSecond = tasksPerSecond;
    }

    @Override
    public void run() {
        long sleepMs = 1000L / tasksPerSecond; // e.g. 10/s = sleep 100ms

        for (int i = 0; i < totalTasks; i++) {
            String type     = TYPES[(int)(Math.random() * TYPES.length)];
            TaskPriority pr = PRIORITIES[(int)(Math.random() * PRIORITIES.length)];
            Task task       = new Task(type, pr, "payload-" + i);

            if (queue.submit(task)) {
                metrics.recordSubmitted();
            }

            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Producer finished. Submitted " + totalTasks + " tasks.");
    }
}