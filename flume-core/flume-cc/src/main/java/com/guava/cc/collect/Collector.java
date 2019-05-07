package com.guava.cc.collect;

import com.google.common.eventbus.EventBus;
import org.apache.flume.Source;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public interface Collector<T> {

	void poll();

	// 注册监听对象
	void register(CollectionListener o);

	void interrupt();

	void close();
}
