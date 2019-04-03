package com.cc.zookeeper.response;

import com.cc.zookeeper.request.GetAclRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class GetAclResponse extends AsyncResponse{

	private final List<ACL> alc;
	private final Stat stat;

	public GetAclResponse(KeeperException.Code code, String path, Object ctx, List<ACL> alc, Stat stat, GetAclRequest request) {
		super(ZkApis.GETACL,code,path,ctx, request);
		this.alc = alc;
		this.stat = stat;
	}

	public List<ACL> alc() {
		return alc;
	}

	public Stat stat() {
		return stat;
	}
}