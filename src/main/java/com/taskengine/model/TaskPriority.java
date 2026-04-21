package com.taskengine.model;

public enum TaskPriority {
    HIGH,    // ordinal = 0 → highest priority
    MEDIUM,  // ordinal = 1
    LOW;     // ordinal = 2 → lowest priority
    // PriorityBlockingQueue uses compareTo to sort.
    // Lower ordinal = dequeued first.
}