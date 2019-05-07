package com.guava.cc.concurrent;

/*
 * User: chenchong
 * Date: 2019/3/9
 * description:
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock {

	// 默认： 非公平锁
	private Lock lock = new ReentrantLock();

	// 每一个 Condition 内部都维护有一个等待队列，调用 await() 的线程会进入等待队列
	// 调用 condition.await 方法的线程必须是已经获得了lock
	// 调用 await() 的线程会释放持有的 Lock , 然后加入等待队列
	private Condition condition = lock.newCondition();

	public static void main(String[] args){
		MyReentrantLock lock = new MyReentrantLock();

		new Thread(()->lock.after()).start();
		new Thread(()->lock.before()).start();
	}

	public void before() {
		lock.lock();
		System.out.println("before");
		// 将 等待队列的头结点 移至同步队列
		// signal方法首先会检测当前线程是否已经获取lock，
		// 如果没有获取lock会直接抛出异常，
		// 如果获取的话再得到等待队列的头指针引用的节点
		condition.signalAll();
		lock.unlock();
	}

	public void after() {
		try {
			lock.lock();
			// 前线程释放 lock, 加入等待队列
			// 被 signal/signalAll 后会使得当前线程从等待队列中移至到同步队列中去
			// 直到获得了 lock 后才会从 await 方法返回
			condition.await();
			System.out.println("after");
		} catch (InterruptedException e) {
			e.printStackTrace();
			 lock.unlock();
		}
	}

}
