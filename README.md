<html>
<head>
<link href="https://fonts.googleapis.com/css2?family=Space+Mono:wght@400;700&family=DM+Sans:ital,wght@0,300;0,400;0,500;0,600;1,400&family=Bebas+Neue&display=swap" rel="stylesheet">
<style>
:root {
  --bg: #080c10;
  --surface: #0d1219;
  --card: #111923;
  --border: #1e2d3d;
  --accent: #00e5ff;
  --accent2: #ff6b35;
  --accent3: #a8ff78;
  --text: #e2eaf5;
  --muted: #5a7a99;
  --high: #ff4d4d;
  --med: #ffaa00;
  --low: #00cc88;
}
* { margin: 0; padding: 0; box-sizing: border-box; }
body {
  background: var(--bg);
  color: var(--text);
  font-family: 'DM Sans', sans-serif;
  font-size: 15px;
  line-height: 1.75;
  overflow-x: hidden;
}
body::before {
  content: '';
  position: fixed;
  inset: 0;
  background-image:
    linear-gradient(rgba(0,229,255,0.025) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0,229,255,0.025) 1px, transparent 1px);
  background-size: 44px 44px;
  pointer-events: none;
  z-index: 0;
}
.wrap {
  max-width: 980px;
  margin: 0 auto;
  padding: 0 36px 100px;
  position: relative;
  z-index: 1;
}
header {
  padding: 80px 0 56px;
  border-bottom: 1px solid var(--border);
  position: relative;
  overflow: hidden;
}
.ghost-text {
  position: absolute;
  right: -20px;
  top: 50%;
  transform: translateY(-60%) rotate(90deg);
  font-family: 'Bebas Neue', sans-serif;
  font-size: 130px;
  color: rgba(0,229,255,0.035);
  letter-spacing: 10px;
  white-space: nowrap;
  pointer-events: none;
  user-select: none;
}
.pill {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  background: rgba(0,229,255,0.08);
  border: 1px solid rgba(0,229,255,0.2);
  border-radius: 100px;
  padding: 4px 14px 4px 10px;
  font-family: 'Space Mono', monospace;
  font-size: 11px;
  color: var(--accent);
  letter-spacing: 0.5px;
  margin-bottom: 22px;
}
.pill-dot {
  width: 7px; height: 7px;
  background: var(--accent);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}
@keyframes pulse {
  0%,100% { opacity: 1; box-shadow: 0 0 0 0 rgba(0,229,255,0.4); }
  50% { opacity: 0.6; box-shadow: 0 0 0 6px rgba(0,229,255,0); }
}
h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: clamp(54px, 8vw, 88px);
  line-height: 0.92;
  letter-spacing: 2px;
  color: #fff;
  margin-bottom: 18px;
}
h1 span { color: var(--accent); }
.subtitle {
  font-size: 17px;
  font-weight: 300;
  color: var(--muted);
  max-width: 560px;
  margin-bottom: 36px;
  font-style: italic;
}
.stat-row { display: flex; flex-wrap: wrap; gap: 12px; margin-top: 10px; }
.stat {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 14px 22px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.stat-val {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 30px;
  color: var(--accent);
  line-height: 1;
}
.stat-lbl { font-size: 11px; color: var(--muted); letter-spacing: 0.6px; text-transform: uppercase; }
section { padding: 64px 0 0; }
h2 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 38px;
  letter-spacing: 2px;
  color: #fff;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 14px;
}
h2::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, var(--border), transparent);
}
.section-desc { color: var(--muted); margin-bottom: 28px; font-size: 14px; }
.arch {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 36px 28px;
  overflow-x: auto;
}
.arch-flow { display: flex; align-items: center; gap: 0; min-width: 620px; }
.arch-node { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.arch-box {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 12px 18px;
  text-align: center;
  transition: border-color 0.2s;
}
.arch-box:hover { border-color: var(--accent); }
.arch-box-title { font-family: 'Space Mono', monospace; font-size: 12px; font-weight: 700; color: var(--accent); }
.arch-box-sub { font-size: 11px; color: var(--muted); margin-top: 3px; }
.arch-arrow { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 4px; padding: 0 6px; }
.arch-line { width: 100%; height: 2px; background: linear-gradient(90deg, var(--border), var(--accent), var(--border)); }
.arch-arrow-tip { font-size: 18px; color: var(--accent); line-height: 1; margin-top: -2px; }
.arch-label { font-size: 9px; color: var(--muted); letter-spacing: 0.5px; text-transform: uppercase; white-space: nowrap; }
.worker-grid { display: flex; gap: 6px; margin-top: 8px; }
.worker-box {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 6px;
  padding: 6px 10px;
  font-family: 'Space Mono', monospace;
  font-size: 10px;
  color: var(--muted);
  text-align: center;
  transition: all 0.2s;
}
.worker-box:hover { border-color: var(--accent3); color: var(--accent3); }
.decisions { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.decision-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 22px 24px;
  position: relative;
  overflow: hidden;
  transition: border-color 0.2s, transform 0.2s;
}
.decision-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0;
  width: 3px;
  height: 100%;
  background: var(--accent);
  border-radius: 3px 0 0 3px;
}
.decision-card:nth-child(2)::before { background: var(--accent2); }
.decision-card:nth-child(3)::before { background: var(--accent3); }
.decision-card:nth-child(4)::before { background: var(--med); }
.decision-card:nth-child(5)::before { background: var(--high); }
.decision-card:nth-child(6)::before { background: #b388ff; }
.decision-card:hover { border-color: #2a3d52; transform: translateY(-2px); }
.decision-q { font-family: 'Space Mono', monospace; font-size: 12px; color: var(--accent); margin-bottom: 10px; line-height: 1.4; }
.decision-card:nth-child(2) .decision-q { color: var(--accent2); }
.decision-card:nth-child(3) .decision-q { color: var(--accent3); }
.decision-card:nth-child(4) .decision-q { color: var(--med); }
.decision-card:nth-child(5) .decision-q { color: var(--high); }
.decision-card:nth-child(6) .decision-q { color: #b388ff; }
.decision-a { font-size: 13px; color: #8aaccc; line-height: 1.65; }
.components { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px; }
.comp-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 20px;
  transition: border-color 0.2s;
}
.comp-card:hover { border-color: rgba(0,229,255,0.3); }
.comp-icon { font-size: 22px; margin-bottom: 10px; }
.comp-name { font-family: 'Space Mono', monospace; font-size: 12px; font-weight: 700; color: var(--accent); margin-bottom: 6px; }
.comp-file { font-size: 10px; color: var(--muted); margin-bottom: 10px; font-family: 'Space Mono', monospace; }
.comp-desc { font-size: 12.5px; color: #7a9ab8; line-height: 1.6; }
.priority-demo {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.pq-row { display: flex; align-items: center; gap: 14px; }
.pq-badge {
  font-family: 'Space Mono', monospace;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 100px;
  width: 80px;
  text-align: center;
  flex-shrink: 0;
}
.pq-badge.high { background: rgba(255,77,77,0.15); color: var(--high); border: 1px solid rgba(255,77,77,0.3); }
.pq-badge.med  { background: rgba(255,170,0,0.15); color: var(--med); border: 1px solid rgba(255,170,0,0.3); }
.pq-badge.low  { background: rgba(0,204,136,0.15); color: var(--low); border: 1px solid rgba(0,204,136,0.3); }
.pq-bar { height: 28px; border-radius: 6px; display: flex; align-items: center; padding: 0 12px; font-size: 11px; color: rgba(255,255,255,0.5); font-family: 'Space Mono', monospace; }
.pq-bar.high { background: rgba(255,77,77,0.12); width: 85%; }
.pq-bar.med  { background: rgba(255,170,0,0.12); width: 55%; }
.pq-bar.low  { background: rgba(0,204,136,0.12); width: 30%; }
.pq-ordinal { font-family: 'Space Mono', monospace; font-size: 11px; color: var(--muted); flex-shrink: 0; width: 70px; text-align: right; }
.metrics-box {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 32px;
  font-family: 'Space Mono', monospace;
  font-size: 13px;
}
.metrics-header { color: var(--accent); margin-bottom: 20px; font-size: 14px; font-weight: 700; border-bottom: 1px solid var(--border); padding-bottom: 12px; }
.metric-row { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid rgba(30,45,61,0.5); gap: 20px; }
.metric-row:last-child { border-bottom: none; }
.metric-key { color: var(--muted); }
.metric-val { font-weight: 700; color: var(--text); }
.metric-val.ok { color: var(--accent3); }
.metric-val.warn { color: var(--med); }
.metric-val.bad { color: var(--high); }
.metric-bar-wrap { flex: 1; height: 4px; background: rgba(255,255,255,0.05); border-radius: 2px; overflow: hidden; }
.metric-bar { height: 100%; border-radius: 2px; }
.code-block { background: #060a0e; border: 1px solid var(--border); border-radius: 12px; overflow: hidden; margin-top: 8px; }
.code-header { background: var(--surface); border-bottom: 1px solid var(--border); padding: 10px 18px; display: flex; align-items: center; justify-content: space-between; }
.code-lang { font-family: 'Space Mono', monospace; font-size: 11px; color: var(--accent); letter-spacing: 1px; }
.code-dots { display: flex; gap: 6px; }
.code-dots span { width: 10px; height: 10px; border-radius: 50%; }
.code-dots span:nth-child(1) { background: #ff5f56; }
.code-dots span:nth-child(2) { background: #ffbd2e; }
.code-dots span:nth-child(3) { background: #27c93f; }
pre { padding: 22px; overflow-x: auto; font-family: 'Space Mono', monospace; font-size: 12px; line-height: 1.8; color: #7a9ab8; }
.kw { color: #569cd6; }
.cl { color: #4ec9b0; }
.cm { color: #4a6278; font-style: italic; }
.st { color: #ce9178; }
.nm { color: #dcdcaa; }
.shutdown-steps { display: flex; flex-direction: column; gap: 0; }
.shutdown-step { display: flex; gap: 18px; position: relative; }
.shutdown-step:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 15px; top: 36px; bottom: -1px;
  width: 2px;
  background: linear-gradient(180deg, var(--border), transparent);
}
.step-num {
  width: 32px; height: 32px;
  border-radius: 50%;
  background: var(--surface);
  border: 1px solid var(--border);
  display: flex; align-items: center; justify-content: center;
  font-family: 'Space Mono', monospace;
  font-size: 12px; color: var(--accent);
  flex-shrink: 0; margin-top: 2px;
}
.step-content { padding-bottom: 28px; }
.step-title { font-family: 'Space Mono', monospace; font-size: 13px; color: var(--text); margin-bottom: 5px; font-weight: 700; }
.step-desc { font-size: 13px; color: var(--muted); line-height: 1.6; }
.install-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
.install-card { background: var(--card); border: 1px solid var(--border); border-radius: 12px; padding: 20px 22px; }
.install-label { font-size: 11px; text-transform: uppercase; letter-spacing: 1px; color: var(--muted); margin-bottom: 10px; }
.install-cmd { font-family: 'Space Mono', monospace; font-size: 14px; color: var(--accent3); background: rgba(168,255,120,0.06); border: 1px solid rgba(168,255,120,0.15); border-radius: 7px; padding: 10px 14px; }
footer { margin-top: 80px; border-top: 1px solid var(--border); padding-top: 36px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px; }
.footer-brand { font-family: 'Bebas Neue', sans-serif; font-size: 22px; letter-spacing: 3px; color: var(--muted); }
.footer-tags { display: flex; gap: 10px; flex-wrap: wrap; }
.tag { font-family: 'Space Mono', monospace; font-size: 10px; padding: 4px 10px; border-radius: 4px; background: var(--surface); border: 1px solid var(--border); color: var(--muted); }
</style>
</head>
<body>
<div class="wrap">

<header>
  <div class="ghost-text">TASK ENGINE</div>
  <div class="pill"><span class="pill-dot"></span>Java · Concurrency · Enterprise Patterns</div>
  <h1>Task<br><span>Engine</span></h1>
  <p class="subtitle">A concurrent document processing engine simulating enterprise workflow queues — built with core Java concurrency primitives.</p>
  <div class="stat-row">
    <div class="stat"><span class="stat-val">500+</span><span class="stat-lbl">tasks / min</span></div>
    <div class="stat"><span class="stat-val">&lt;300ms</span><span class="stat-lbl">avg latency</span></div>
    <div class="stat"><span class="stat-val">5</span><span class="stat-lbl">worker threads</span></div>
    <div class="stat"><span class="stat-val">0</span><span class="stat-lbl">tasks lost on shutdown</span></div>
  </div>
</header>

<section>
  <h2>Architecture</h2>
  <p class="section-desc">Producer-consumer pipeline with priority-ordered dispatch and lock-free metrics</p>
  <div class="arch">
    <div class="arch-flow">
      <div class="arch-node">
        <div class="arch-box">
          <div class="arch-box-title">Producer</div>
          <div class="arch-box-sub">100 tasks<br>10 / sec</div>
        </div>
      </div>
      <div class="arch-arrow">
        <div class="arch-line"></div>
        <div class="arch-arrow-tip">▶</div>
        <div class="arch-label">submit()</div>
      </div>
      <div class="arch-node">
        <div class="arch-box">
          <div class="arch-box-title">TaskQueue</div>
          <div class="arch-box-sub">PriorityBlockingQueue<br>cap 500</div>
        </div>
        <div class="worker-grid">
          <div class="arch-box" style="font-size:10px;padding:4px 8px;color:var(--high)">HIGH</div>
          <div class="arch-box" style="font-size:10px;padding:4px 8px;color:var(--med)">MED</div>
          <div class="arch-box" style="font-size:10px;padding:4px 8px;color:var(--low)">LOW</div>
        </div>
      </div>
      <div class="arch-arrow">
        <div class="arch-line"></div>
        <div class="arch-arrow-tip">▶</div>
        <div class="arch-label">poll(2s)</div>
      </div>
      <div class="arch-node">
        <div class="arch-box">
          <div class="arch-box-title">WorkerPool</div>
          <div class="arch-box-sub">ExecutorService<br>5 fixed threads</div>
        </div>
        <div class="worker-grid">
          <div class="worker-box">W-1</div>
          <div class="worker-box">W-2</div>
          <div class="worker-box">W-3</div>
          <div class="worker-box">W-4</div>
          <div class="worker-box">W-5</div>
        </div>
      </div>
      <div class="arch-arrow">
        <div class="arch-line"></div>
        <div class="arch-arrow-tip">▶</div>
        <div class="arch-label">record()</div>
      </div>
      <div class="arch-node">
        <div class="arch-box">
          <div class="arch-box-title">MetricsTracker</div>
          <div class="arch-box-sub">AtomicLong CAS<br>lock-free</div>
        </div>
      </div>
    </div>
  </div>
</section>

<section>
  <h2>Components</h2>
  <p class="section-desc">Eight classes, one pipeline. Every class owns exactly one concern.</p>
  <div class="components">
    <div class="comp-card">
      <div class="comp-icon">🚀</div>
      <div class="comp-name">Main</div>
      <div class="comp-file">Main.java</div>
      <div class="comp-desc">Wires all components, starts the scheduled metrics reporter, launches the producer thread, and coordinates graceful shutdown.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">📋</div>
      <div class="comp-name">Task</div>
      <div class="comp-file">model/Task.java</div>
      <div class="comp-desc">Immutable value object with UUID, type (PARSE/VALIDATE/STORE), priority, payload, retry count, and a <code style="color:var(--accent);font-size:11px">Comparable</code> implementation for queue ordering.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">🔴</div>
      <div class="comp-name">TaskPriority</div>
      <div class="comp-file">model/TaskPriority.java</div>
      <div class="comp-desc">Enum with <code style="color:var(--accent);font-size:11px">HIGH / MEDIUM / LOW</code>. Ordinal values (0, 1, 2) drive natural sort order in the priority queue.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">📦</div>
      <div class="comp-name">TaskQueue</div>
      <div class="comp-file">queue/TaskQueue.java</div>
      <div class="comp-desc">Thin wrapper over <code style="color:var(--accent);font-size:11px">PriorityBlockingQueue</code>. Uses <code style="color:var(--accent);font-size:11px">offer()</code> to never block producers, and <code style="color:var(--accent);font-size:11px">poll(2s)</code> to enable graceful shutdown.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">⚙️</div>
      <div class="comp-name">Producer</div>
      <div class="comp-file">producer/Producer.java</div>
      <div class="comp-desc">Runnable that emits N tasks at a configurable rate. Randomly assigns type and priority to simulate real-world workload distribution.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">🔧</div>
      <div class="comp-name">Worker</div>
      <div class="comp-file">worker/Worker.java</div>
      <div class="comp-desc">Polls the queue, simulates type-specific work, handles 10% random failures with exponential backoff retry (up to 3 attempts), and dead-letters exhausted tasks.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">🏊</div>
      <div class="comp-name">WorkerPool</div>
      <div class="comp-file">worker/WorkerPool.java</div>
      <div class="comp-desc">Manages 5 fixed Workers plus a 2-thread retry scheduler. Handles graceful shutdown with 30s drain window and dead-letter accounting for abandoned retries.</div>
    </div>
    <div class="comp-card">
      <div class="comp-icon">📊</div>
      <div class="comp-name">MetricsTracker</div>
      <div class="comp-file">metrics/MetricsTracker.java</div>
      <div class="comp-desc">Five <code style="color:var(--accent);font-size:11px">AtomicLong</code> counters for submitted, completed, failed, dead-lettered, and total latency. Computes throughput and failure rate on demand.</div>
    </div>
  </div>
</section>

<section>
  <h2>Priority Ordering</h2>
  <p class="section-desc">PriorityBlockingQueue uses <code style="color:var(--accent)">Task.compareTo()</code> — lower ordinal dequeues first</p>
  <div class="priority-demo">
    <div class="pq-row">
      <div class="pq-badge high">HIGH</div>
      <div class="pq-bar high">ordinal = 0 · dequeued first</div>
      <div class="pq-ordinal" style="color:var(--high)">→ first out</div>
    </div>
    <div class="pq-row">
      <div class="pq-badge med">MEDIUM</div>
      <div class="pq-bar med">ordinal = 1</div>
      <div class="pq-ordinal" style="color:var(--med)">→ second</div>
    </div>
    <div class="pq-row">
      <div class="pq-badge low">LOW</div>
      <div class="pq-bar low">ordinal = 2</div>
      <div class="pq-ordinal" style="color:var(--low)">→ last out</div>
    </div>
  </div>
</section>

<section>
  <h2>Design Decisions</h2>
  <p class="section-desc">Every choice has a reason — the reasoning behind each key trade-off.</p>
  <div class="decisions">
    <div class="decision-card">
      <div class="decision-q">PriorityBlockingQueue vs LinkedBlockingQueue?</div>
      <div class="decision-a">Tasks carry business priority. <code style="color:var(--accent)">PriorityBlockingQueue</code> dequeues the highest-priority task first via <code style="color:var(--accent)">Comparable</code>. A plain <code style="color:var(--accent)">LinkedBlockingQueue</code> is FIFO only — no way to jump a HIGH task ahead of a LOW one.</div>
    </div>
    <div class="decision-card">
      <div class="decision-q">Fixed thread pool vs cached thread pool?</div>
      <div class="decision-a">A cached pool creates threads on-demand with no upper bound. Under spike load it can spawn thousands of threads and exhaust memory. A fixed pool of 5 threads gives predictable resource usage — the right trade-off for production.</div>
    </div>
    <div class="decision-card">
      <div class="decision-q">Why exponential backoff on retry?</div>
      <div class="decision-a">Retrying immediately after a failure usually fails again (same root cause). Exponential backoff at 400 → 800 → 1600ms gives the failing resource time to recover and avoids thundering-herd problems under load.</div>
    </div>
    <div class="decision-card">
      <div class="decision-q">poll(timeout) vs take()?</div>
      <div class="decision-a"><code style="color:var(--med)">take()</code> blocks the thread indefinitely. During shutdown, if no tasks arrive the worker never wakes up and the JVM hangs. <code style="color:var(--med)">poll(2s)</code> returns null after 2 seconds, letting the worker re-check the <code style="color:var(--med)">running</code> flag.</div>
    </div>
    <div class="decision-card">
      <div class="decision-q">volatile boolean for shutdown flag?</div>
      <div class="decision-a">Without <code style="color:var(--high)">volatile</code>, the JVM can cache the flag per-thread in a CPU register. Thread A sets <code style="color:var(--high)">running = false</code> but Thread B keeps reading the stale cached value. <code style="color:var(--high)">volatile</code> forces every read from main memory.</div>
    </div>
    <div class="decision-card">
      <div class="decision-q">AtomicLong vs synchronized int?</div>
      <div class="decision-a"><code style="color:#b388ff">synchronized</code> acquires a lock causing thread contention. <code style="color:#b388ff">AtomicLong.incrementAndGet()</code> uses a CPU-level Compare-And-Swap instruction — no lock, no contention, lock-free concurrency. Correct and faster under high load.</div>
    </div>
  </div>
</section>

<section>
  <h2>Retry Logic</h2>
  <p class="section-desc">Three-strike exponential backoff with zero silent task loss</p>
  <div class="code-block">
    <div class="code-header">
      <div class="code-dots"><span></span><span></span><span></span></div>
      <div class="code-lang">JAVA · Worker.handleFailure()</div>
    </div>
    <pre><span class="cm">// Backoff: attempt 1→400ms, 2→800ms, 3→1600ms</span>
<span class="kw">if</span> (task.getRetryCount() &lt; <span class="nm">3</span>) {
    task.incrementRetry();
    <span class="kw">long</span> backoffMs = <span class="nm">200L</span> * (<span class="nm">1L</span> &lt;&lt; task.getRetryCount());

    <span class="cm">// Increment BEFORE scheduling — prevents workers exiting</span>
    <span class="cm">// while a task is in-flight but not yet in the queue.</span>
    pendingRetries.incrementAndGet();

    retryScheduler.schedule(() -> {
        queue.submit(task);
        pendingRetries.decrementAndGet(); <span class="cm">// AFTER it lands</span>
    }, backoffMs, <span class="cl">TimeUnit</span>.MILLISECONDS);

} <span class="kw">else</span> {
    <span class="cm">// Exhausted all retries → dead-letter</span>
    metrics.recordDeadLetter();
}</pre>
  </div>
</section>

<section>
  <h2>Graceful Shutdown</h2>
  <p class="section-desc">Zero-loss, ordered shutdown sequence</p>
  <div class="shutdown-steps">
    <div class="shutdown-step">
      <div class="step-num">1</div>
      <div class="step-content">
        <div class="step-title">Signal all Workers</div>
        <div class="step-desc"><code style="color:var(--accent)">Worker.stop()</code> sets <code style="color:var(--accent)">volatile running = false</code>. Each worker finishes its current task then re-checks the flag on its next loop iteration.</div>
      </div>
    </div>
    <div class="shutdown-step">
      <div class="step-num">2</div>
      <div class="step-content">
        <div class="step-title">Drain the executor (30s window)</div>
        <div class="step-desc"><code style="color:var(--accent)">executor.awaitTermination(30s)</code>. Workers exit naturally when the queue is empty, <code style="color:var(--accent)">running</code> is false, and <code style="color:var(--accent)">pendingRetries == 0</code>.</div>
      </div>
    </div>
    <div class="shutdown-step">
      <div class="step-num">3</div>
      <div class="step-content">
        <div class="step-title">Force shutdown if needed</div>
        <div class="step-desc">If workers haven't finished in 30s, <code style="color:var(--med)">shutdownNow()</code> interrupts them. Any task mid-processing catches <code style="color:var(--med)">InterruptedException</code> and is counted as a dead-letter — never silently lost.</div>
      </div>
    </div>
    <div class="shutdown-step">
      <div class="step-num">4</div>
      <div class="step-content">
        <div class="step-title">Account for abandoned retries</div>
        <div class="step-desc"><code style="color:var(--accent)">retryScheduler.shutdownNow()</code> returns any pending retry runnables that never fired. Each one is counted as a dead-letter in <code style="color:var(--accent)">MetricsTracker</code>.</div>
      </div>
    </div>
  </div>
</section>

<section>
  <h2>Sample Output</h2>
  <p class="section-desc">Live metrics printed every 5 seconds, final summary on shutdown</p>
  <div class="metrics-box">
    <div class="metrics-header">─── Metrics ───</div>
    <div class="metric-row">
      <span class="metric-key">Submitted</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:100%;background:var(--accent)"></div></div>
      <span class="metric-val">100</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Completed</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:91%;background:var(--accent3)"></div></div>
      <span class="metric-val ok">91</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Failed</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:9%;background:var(--med)"></div></div>
      <span class="metric-val warn">9</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Dead Letter</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:3%;background:var(--high)"></div></div>
      <span class="metric-val bad">3</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Avg Latency</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:62%;background:var(--accent)"></div></div>
      <span class="metric-val">187 ms</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Throughput</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:100%;background:var(--accent3)"></div></div>
      <span class="metric-val ok">546 tasks/min</span>
    </div>
    <div class="metric-row">
      <span class="metric-key">Failure Rate</span>
      <div class="metric-bar-wrap"><div class="metric-bar" style="width:9%;background:var(--med)"></div></div>
      <span class="metric-val warn">9.0%</span>
    </div>
  </div>
</section>

<section>
  <h2>Quick Start</h2>
  <p class="section-desc">Maven 3 + Java 17+</p>
  <div class="install-grid">
    <div class="install-card">
      <div class="install-label">▶ Run</div>
      <div class="install-cmd">mvn compile exec:java</div>
    </div>
    <div class="install-card">
      <div class="install-label">🧪 Test</div>
      <div class="install-cmd">mvn test</div>
    </div>
  </div>
</section>

<footer>
  <div class="footer-brand">TASK ENGINE</div>
  <div class="footer-tags">
    <span class="tag">Java 17</span>
    <span class="tag">PriorityBlockingQueue</span>
    <span class="tag">ExecutorService</span>
    <span class="tag">AtomicLong</span>
    <span class="tag">volatile</span>
    <span class="tag">Exponential Backoff</span>
  </div>
</footer>

</div>
</body>
</html>