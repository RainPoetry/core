package com.guava.cc.di;

import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public interface Notifier {

	void register(String path, WatchListener listener);

	void run();

	void unRegister(String path);
}
