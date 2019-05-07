package com.guava.cc.sink;

import com.guava.cc.common.Logging;
import com.google.common.base.Preconditions;
import org.apache.flume.Channel;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Sink;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * User: chenchong
 * Date: 2019/2/28
 * description:
 */
public abstract class BaseSink extends Logging implements Sink, LifecycleAware {
	private Channel channel;
	private String name;

	private LifecycleState lifecycleState;

	public BaseSink() {
		lifecycleState = LifecycleState.IDLE;
	}

	@Override
	public synchronized void start() {
		Preconditions.checkState(channel != null, "No channel configured");
		run();
		lifecycleState = LifecycleState.START;
	}

	public abstract void run();

	@Override
	public synchronized void stop() {
		lifecycleState = LifecycleState.STOP;
	}

	@Override
	public synchronized Channel getChannel() {
		return channel;
	}

	@Override
	public synchronized void setChannel(Channel channel) {
		this.channel = channel;
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

	@Override
	public String toString() {
		return this.getClass().getName() + "{name:" + name + ", channel:" + channel.getName() + "}";
	}
}
