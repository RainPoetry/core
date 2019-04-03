package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class GetDataRequest extends AsyncRequest{


	public GetDataRequest(String path) {
		this(path,null);
	}

	public GetDataRequest(String path, Object ctx) {
		super(ZkApis.GETDATA, path, ctx);
	}
}
