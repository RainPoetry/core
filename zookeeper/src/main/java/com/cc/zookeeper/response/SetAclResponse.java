package com.cc.zookeeper.response;

import com.cc.zookeeper.request.SetAclRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class SetAclResponse extends AsyncResponse {

	private final Stat stat;

	public SetAclResponse(KeeperException.Code code, String path, Object ctx, Stat stat, SetAclRequest request) {
		super(ZkApis.SETACL, code, path, ctx, request);
		this.stat = stat;
	}

	public Stat stat() {
		return stat;
	}
}
