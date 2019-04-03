package com.cc.zookeeper.response;

import com.cc.zookeeper.request.AsyncRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public  class AsyncResponse {

	private final KeeperException.Code code;
	private final ZkApis apiKeys;
	private final String path;
	private final Object ctx;
	private final AsyncRequest request;

	public AsyncResponse(ZkApis apiKeys, KeeperException.Code code, String path, Object ctx, AsyncRequest request) {
		this.code = code;
		this.path = path;
		this.ctx = ctx;
		this.apiKeys = apiKeys;
		this.request = request;
	}

	public KeeperException.Code code() {
		return code;
	}

	public String path() {
		return path;
	}

	public Object ctx() {
		return ctx;
	}

	public ZkApis apiKeys() {
		return apiKeys;
	}

	public AsyncRequest request() {
		return request;
	}

	/** Return None if the result code is OK and KeeperException otherwise. */
	public KeeperException resultException() {
		if (code == KeeperException.Code.OK)
			return null;
		else
			return KeeperException.create(code, path);
	}

	/**
	 * Throw KeeperException if the result code is not OK.
	 */
	public void maybeThrow() {
		if (code != KeeperException.Code.OK)
			 KeeperException.create(code, path).printStackTrace();
	}
}
