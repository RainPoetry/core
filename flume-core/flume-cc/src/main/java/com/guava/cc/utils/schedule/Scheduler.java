package com.guava.cc.utils.schedule;

import java.util.concurrent.TimeUnit;

/**
 * User: chenchong
 * Date: 2019/2/26
 * description:
 */
public interface Scheduler {

	 void startUp();

	 void shutdown() throws InterruptedException;

	 boolean isStart();

	 void scheduler(String name, Runnable e, long delay, long period, TimeUnit timeUnit);
}
