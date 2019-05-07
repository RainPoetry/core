package com.guava.cc.concurrent;

/*
 * User: chenchong
 * Date: 2019/3/9
 * description:
 */

public class MyTry {

	private  boolean flag = false;

	public static void main(String[] args) throws InterruptedException {

		MyTry myTry = new MyTry();
		new Thread(()->{
			while(!myTry.flag);
			System.out.println("break");
		}).start();

		new Thread(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myTry.flag = true;
		}).start();
	}
}
