package com.taskengine;

import com.taskengine.metrics.MetricsTracker;
import com.taskengine.producer.Producer;
import com.taskengine.queue.TaskQueue;
import com.taskengine.worker.WorkerPool;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 1. Create shared components
        TaskQueue      queue   = new TaskQueue(500);
        MetricsTracker metrics = new MetricsTracker();

        // 2. Start live metrics (prints every 5 seconds)
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(metrics::printSummary, 5, 5, TimeUnit.SECONDS);

        // 3. Start worker pool (5 threads)
        WorkerPool pool = new WorkerPool(5, queue, metrics);
        pool.start();

        // 4. Start producer (100 tasks, 10 per second = ~10 seconds)
        Producer producer = new Producer(queue, metrics, 100, 10);
        Thread producerThread = new Thread(producer, "producer");
        producerThread.start();

        // 5. Wait for producer to finish
        producerThread.join();

        // 6. Give workers 2 extra seconds to drain remaining tasks
        Thread.sleep(2000);

        // 7. Graceful shutdown
        scheduler.shutdown();
        pool.shutdown();

        // 8. Final summary
        System.out.println("\n===== FINAL METRICS =====");
        metrics.printSummary();
    }
}