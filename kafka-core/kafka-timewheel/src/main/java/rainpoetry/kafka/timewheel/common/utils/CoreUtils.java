package rainpoetry.kafka.timewheel.common.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * User: chenchong
 * Date: 2019/1/23
 * description:	General helper functions!
 */
public class CoreUtils {

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
