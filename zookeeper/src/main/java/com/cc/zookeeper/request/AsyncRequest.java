package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class AsyncRequest {

	private final ZkApis apiKeys;
	private final String path;
	private final Object ctx;

	public AsyncRequest(ZkApis apiKeys, String path, Object ctx){
		this.apiKeys = apiKeys;
		this.path = path;
		this.ctx = ctx;
	}

	public ZkApis apiKeys() {
		return apiKeys;
	}

	public String path() {
		return path;
	}

	public Object ctx() {
		return ctx;
	}
}
