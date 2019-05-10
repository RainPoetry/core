package rainpoetry.kafka.timewheel.common.utils.timer;



import rainpoetry.kafka.timewheel.common.utils.Logging;
import rainpoetry.kafka.timewheel.common.utils.SchedulerThread;
import rainpoetry.kafka.timewheel.common.utils.Time;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: chenchong
 * Date: 2019/1/21
 * description: 负责与 time Wheel 进行交互
 */
public class SystemTimer extends Logging implements Timer {

	// 任务定时调度的时间近似度（毫秒为单位）

	private final static long startMs_default = Time.SYSTEM.hiResClockMs();

	// 用于调度 Task 的 run()
	private final ExecutorService taskExecutor;
	private final DelayQueue<TimerTaskList> delayQueue;
	private final AtomicInteger taskCounter;
	private final TimingWheel timingWheel;

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
	private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

	public SystemTimer(String executorName, long tickMs,int wheelSize) {
		this(executorName, tickMs, wheelSize, startMs_default);
	}

	public SystemTimer(String executorName, long tickMs, int wheelSize, long startMs) {
		taskExecutor = Executors.newFixedThreadPool(1, runnable ->
				SchedulerThread.nonDaemon("executor-" + executorName, runnable)
		);
		this.delayQueue = new DelayQueue<TimerTaskList>();
		this.taskCounter = new AtomicInteger(0);
		this.timingWheel = new TimingWheel(tickMs, wheelSize, startMs, taskCounter, this.delayQueue);
	}

	@Override
	public void add(TimerTask timerTask) {
		readLock.lock();
		try {
			addTimerTaskEntry(new TimerTaskEntry(timerTask, timerTask.delayMs() + Time.SYSTEM.hiResClockMs()));
		} finally {
			readLock.unlock();
		}
	}

	private void addTimerTaskEntry(TimerTaskEntry timerTaskEntry) {
		// 添加失败，说明  过期 或者 cancel
		if (!timingWheel.add(timerTaskEntry)) {
			if (!timerTaskEntry.cancel())
				taskExecutor.submit(timerTaskEntry.timerTask());
		}
	}

	@Override
	public boolean advanceClock(long timeoutMs) {
		try {
			TimerTaskList bucket = delayQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
			if (bucket != null) {
				writeLock.lock();
				try {
					while (bucket != null) {
						// 向前推动时间轮
						timingWheel.advanceClock(bucket.getExpiration());
						// 删除 bucket 里的 TimerTask， 并执行 run
						bucket.flush(timerTaskEntry -> addTimerTaskEntry(timerTaskEntry));
						bucket = delayQueue.poll();
					}
				} finally {
					writeLock.unlock();
				}
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int size() {
		return taskCounter.get();
	}

	@Override
	public void shutdown() {
		taskExecutor.shutdown();
	}
}
