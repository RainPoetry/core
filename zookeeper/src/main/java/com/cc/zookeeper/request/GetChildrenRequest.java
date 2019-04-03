package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class GetChildrenRequest extends AsyncRequest {

	public GetChildrenRequest(String path) {
		this(path,null);
	}

	public GetChildrenRequest(String path, Object ctx) {
		super(ZkApis.GETCHILDREN, path, ctx);
	}
}
