package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class SetDataRequest extends AsyncRequest{

	private final byte[] data;
	private final int version;

	public SetDataRequest(String path, int version, byte[] data) {
		this(path,null,version,data);
	}

	public SetDataRequest(String path, Object ctx, int version, byte[] data) {
		super(ZkApis.SETDATA, path, ctx);
		this.version = version;
		this.data = data;
	}

	public byte[] data() {
		return data;
	}

	public int version() {
		return version;
	}
}
