package com.cc.di;

import com.cc.collection.ConcurrentListMap;
import com.cc.common.Logging;
import com.google.common.collect.HashMultimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public abstract class AbstractNotifier extends Logging implements Notifier{

	protected ConcurrentListMap<String,WatchListener> cacheMap = new ConcurrentListMap(16);

	@Override
	public void register(String path, WatchListener listener) {
		cacheMap.putAndCreate(path,listener);
	}

	public void unRegister(String path) {
		cacheMap.remove(path);
	}

}
