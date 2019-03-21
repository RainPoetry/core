package com.cc.oracle.monitor;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

public interface Filter<T> {

	boolean match(T t);
}
