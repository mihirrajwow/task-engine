# Task Engine — Concurrent Document Processing Engine

A Java 17 multithreaded task processing system demonstrating production-grade concurrency patterns: a priority-aware producer-consumer pipeline backed by a fixed thread pool, exponential-backoff retry logic, a dead-letter accounting model, and a lock-free metrics layer using `AtomicLong`.

Built to demonstrate concurrent system design directly transferable to enterprise Java platforms like Hyland OnBase.

---

## Architecture

```
Producer (1 thread)
    │
    │  submit(Task)
    ▼
TaskQueue  (PriorityBlockingQueue — capacity-bounded)
    │
    │  poll(2s timeout)
    ▼
WorkerPool (N worker threads)
    │
    ├── simulateWork(type)     → PARSE / VALIDATE / STORE
    │
    ├── on success             → MetricsTracker.recordCompleted()
    │
    └── on failure
            ├── retries < 3   → ScheduledExecutorService (exponential backoff)
            │                   → re-enqueue → back to TaskQueue
            └── retries == 3  → MetricsTracker.recordDeadLetter()
```

### Component Breakdown

| Class | Role |
|---|---|
| `Main` | Wires all components; owns the lifecycle (start → produce → drain → shutdown) |
| `Producer` | Single-threaded task generator; emits `N` tasks at a configurable rate |
| `TaskQueue` | Wraps `PriorityBlockingQueue`; enforces capacity without blocking the producer |
| `WorkerPool` | Manages the fixed thread pool and the retry scheduler |
| `Worker` | Pulls tasks from the queue, processes them, handles retries and dead-lettering |
| `Task` | Immutable value object with UUID, type, priority, payload, and retry counter |
| `TaskPriority` | `HIGH / MEDIUM / LOW` enum; ordinal drives `PriorityBlockingQueue` ordering |
| `MetricsTracker` | Lock-free counters via `AtomicLong`; prints live throughput/latency snapshots |

---

## Concurrency Design Decisions

### Priority Queue
`TaskQueue` wraps `PriorityBlockingQueue<Task>`. `Task` implements `Comparable<Task>` by ordinal so `HIGH` (ordinal 0) always exits before `MEDIUM` (1) and `LOW` (2). No custom `Comparator` needed — the enum ordering is the contract.

### Non-blocking Producer
`TaskQueue.submit()` uses `offer()` instead of `put()`. If the queue is at capacity the task is dropped and logged — the producer is never blocked. This mirrors backpressure strategies used in real-time ingestion systems.

### Safe Worker Shutdown
Workers use a timed `poll(2, TimeUnit.SECONDS)` instead of `take()`. This means the worker loop can re-evaluate its `running` flag and the `pendingRetries` counter every 2 seconds, enabling a clean exit without deadlock.

### Retry with Exponential Backoff
Failed tasks are rescheduled via a dedicated `ScheduledExecutorService` with a backoff of `200 * 2^retryCount` milliseconds (400ms, 800ms, 1600ms). A shared `AtomicInteger pendingRetries` is incremented before scheduling and decremented after the task re-enters the queue — this prevents a race window where all workers could exit while a retry is still in flight.

### Dead-Letter Accounting
Every task fate is accounted for. Tasks exhausting retries are counted as dead-lettered. Tasks that were mid-processing during a forced `shutdownNow()` are caught via `InterruptedException` and counted as dead-letters rather than silently discarded. Pending retries abandoned at shutdown are individually walked and counted the same way.

### Lock-Free Metrics
`MetricsTracker` uses only `AtomicLong` counters. There are no `synchronized` blocks in the hot path. `incrementAndGet()` compiles to a single compare-and-swap CPU instruction.

---

## Project Structure

```
task-engine/
├── pom.xml
└── src/
    ├── main/java/com/taskengine/
    │   ├── Main.java
    │   ├── metrics/
    │   │   └── MetricsTracker.java
    │   ├── model/
    │   │   ├── Task.java
    │   │   └── TaskPriority.java
    │   ├── producer/
    │   │   └── Producer.java
    │   ├── queue/
    │   │   └── TaskQueue.java
    │   └── worker/
    │       ├── Worker.java
    │       └── WorkerPool.java
    └── test/java/com/taskengine/
        ├── AppTest.java
        └── WorkerPoolTest.java
```

---

## Prerequisites

- Java 17+
- Maven 3.8+

---

## Build & Run

```bash
# Build
mvn clean compile

# Run all tests
mvn test

# Run the demo (100 tasks, 5 workers, live metrics every 5s)
mvn exec:java
```

### Sample Output

```
--- Metrics ---
Submitted:    60
Completed:    47
Failed:       8
Dead letter:  1
Avg latency:  183 ms
Throughput:   47 tasks/min
Failure rate: 13.3%

===== FINAL METRICS =====
Submitted:    100
Completed:    88
Failed:       18
Dead letter:  2
Avg latency:  176 ms
Throughput:   88 tasks/min
Failure rate: 18.0%
```

---

## Tests

Three JUnit 5 tests cover the core concurrency guarantees:

**`testQueuePriorityOrdering`** — Submits tasks out of priority order and asserts that `HIGH → MEDIUM → LOW` is the dequeue sequence.

**`testConcurrentSubmissionNoTasksLost`** — 10 concurrent submitter threads each submit 100 tasks (1000 total). Asserts that `completed + dead-lettered == 1000` — no task is silently lost under contention.

**`testGracefulShutdownAccountsForAllTasks`** — Submits 50 tasks, triggers shutdown mid-flight, and asserts that `completed + dead-lettered + still-queued == 50`.

```bash
mvn test
```

---

## Configuration

Default values are set in `Main.java` and can be adjusted directly:

| Parameter | Default | Description |
|---|---|---|
| Queue capacity | `500` | Max tasks in the priority queue |
| Worker threads | `5` | Size of the fixed thread pool |
| Total tasks | `100` | Tasks emitted by the producer |
| Tasks per second | `10` | Producer emission rate |
| Max retries | `3` | Attempts before dead-lettering |
| Retry backoff | `200 * 2^n ms` | Exponential backoff per attempt |
| Metrics interval | `5s` | Live stats print frequency |

---

## Task Types & Simulated Latency

| Type | Simulated Work Duration |
|---|---|
| `PARSE` | 100–300 ms |
| `VALIDATE` | 50–150 ms |
| `STORE` | 200–400 ms |

A 10% random failure rate is injected per task to exercise the retry and dead-letter paths under normal operation.

---

## Dependencies

| Dependency | Version | Scope |
|---|---|---|
| JUnit Jupiter | 5.10.0 | test |

No runtime dependencies beyond the JDK. All concurrency primitives (`ExecutorService`, `PriorityBlockingQueue`, `AtomicLong`, `ScheduledExecutorService`) are from `java.util.concurrent`.