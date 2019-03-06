package com.cc.utils.schedule;

import com.cc.common.Logging;
import com.cc.utils.base.CommonThread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: chenchong
 * Date: 2019/2/26
 * description:
 */
public class CollectionScheduler extends Logging implements Scheduler{

	private final static String default_threadNamePrefix = "collection-scheduler-";
	private final static boolean default_daemon = true;

	private final int threads;
	private final String threadNamePrefix;
	private final boolean daemon;
	private  ScheduledThreadPoolExecutor executor =  null;
	private AtomicInteger schedulerThreadId = new AtomicInteger(0);

	public CollectionScheduler(int threads) {
		this(threads,default_threadNamePrefix);
	}

	public CollectionScheduler(int threads, String threadNamePrefix) {
		this(threads,threadNamePrefix,default_daemon);
	}

	public CollectionScheduler(int threads, String threadNamePrefix, boolean daemon) {
		this.threads = threads;
		this.threadNamePrefix = threadNamePrefix;
		this.daemon = daemon;
	}



	@Override
	public void startUp() {
		info("Initializing task scheduler.");
		synchronized(this) {
			if(isStart())
				throw new IllegalStateException("This scheduler has already been started!");
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
	public void shutdown() throws InterruptedException {
		debug("Shutting down task scheduler.");
		// We use the local variable to avoid NullPointerException if another thread shuts down scheduler at same time.
		ScheduledThreadPoolExecutor cachedExecutor = this.executor;
		if (cachedExecutor != null) {
			synchronized(this) {
				cachedExecutor.shutdown();
				this.executor = null;
			}
			cachedExecutor.awaitTermination(1, TimeUnit.DAYS);
		}
	}

	@Override
	public boolean isStart() {
		synchronized (this) {
			return executor != null;
		}
	}

	public void scheduleOnce(String name,  Runnable e) {
		scheduler(name, e,  0L,  -1L,TimeUnit.MILLISECONDS);
	}

	@Override
	public void scheduler(String name, Runnable r, long delay, long period, TimeUnit timeUnit) {
		debug("Scheduling task %s with initial delay %d ms and period %d ms."
				.format(name, TimeUnit.MILLISECONDS.convert(delay, timeUnit), TimeUnit.MILLISECONDS.convert(period, timeUnit)));
//	 	synchronized(this) {
			ensureRunning();
			if(period >= 0)
				executor.scheduleAtFixedRate(r, delay, period, timeUnit);
			else
				executor.schedule(r, delay, timeUnit);
//		}
	}

	public void scheduler22(String name, Runnable r, long delay, long period, TimeUnit timeUnit) {
		executor.scheduleAtFixedRate(r,0,3,TimeUnit.SECONDS);
	}

	@Override
	protected String ident() {
		return threadNamePrefix;
	}

	private void ensureRunning() {
		if (!isStart())
			throw new IllegalStateException("Kafka scheduler is not running.");
	}

}
