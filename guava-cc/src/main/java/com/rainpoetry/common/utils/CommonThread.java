package com.rainpoetry.common.utils;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonThread extends Thread{

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static CommonThread daemon(final String name, Runnable runnable) {
		return new CommonThread(name, runnable, true);
	}

	public static CommonThread nonDaemon(final String name, Runnable runnable) {
		return new CommonThread(name, runnable, false);
	}

	public CommonThread(final String name, boolean daemon) {
		super(name);
		configureThread(name, daemon);
	}

	public CommonThread(final String name, Runnable runnable, boolean daemon) {
		super(runnable, name);
		configureThread(name, daemon);
	}

	private void configureThread(final String name, boolean daemon) {
		setDaemon(daemon);
		setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				log.error("Uncaught exception in thread '{}':", name, e);
			}
		});
	}
}
