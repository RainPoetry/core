package com.guava.cc.oracle.monitor;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:	日志监控
 */

import java.util.function.Consumer;

public abstract class Monitor {

	public abstract boolean isStart();

	public abstract void start();

	public abstract void stop();

}
