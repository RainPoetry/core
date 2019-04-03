package com.cc.zookeeper;

import com.cc.utils.Logging;
import com.cc.zookeeper.handler.ZkHandler;
import com.cc.zookeeper.request.*;
import com.cc.zookeeper.response.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:	根据业务逻辑对 ZookeeperClient 进行封装
 */
public class ZkClient extends Logging {

	private static final String ident = "[zkClient] ";
	private final ZookeeperClient zookeeperClient;

	public ZkClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, int maxInflightRequests) throws IOException {
		super(ident);
		zookeeperClient = new ZookeeperClient(connectString, sessionTimeoutMs, connectionTimeoutMs, maxInflightRequests);
	}

	public String createSequentialPersistentPath(String path, byte[] data) {
		CreateRequest createRequest = new CreateRequest(data, path, CreateMode.EPHEMERAL_SEQUENTIAL, ZooDefs.Ids.OPEN_ACL_UNSAFE);
		CreateResponse createResponse = (CreateResponse) retryRequestUntilConnected(createRequest);
		createResponse.maybeThrow();
		return createResponse.name();
	}

	public boolean existNode(String path) {
		ExistsRequest request = new ExistsRequest(path);
		ExistsResponse response = (ExistsResponse) retryRequestUntilConnected(request);
		response.maybeThrow();
		return response.stat() == null;
	}

	// 递归删除节点
	public boolean deleteRecursive(String path, int expectedControllerEpochZkVersion) {
		GetChildrenResponse getChildrenResponse = (GetChildrenResponse) retryRequestUntilConnected(new GetChildrenRequest(path));
		switch (getChildrenResponse.code()) {
			case OK:
				// 递归删除子节点
				getChildrenResponse.children().forEach(child -> {
					deleteRecursive(path + "/" + child, expectedControllerEpochZkVersion);
				});
				DeleteResponse deleteResponse = (DeleteResponse) retryRequestUntilConnected(new DeleteRequest(path, expectedControllerEpochZkVersion));
				if (deleteResponse.code() != KeeperException.Code.OK && deleteResponse.code() != KeeperException.Code.NONODE)
					deleteResponse.maybeThrow();
				return true;
			case NONODE:
				return false;
			default:
				getChildrenResponse.maybeThrow();
				return false;
		}
	}

	public void createRecursive(String path, byte[] data) {
		createRecursive(path, data, true);
	}

	private String parentPath(String path) {
		int indexOfLastSlash = path.lastIndexOf("/");
		if (indexOfLastSlash == -1) throw new IllegalArgumentException("Invalid path :" + path);
		return path.substring(0, indexOfLastSlash);
	}

	private void createRecursive0(String path) {
		CreateRequest createRequest = new CreateRequest(null, path, CreateMode.PERSISTENT, ZooDefs.Ids.OPEN_ACL_UNSAFE);
		CreateResponse createResponse = (CreateResponse) retryRequestUntilConnected(createRequest);
		if (createResponse.code() == KeeperException.Code.NONODE) {
			createRecursive0(parentPath(path));
			createResponse = (CreateResponse) retryRequestUntilConnected(createRequest);
			if (createResponse.code() != KeeperException.Code.OK && createResponse.code() != KeeperException.Code.NODEEXISTS) {
				createResponse.maybeThrow();
			}
		} else if (createResponse.code() != KeeperException.Code.OK && createResponse.code() != KeeperException.Code.NODEEXISTS) {
			createResponse.maybeThrow();
		}
	}

	// 递归创建节点
	public void createRecursive(String path, byte[] data, Boolean throwIfPathExists) {
		CreateRequest createRequest = new CreateRequest(data, path, CreateMode.PERSISTENT, ZooDefs.Ids.OPEN_ACL_UNSAFE);
		CreateResponse createResponse = (CreateResponse) retryRequestUntilConnected(createRequest);
		if (throwIfPathExists && createResponse.code() == KeeperException.Code.NODEEXISTS) {
			createResponse.maybeThrow();
		} else if (createResponse.code() == KeeperException.Code.NONODE) {
			createRecursive0(parentPath(path));
			createResponse = (CreateResponse) retryRequestUntilConnected(createRequest);
			if (throwIfPathExists || createResponse.code() != KeeperException.Code.NODEEXISTS)
				createResponse.maybeThrow();
		} else if (createResponse.code() != KeeperException.Code.NODEEXISTS)
			createResponse.maybeThrow();
	}

	// 事务的处理
	/*public void createAndSetNodeData() {
		Transaction transaction = zookeeperClient.createTransaction();
		String path = "/cc";
		transaction.create(path, "sasa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		transaction.setData("/1", "new Data".getBytes(), -1);
		try {
			transaction.commit();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}*/

	public void registerZkHandler(ZkHandler handler) {
		zookeeperClient.registerZkHandler(handler);
	}

	public void removeZkHandler(ZkHandler handler) {
		zookeeperClient.removeZkHandler(handler);
	}


	public <E extends AsyncRequest> AsyncResponse retryRequestUntilConnected(AsyncRequest request) {
		return retryRequestUntilConnected(Arrays.asList(request)).peek();
	}

	// 连接异常时，自动等待重新连接
	public BlockingQueue<AsyncResponse> retryRequestUntilConnected(List<AsyncRequest> requests) {
		BlockingQueue<AsyncResponse> responseQueue = new ArrayBlockingQueue<>(requests.size());
		List<AsyncRequest> remainingRequest = new ArrayList<>(requests);
		while (remainingRequest.size() > 0) {
			BlockingQueue<AsyncResponse> batchResponse = zookeeperClient.handleRequests(remainingRequest);
			remainingRequest.clear();
			batchResponse.forEach(response -> {
				if (response.code() == KeeperException.Code.CONNECTIONLOSS) {
					remainingRequest.add(response.request());
				} else {
					responseQueue.add(response);
				}
			});
			if (remainingRequest.size() > 0)
				zookeeperClient.waitUntilConnected();
		}
		return responseQueue;
	}


}
