package com.rainpoetry.common.scheduler;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import java.util.concurrent.TimeUnit;

public interface Scheduler {

	void startUp();

	void shutdown();

	boolean isStart();

	void scheduler(String name, Runnable r, long delay, long period, TimeUnit timeUnit);

	void schedulerOnce(String name, Runnable r);
}
