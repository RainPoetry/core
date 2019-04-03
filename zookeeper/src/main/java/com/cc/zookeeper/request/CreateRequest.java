package com.cc.zookeeper.request;

import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

import java.util.List;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class CreateRequest extends AsyncRequest {

	private final CreateMode createMode;
	private final List<ACL> acl;
	private final byte[] data;

	public CreateRequest( byte[] data, String path, CreateMode createMode, List<ACL> acl) {
		this(data,path,null,createMode,acl);
	}

	public CreateRequest( byte[] data, String path, Object ctx, CreateMode createMode, List<ACL> acl) {
		super(ZkApis.CREATE, path, ctx);
		this.createMode = createMode;
		this.acl = acl;
		this.data = data;
	}

	public CreateMode createMode() {
		return createMode;
	}

	public List<ACL> acl() {
		return acl;
	}

	public byte[] data() {
		return data;
	}
}
