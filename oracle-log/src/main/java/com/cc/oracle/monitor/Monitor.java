package com.cc.oracle.monitor;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:	日志监控
 */

import java.util.function.Consumer;

public interface  Monitor {

	  boolean isStart();

	  void start();

	  void stop();

	  void awaitShutdown() throws InterruptedException;

}
