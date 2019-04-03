package com.cc.zookeeper.response;

import com.cc.zookeeper.request.CreateRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class CreateResponse extends AsyncResponse{

	private final String name;
	public CreateResponse(KeeperException.Code code, String path, Object ctx, String name, CreateRequest request) {
		super(ZkApis.CREATE,code, path, ctx, request);
		this.name = name;
	}

	public String name() {
		return name;
	}
}
