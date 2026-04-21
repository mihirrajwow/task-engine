package com.taskengine;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.model.Task;
import com.taskengine.model.TaskPriority;
import com.taskengine.queue.TaskQueue;
import com.taskengine.worker.WorkerPool;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;

class WorkerPoolTest {

    // ── Test 1: Priority ordering ────────────────────────────────────────────
    // Submit tasks out of order. Prove HIGH always comes out first.
    @Test
    void testQueuePriorityOrdering() throws InterruptedException {
        TaskQueue queue = new TaskQueue(100);

        queue.submit(new Task("PARSE",    TaskPriority.HIGH,   "high-payload"));
        queue.submit(new Task("VALIDATE", TaskPriority.LOW,    "low-payload"));
        queue.submit(new Task("STORE",    TaskPriority.MEDIUM, "med-payload"));

        Task first  = queue.poll();
        Task second = queue.poll();
        Task third  = queue.poll();

        assertEquals(TaskPriority.HIGH,   first.getPriority(),  "First should be HIGH");
        assertEquals(TaskPriority.MEDIUM, second.getPriority(), "Second should be MEDIUM");
        assertEquals(TaskPriority.LOW,    third.getPriority(),  "Third should be LOW");
    }

    // ── Test 2: Zero tasks lost under concurrent submission ──────────────────
    // 10 threads, each submits 100 tasks. All 1000 must be accounted for.
    @Test
    void testConcurrentSubmissionNoTasksLost() throws InterruptedException {
        TaskQueue queue       = new TaskQueue(2000);
        MetricsTracker metrics = new MetricsTracker();
        WorkerPool pool       = new WorkerPool(5, queue, metrics);
        pool.start();

        int threadCount = 10;
        int tasksPerThread = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService submitters = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            submitters.submit(() -> {
                for (int j = 0; j < tasksPerThread; j++) {
                    Task t = new Task("PARSE", TaskPriority.MEDIUM, "data-" + j);
                    if (queue.submit(t)) metrics.recordSubmitted();
                }
                latch.countDown();
            });
        }

        latch.await(); // wait for all submitters to finish
        submitters.shutdown();
        Thread.sleep(15000); // let workers process
        pool.shutdown();

        long processed = metrics.getCompleted() + metrics.getDeadLettered();
        assertEquals(threadCount * tasksPerThread, processed,
            "All 1000 tasks must be completed or dead-lettered — none lost");
    }

    // ── Test 3: Graceful shutdown — no exceptions, all tasks accounted for ───
    @Test
    void testGracefulShutdownAccountsForAllTasks() throws InterruptedException {
        TaskQueue queue       = new TaskQueue(500);
        MetricsTracker metrics = new MetricsTracker();
        WorkerPool pool       = new WorkerPool(3, queue, metrics);
        pool.start();

        int total = 50;
        for (int i = 0; i < total; i++) {
            Task t = new Task("VALIDATE", TaskPriority.HIGH, "payload-" + i);
            if (queue.submit(t)) metrics.recordSubmitted();
        }

        Thread.sleep(2000); // partial processing
        assertDoesNotThrow(pool::shutdown, "Shutdown must not throw any exception");

        long processed = metrics.getCompleted() + metrics.getDeadLettered();
        // Some tasks may still be in queue — verify the rest
        long remaining = queue.size();
        assertEquals(total, processed + remaining,
            "All submitted tasks must be in processed or still in queue — none lost");
    }
}