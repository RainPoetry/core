package com.cc.zookeeper;

import com.cc.utils.Logging;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:	任务调度
 */
public class ZkScheduler extends Logging {

	private static final String DEFAULT_THREAD_NAME = "zk-scheduler-";
	private final String threadNamePrefix;
	private static final String ident = "scheduler";
	private final boolean daemon;
	private final int threads;
	private ScheduledThreadPoolExecutor executor = null;
	private AtomicInteger schedulerThreadId = new AtomicInteger(0);

	public ZkScheduler(int threads, boolean daemon) {
		this(threads, DEFAULT_THREAD_NAME, daemon);
	}

	public ZkScheduler(int threads, String threadNamePrefix, boolean daemon) {
		super(ident);
		this.threadNamePrefix = threadNamePrefix;
		this.daemon = daemon;
		this.threads = threads;
	}

	public void start() {
		info("Initializing task scheduler.");
		synchronized (this) {
			if (isStart())
				throw new IllegalStateException("This scheduler has already been started!");
			executor = new ScheduledThreadPoolExecutor(threads);
			executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			executor.setThreadFactory(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					return new ZkThread(threadNamePrefix + schedulerThreadId.getAndIncrement(), runnable, daemon);
				}
			});
		}
	}

	public void shutdown() {
		debug("Shutting down task scheduler.");
		// We use the local variable to avoid NullPointerException if another thread shuts down scheduler at same time.
		ScheduledThreadPoolExecutor cachedExecutor = this.executor;
		if (cachedExecutor != null) {
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

	public void scheduleOnce(String name, Runnable runnable) {
		schedule(name, 0L, -1L, TimeUnit.MILLISECONDS, runnable);
	}

	public void schedule(String name, Long delay, Long period, TimeUnit timeUnit, Runnable runnable) {
		debug("Scheduling task %s with initial delay %d ms and period %d ms."
				.format(name, TimeUnit.MILLISECONDS.convert(delay, timeUnit), TimeUnit.MILLISECONDS.convert(period, timeUnit)));
		synchronized (this) {
			debug("Beginning execution of scheduled task '%s'.".format(name));
			try {
				if (period >= 0)
					executor.scheduleAtFixedRate(runnable, delay, period, timeUnit);
				else
					executor.schedule(runnable, delay, timeUnit);
			} catch (Throwable t) {
				error("Uncaught exception in scheduled task '" + name + "'", t);
			} finally {
				debug("Completed execution of scheduled task '%s'.".format(name));
			}
		}
	}

	public boolean isStart() {
		synchronized (this) {
			return executor != null;
		}
	}

	private void ensureRunning() {
		if (!isStart())
			throw new IllegalStateException("Kafka scheduler is not running.");
	}

}
