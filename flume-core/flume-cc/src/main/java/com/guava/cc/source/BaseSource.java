package com.guava.cc.source;

import com.guava.cc.common.Logging;
import com.google.common.base.Preconditions;
import org.apache.flume.Source;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.lifecycle.LifecycleState;

import java.util.function.Predicate;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public abstract class BaseSource extends Logging implements Source {

	private ChannelProcessor channelProcessor;
	private String name;

	private LifecycleState lifecycleState;

	public BaseSource() {
		lifecycleState = LifecycleState.IDLE;
	}

	@Override
	public synchronized void start() {
		Preconditions.checkState(channelProcessor != null,
				"No channel processor configured");
		run();
		lifecycleState = LifecycleState.START;
	}

	public abstract void run();
	public abstract void close();

	@Override
	public synchronized void stop() {
		close();
		lifecycleState = LifecycleState.STOP;
	}

	@Override
	public synchronized void setChannelProcessor(ChannelProcessor cp) {
		channelProcessor = cp;
	}

	@Override
	public synchronized ChannelProcessor getChannelProcessor() {
		return channelProcessor;
	}

	@Override
	public synchronized LifecycleState getLifecycleState() {
		return lifecycleState;
	}

	@Override
	public synchronized void setName(String name) {
		this.name = name;
	}

	@Override
	public synchronized String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + "{name:" + name + ",state:" + lifecycleState + "}";
	}
}
