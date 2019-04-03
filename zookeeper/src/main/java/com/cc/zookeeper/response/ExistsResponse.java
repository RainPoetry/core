package com.cc.zookeeper.response;

import com.cc.zookeeper.request.ExistsRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class ExistsResponse extends AsyncResponse {

	private final Stat stat;

	public ExistsResponse(KeeperException.Code code, String path, Object ctx, Stat stat, ExistsRequest existsRequest) {
		super(ZkApis.EXISTS, code, path, ctx, existsRequest);
		this.stat = stat;
	}

	public Stat stat() {
		return stat;
	}
}
