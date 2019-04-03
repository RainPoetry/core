package com.cc.zookeeper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:	
 */
public class ZkHandlerConcurrentMap<K, V> extends ConcurrentHashMap<K, V> {

	public ZkHandlerConcurrentMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public ZkHandlerConcurrentMap() {
		super();
	}

	public ZkHandlerConcurrentMap(int initialCapacity) {
		super(initialCapacity);
	}

	public void getAndExecute(String key, Consumer<? super V> consumer) {
		V v;
		if ((v = get(key)) != null)
			consumer.accept(v);
	}

}
