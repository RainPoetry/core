package com.guava.cc.di;

import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public interface WatchListener {

	void create(String path);
	void delete(String path);
	void modify(String path);
	void reName(String oldPath, String newPath);
	void overflow(String path);
}
