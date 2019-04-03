package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.data.ACL;

import java.util.List;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class SetAclRequest extends AsyncRequest {

	private final int version;
	private final List<ACL> acl;

	public SetAclRequest(String path, int version, List<ACL> acl) {
		this(path,null,version,acl);
	}

	public SetAclRequest(String path, Object ctx, int version, List<ACL> acl) {
		super(ZkApis.SETACL, path, ctx);
		this.version = version;
		this.acl = acl;
	}

	public int version() {
		return version;
	}

	public List<ACL> acl() {
		return acl;
	}
}
