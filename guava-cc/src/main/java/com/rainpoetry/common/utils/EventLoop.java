package com.rainpoetry.common.utils;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EventLoop<E> {

	private BlockingQueue<E> queue = new LinkedBlockingQueue<E>();

	private final AtomicBoolean stopped = new AtomicBoolean(false);
	private final Thread eventThread;

	public EventLoop(String name) {
		eventThread = new CommonThread(name, () -> {
			while (!stopped.get()) {
				try {
					E e = queue.take();
					onReceive(e);
				} catch (InterruptedException t) {
					t.printStackTrace();
				} catch (Exception t) {
					onError(t);
				}
			}
		}, true);
	}

	public void start() {
		if (stopped.get())
			throw new IllegalStateException(eventThread.getName() + " has already been stopped");
		// Call onStart before starting the event thread to make sure it happens before onReceive
		onStart();
		eventThread.start();
	}

	public void stop() {
		if (stopped.compareAndSet(false, true)) {
			eventThread.interrupt();
			boolean onStopCalled = false;
			try {
				eventThread.join();
				// Call onStop after the event thread exits to make sure onReceive happens before onStop
				onStopCalled = true;
				onStop();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				if (!onStopCalled) {
					// ie is thrown from `eventThread.join()`. Otherwise, we should not call `onStop` since
					// it's already called.
					onStop();
				}
			}
		} else {
			// Keep quiet to allow calling `stop` multiple times.
		}
	}

	public void post(E e) {
		try {
			queue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	protected boolean isActive() {
		return eventThread.isAlive();
	}

	protected abstract void onStart();

	protected abstract void onStop();

	protected abstract void onReceive(E e);

	protected abstract void onError(Throwable t);

}
