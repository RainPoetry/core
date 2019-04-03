package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class DeleteRequest extends AsyncRequest{

	private final int version;

	public DeleteRequest(String path, int version) {
		this(path,null,version);
	}

	public DeleteRequest(String path, Object ctx, int version) {
		super(ZkApis.DELETE, path, ctx);
		this.version = version;
	}

	public int version() {
		return version;
	}
}
