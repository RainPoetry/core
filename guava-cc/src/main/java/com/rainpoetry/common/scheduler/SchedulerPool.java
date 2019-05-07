package com.rainpoetry.common.scheduler;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */



import com.rainpoetry.common.utils.CommonThread;
import com.rainpoetry.common.utils.Logging;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulerPool extends Logging implements Scheduler {

	private static final String SCHEDULER_IDENTIFY = "scheduler-pool ";
	private static final boolean DEFAULT_DAEMON = true;

	private final int threads;
	private final String threadNamePrefix;
	private final boolean daemon;

	private ScheduledThreadPoolExecutor executor;
	private AtomicInteger schedulerThreadId = new AtomicInteger(0);

	public SchedulerPool(int threads) {
		this(SCHEDULER_IDENTIFY + "-", threads, DEFAULT_DAEMON);
	}

	public SchedulerPool(String threadNamePrefix, int threads) {
		this(threadNamePrefix, threads, DEFAULT_DAEMON);
	}

	public SchedulerPool(String threadNamePrefix, int threads, boolean daemon) {
		logIdent = SCHEDULER_IDENTIFY;
		this.threadNamePrefix = threadNamePrefix;
		this.threads = threads;
		this.daemon = daemon;

	}

	@Override
	public void startUp() {
		info("Initializing task scheduler.");
		synchronized (this) {
			if (isStart())
				new SchedulerException("scheduler has init");
			executor = new ScheduledThreadPoolExecutor(threads);
			executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			executor.setThreadFactory(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new CommonThread(threadNamePrefix + schedulerThreadId.getAndIncrement(), r, daemon);
				}
			});
		}
	}

	@Override
	public void shutdown() {
		info("Shutting down task scheduler.");
		// We use the local variable to avoid NullPointerException if another thread shuts down scheduler at same time.
		ScheduledThreadPoolExecutor cachedExecutor = this.executor;
		if (executor != null) {
			synchronized (this) {
				cachedExecutor.shutdown();
				this.executor = null;
			}
			try {
				cachedExecutor.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isStart() {
		synchronized (this) {
			return this.executor != null;
		}
	}

	@Override
	public void scheduler(String name, Runnable r, long delay, long period, TimeUnit timeUnit) {
		info("Scheduling task %s with initial delay %d ms and period %d ms."
				.format(name, TimeUnit.MILLISECONDS.convert(delay, timeUnit), TimeUnit.MILLISECONDS.convert(period, timeUnit)));
		synchronized (this) {
			ensureRunning();
			if (period >= 0)
				executor.scheduleAtFixedRate(r, delay, period, timeUnit);
			else
				executor.schedule(r, delay, timeUnit);
		}
	}

	@Override
	public void schedulerOnce(String name, Runnable r) {
		scheduler(name, r, 0, -1L, TimeUnit.MILLISECONDS);
	}

	private void ensureRunning() {
		if (!isStart())
			throw new SchedulerException("Task scheduler is not running.");
	}
}
