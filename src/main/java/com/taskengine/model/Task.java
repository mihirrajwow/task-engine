package com.taskengine.model;

import java.util.UUID;

public class Task implements Comparable<Task> {

    private final UUID taskId;
    private final String type;       // "PARSE", "VALIDATE", "STORE"
    private final TaskPriority priority;
    private final String payload;
    private int retryCount;
    private final long createdAt;

    public Task(String type, TaskPriority priority, String payload) {
        this.taskId    = UUID.randomUUID();
        this.type      = type;
        this.priority  = priority;
        this.payload   = payload;
        this.retryCount = 0;
        this.createdAt = System.currentTimeMillis();
    }

    // KEY: this is what PriorityBlockingQueue uses for ordering
    @Override
    public int compareTo(Task other) {
        return Integer.compare(
            this.priority.ordinal(),
            other.priority.ordinal()
        );
        // HIGH.ordinal()=0 < LOW.ordinal()=2
        // So HIGH tasks come out first — correct!
    }

    // Getters
    public UUID getTaskId()        { return taskId; }
    public String getType()        { return type; }
    public TaskPriority getPriority() { return priority; }
    public String getPayload()     { return payload; }
    public int getRetryCount()     { return retryCount; }
    public long getCreatedAt()     { return createdAt; }

    public void incrementRetry()   { retryCount++; }

    @Override
    public String toString() {
        return String.format("[%s] type=%s priority=%s retries=%d",
            taskId.toString().substring(0, 8), type, priority, retryCount);
    }
}