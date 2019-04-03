package com.cc.zookeeper.response;

import com.cc.zookeeper.request.DeleteRequest;
import com.cc.zookeeper.status.ZkApis;
import org.apache.zookeeper.KeeperException;

/**
 * User: chenchong
 * Date: 2019/1/14
 * description:
 */
public class DeleteResponse extends AsyncResponse{


	public DeleteResponse(KeeperException.Code code, String path, Object ctx, DeleteRequest request) {
		super(ZkApis.DELETE,code, path, ctx, request);
	}
}