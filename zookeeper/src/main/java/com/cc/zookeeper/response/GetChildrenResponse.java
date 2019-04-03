package com.cc.zookeeper.response;

import com.cc.zookeeper.request.GetChildrenRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class GetChildrenResponse extends AsyncResponse {

	private final List<String> children;
	private final Stat stat;

	public GetChildrenResponse(KeeperException.Code code, String path, Object ctx, List<String> children, Stat stat, GetChildrenRequest request) {
		super(ZkApis.GETCHILDREN, code, path, ctx, request);
		this.children = children;
		this.stat = stat;
	}

	public List<String> children() {
		return children;
	}

	public Stat stat() {
		return stat;
	}
}
