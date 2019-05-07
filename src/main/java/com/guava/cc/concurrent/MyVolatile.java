package com.guava.cc.concurrent;

/*
 * User: chenchong
 * Date: 2019/3/9
 * description:
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Lock前缀的指令会引起处理器缓存写回内存；
 *	一个处理器的缓存回写到内存会导致其他处理器的缓存失效；
 *	当处理器发现本地缓存失效后，就会从内存中重读该变量数据，即可以获取当前最新值。
 */
public class MyVolatile {

	private static /**volatile*/ boolean flag = false;
	// no use , 当 n 个线程读取这个值时，读取的都是最新值，因此 ， count++ 是根据最新值来的，
	// 但是当把 count 写入到 内存，会存在覆盖问题，也有可能存在 多个线程获取最新值后 都 ++ 然后写入
	// 使用场景：
	// 		状态标识
	private static volatile int count = 0;

	public static void main(String[] args) throws InterruptedException {

//		flag();

		count();
	}

	public static void count() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(10);

		for(int i = 0;i<10;i++) {
			service.submit(()->{for(int j=0;j<100;j++) synchronized (MyVolatile.class){count++;}});
		}
		while(service.awaitTermination(500,TimeUnit.MICROSECONDS));
		System.out.println("result: " + count);
		service.shutdown();
	}
	public static void flag() {
		new Thread(()->{
			while(!flag);
		}).start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new Thread(()->{
			flag = true;
		}).start();
	}


}
