package com.taskengine.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class MetricsTracker {

    // All AtomicLong — thread-safe without synchronized keyword
    // incrementAndGet() is a single atomic CPU instruction (CAS)
    private final AtomicLong tasksSubmitted    = new AtomicLong(0);
    private final AtomicLong tasksCompleted    = new AtomicLong(0);
    private final AtomicLong tasksFailed       = new AtomicLong(0);
    private final AtomicLong deadLettered      = new AtomicLong(0);
    private final AtomicLong totalProcessingMs = new AtomicLong(0);

    private final long startTimeMs = System.currentTimeMillis();

    public void recordSubmitted()           { tasksSubmitted.incrementAndGet(); }
    public void recordFailed()              { tasksFailed.incrementAndGet(); }
    public void recordDeadLetter()          { deadLettered.incrementAndGet(); }

    public void recordCompleted(long processingMs) {
        tasksCompleted.incrementAndGet();
        totalProcessingMs.addAndGet(processingMs);
    }

    public long getCompleted()    { return tasksCompleted.get(); }
    public long getDeadLettered() { return deadLettered.get(); }

    public void printSummary() {
        long completed  = tasksCompleted.get();
        long submitted  = tasksSubmitted.get();
        long failed     = tasksFailed.get();
        long dead       = deadLettered.get();
        long totalMs    = totalProcessingMs.get();

        long avgLatency = completed > 0 ? totalMs / completed : 0;
        long elapsedMin = Math.max(1, (System.currentTimeMillis() - startTimeMs) / 60_000);
        long throughput = completed / elapsedMin;
        double failRate = submitted > 0 ? (failed * 100.0 / submitted) : 0;

        System.out.printf("""
            --- Metrics ---
            Submitted:    %d
            Completed:    %d
            Failed:       %d
            Dead letter:  %d
            Avg latency:  %d ms
            Throughput:   %d tasks/min
            Failure rate: %.1f%%
            """,
            submitted, completed, failed, dead, avgLatency, throughput, failRate
        );
    }
}