package com.taskengine.queue;

import com.taskengine.model.Task;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TaskQueue {

    private static final Logger logger = Logger.getLogger(TaskQueue.class.getName());
    private final PriorityBlockingQueue<Task> queue;
    private final int capacity;

    public TaskQueue(int capacity) {
        this.capacity = capacity;
        // Initial capacity hint = 11 (default), max enforced by us
        this.queue = new PriorityBlockingQueue<>(11);
    }

    /**
     * Submit a task. Returns false if queue is full — never blocks the producer.
     * Use offer(), not put(). put() blocks; we don't want that.
     */
    public boolean submit(Task task) {
        if (queue.size() >= capacity) {
            logger.warning("Queue full! Dropping task: " + task);
            return false;
        }
        return queue.offer(task);
    }

    /**
     * Poll with timeout. Returns null if no task arrives within 2 seconds.
     * Workers loop back and re-check the shutdown flag.
     * NEVER use queue.take() — it blocks forever, preventing graceful shutdown.
     */
    public Task poll() throws InterruptedException {
        return queue.poll(2, TimeUnit.SECONDS);
    }

    public int size() {
        return queue.size();
    }
}