package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class GetAclRequest extends AsyncRequest {


	public GetAclRequest(String path) {
		this(path,null);
	}

	public GetAclRequest(String path, Object ctx) {
		super(ZkApis.GETACL, path, ctx);
	}
}
