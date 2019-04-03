package com.cc.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: chenchong
 * Date: 2019/1/15
 * description:
 */
public class ZkThread extends Thread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static ZkThread daemon(final String name, Runnable runnable) {
		return new ZkThread(name, runnable, true);
	}

	public static ZkThread nonDaemon(final String name, Runnable runnable) {
		return new ZkThread(name, runnable, false);
	}

	public ZkThread(final String name, boolean daemon) {
		super(name);
		configureThread(name, daemon);
	}

	public ZkThread(final String name, Runnable runnable, boolean daemon) {
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
