package com.cc.zookeeper.response;

import com.cc.zookeeper.request.GetDataRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class GetDataResponse extends AsyncResponse {

	private final byte[] data;
	private final Stat stat;

	public GetDataResponse(KeeperException.Code code, String path, Object ctx, byte[] data, Stat stat, GetDataRequest request) {
		super(ZkApis.GETDATA, code, path, ctx, request);
		this.data = data;
		this.stat = stat;
	}

	public byte[] data() {
		return data;
	}

	public Stat stat() {
		return stat;
	}
}