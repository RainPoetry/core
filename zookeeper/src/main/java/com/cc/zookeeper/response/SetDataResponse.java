package com.cc.zookeeper.response;

import com.cc.zookeeper.request.SetDataRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class SetDataResponse extends AsyncResponse{

	private final Stat stat;

	public SetDataResponse(KeeperException.Code code, String path, Object ctx, Stat stat, SetDataRequest request) {
		super(ZkApis.SETDATA, code, path, ctx, request);
		this.stat = stat;
	}

	public Stat stat() {
		return stat;
	}

}
