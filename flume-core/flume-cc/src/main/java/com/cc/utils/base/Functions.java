package com.cc.utils.base;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * User: chenchong
 * Date: 2019/2/18
 * description:
 */
public final class Functions {

	private Functions(){}

	// Execute the given function inside the lock
	public static<T> T inLock(Lock lock, Supplier<? extends T> fun) {
		lock.lock();
		try {
			return fun.get();
		} finally {
			lock.unlock();
		}
	}

	public static<T> T inReadLock(ReadWriteLock lock, Supplier<? extends T> fun) {
		return inLock(lock.readLock(),fun);
	}

	public static<T> T inWriteLock(ReadWriteLock lock, Supplier<? extends T> fun) {
		return inLock(lock.writeLock(),fun);
	}


}
