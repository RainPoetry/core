package com.cc.zookeeper;

import com.cc.utils.Logging;
import com.cc.zookeeper.exception.ZooKeeperClientAuthFailedException;
import com.cc.zookeeper.exception.ZooKeeperClientExpiredException;
import com.cc.zookeeper.exception.ZooKeeperClientTimeoutException;
import com.cc.zookeeper.handler.ZkHandler;
import com.cc.zookeeper.request.*;
import com.cc.zookeeper.response.*;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/1/11
 * description:	对 zookeeper 底层 API 的封装
 * <p>
 * 	优点：
 * 		1. 提供 zk 异常捕获处理
 * 		2. 支持并发处理
 * 		3. 内部实现了对 zk 节点的监控操作
 * 		4. 支持 Session 过期，自动重新建立连接
 * 		5. 异步处理 zk 请求
 * 		6. session 过期或者 zk 宕机了，通过 foreReinitialize() 来唤醒阻塞的 zk 请求
 */
public class ZookeeperClient extends Logging {

	private static final String ident = "[ZookeeperClient] ";
	private ReentrantReadWriteLock initializationLock = new ReentrantReadWriteLock();
	private ReentrantLock isConnectedOrExpiredLock = new ReentrantLock();
	private Condition isConnectedOrExpiredCondition = isConnectedOrExpiredLock.newCondition();

	// 控制同时访问上限
	private final Semaphore inFlightRequests;
	private volatile ZooKeeper zooKeeper;

	private final String connectString;
	private final int sessionTimeoutMs;
	private final int connectionTimeoutMs;

	private ZkScheduler expiryScheduler = new ZkScheduler(1, "zk-session-expiry-handler", true);

	private final ZkHandlerConcurrentMap<String, ZkHandler> zkHandlerMap = new ZkHandlerConcurrentMap<>(32, 0.75f);

	public ZookeeperClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, int maxInFlightRequests) throws IOException {
		super(ident);
		ZooKeeperClientWatcher watcher = new ZooKeeperClientWatcher();
		zooKeeper = new ZooKeeper(connectString, sessionTimeoutMs, watcher);
		inFlightRequests = new Semaphore(maxInFlightRequests);
		this.connectString = connectString;
		this.sessionTimeoutMs = sessionTimeoutMs;
		this.connectionTimeoutMs = connectionTimeoutMs;
		// 等待 连接到 zookeeper ，如果 connectionTimeOut 过短，会断开连接信息
		waitUntilConnected(connectionTimeoutMs, TimeUnit.MILLISECONDS);
		expiryScheduler.start();
	}

	public <E extends AsyncRequest> AsyncResponse handleRequest(AsyncRequest requests) {
		return handleRequests(Arrays.asList(requests)).peek();
	}

	public BlockingQueue<AsyncResponse> handleRequests(List<AsyncRequest> requests) {
		BlockingQueue<AsyncResponse> responseQueue = new ArrayBlockingQueue<>(requests.size());
		if (requests.size() == 0)
			return responseQueue;
		CountDownLatch latch = new CountDownLatch(requests.size());
		for (AsyncRequest request : requests) {
			try {
				inFlightRequests.acquire();
				ReentrantReadWriteLock.ReadLock readLock = initializationLock.readLock();
				readLock.lock();
				debug(request.path() + "-" + request.apiKeys() + ": 获取读权限");
				send(request, response->{
						responseQueue.add(response);
						inFlightRequests.release();
						latch.countDown();
				});
				debug(request.path() + "-" + request.apiKeys() + ": 释放读锁");
				readLock.unlock();
			} catch (InterruptedException e) {
				inFlightRequests.release();
				e.printStackTrace();
			}
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return responseQueue;
	}

	private <E extends AsyncRequest> void send(E request, Consumer<? super AsyncResponse> callBack) {
		switch (request.apiKeys()) {
			case EXISTS:
				ExistsRequest existsRequest = (ExistsRequest) request;
//				System.out.println("shouldWatch(existsRequest): " + shouldWatch(existsRequest));
				zooKeeper.exists(existsRequest.path(), shouldWatch(existsRequest), new AsyncCallback.StatCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, Stat stat) {
						callBack.accept(new ExistsResponse(KeeperException.Code.get(rc), path, ctx, stat, existsRequest));
					}
				}, request.ctx());
				break;
			case GETDATA:
				GetDataRequest getDataRequest = (GetDataRequest) request;
				zooKeeper.getData(getDataRequest.path(), shouldWatch(getDataRequest), new AsyncCallback.DataCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
						callBack.accept(new GetDataResponse(KeeperException.Code.get(rc), path, ctx, data, stat, getDataRequest));
					}
				}, request.ctx());
				break;
			case GETCHILDREN:
				GetChildrenRequest getChildrenRequest = (GetChildrenRequest) request;
				zooKeeper.getChildren(getChildrenRequest.path(), shouldWatch(getChildrenRequest), new AsyncCallback.Children2Callback() {
					@Override
					public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
						callBack.accept(new GetChildrenResponse(KeeperException.Code.get(rc), path, ctx, children, stat, getChildrenRequest));
					}
				}, request.ctx());
				break;
			case CREATE:
				CreateRequest createRequest = (CreateRequest) request;
				zooKeeper.create(request.path(), createRequest.data(), createRequest.acl(), createRequest.createMode(), new AsyncCallback.StringCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, String name) {
						callBack.accept(new CreateResponse(KeeperException.Code.get(rc), path, ctx, name, createRequest));
					}
				}, request.ctx());
				break;
			case SETDATA:
				SetDataRequest setDataRequest = (SetDataRequest) request;
				zooKeeper.setData(setDataRequest.path(), setDataRequest.data(), setDataRequest.version(), new AsyncCallback.StatCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, Stat stat) {
						callBack.accept(new SetDataResponse(KeeperException.Code.get(rc), path, ctx, stat, setDataRequest));
					}
				}, setDataRequest.ctx());
				break;
			case DELETE:
				DeleteRequest deleteRequest = (DeleteRequest) request;
				zooKeeper.delete(deleteRequest.path(), deleteRequest.version(), new AsyncCallback.VoidCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx) {
						callBack.accept(new DeleteResponse(KeeperException.Code.get(rc), path, ctx, deleteRequest));
					}
				}, deleteRequest.ctx());
				break;
			case GETACL:
				GetAclRequest getAclRequest = (GetAclRequest) request;
				zooKeeper.getACL(getAclRequest.path(), null, new AsyncCallback.ACLCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, List<ACL> acl, Stat stat) {
						callBack.accept(new GetAclResponse(KeeperException.Code.get(rc), path, ctx, acl, stat, getAclRequest));
					}
				}, request.ctx());
				break;
			case SETACL:
				SetAclRequest setACLRequest = (SetAclRequest) request;
				zooKeeper.setACL(setACLRequest.path(), setACLRequest.acl(), setACLRequest.version(), new AsyncCallback.StatCallback() {
					@Override
					public void processResult(int rc, String path, Object ctx, Stat stat) {
						callBack.accept(new SetAclResponse(KeeperException.Code.get(rc), path, ctx, stat, setACLRequest));
					}
				}, setACLRequest.ctx());
				break;
		}
	}

	private boolean shouldWatch(AsyncRequest request) {
		return zkHandlerMap.containsKey(request.path());
	}

	public ZooKeeper.States connectionState() {
		return zooKeeper.getState();
	}

	public Transaction createTransaction() {
		return zooKeeper.transaction();
	}

	public ZooKeeper currentZookeeper() {
		ZooKeeper zooKeeper;
		ReentrantReadWriteLock.ReadLock lock = initializationLock.readLock();
		lock.lock();
		zooKeeper = this.zooKeeper;
		lock.unlock();
		return zooKeeper;
	}

	public void registerZkHandler(ZkHandler handler) {
		zkHandlerMap.put(handler.path(), handler);
	}

	public void removeZkHandler(ZkHandler handler) {
		zkHandlerMap.remove(handler.path());
	}

	public void close() {
		info("Closing.");
		ReentrantReadWriteLock.WriteLock lock = initializationLock.writeLock();
		lock.lock();
		zkHandlerMap.clear();
		try {
			zooKeeper.close();
		} catch (InterruptedException e) {
			error("Close Fail");
		}
		lock.unlock();
		expiryScheduler.shutdown();
		info("Closed.");
	}

	public void waitUntilConnected() {
		waitUntilConnected(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	}

	private void waitUntilConnected(long timeout, TimeUnit timeUnit) {
		info("Waiting until connected.");
		long nanos = timeUnit.toNanos(timeout);
		isConnectedOrExpiredLock.lock();
		ZooKeeper.States state = connectionState();
		while (!state.isConnected() && state.isAlive()) {
			if (nanos <= 0) {
				throw new ZooKeeperClientTimeoutException("Timed out waiting for connection while in state: " + state);
			}
			try {
				// 阻塞等待其他线程唤醒
				nanos = isConnectedOrExpiredCondition.awaitNanos(nanos);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = connectionState();
			if (state == ZooKeeper.States.AUTH_FAILED) {
				throw new ZooKeeperClientAuthFailedException("Auth failed either before or while waiting for connection");
			} else if (state == ZooKeeper.States.CLOSED) {
				throw new ZooKeeperClientExpiredException("Session expired either before or while waiting for connection");
			}
		}
		isConnectedOrExpiredLock.unlock();
		info("Connected.");
	}

	public void foreReinitialize() {
		try {
			zooKeeper.close();
			reinitialize();
		} catch (InterruptedException e) {

		}
	}

	public void reinitialize() {
		ReentrantReadWriteLock.WriteLock lock = initializationLock.writeLock();
		lock.lock();
		if (connectionState().isAlive()) {
			try {
				zooKeeper.close();
				info("Initializing a new session to " + connectString);
				boolean connected = false;
				// 阻塞等待，直到连接成功
				while (!connected) {
					try {
						zooKeeper = new ZooKeeper(connectString, sessionTimeoutMs, new ZooKeeperClientWatcher());
						connected = true;
					} catch (Exception e) {
						info("Error when recreating ZooKeeper, retrying after a short sleep", e);
						Thread.sleep(1000);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lock.unlock();
	}

	private void scheduleSessionExpiryHandler() {
		expiryScheduler.scheduleOnce("zk-session-expired", new Runnable() {
			@Override
			public void run() {
				info("Session expired.");
				reinitialize();
			}
		});
	}

	private class ZooKeeperClientWatcher implements Watcher {

		public void process(WatchedEvent event) {
			debug("Received event: " + event);
			String path = event.getPath();
			if (path == null) {
				isConnectedOrExpiredLock.lock();
				// 唤醒阻塞的线程
				isConnectedOrExpiredCondition.signalAll();
				isConnectedOrExpiredLock.unlock();
				Event.KeeperState state = event.getState();
				if (state == Event.KeeperState.AuthFailed) {
					error("Auth failed.");
				} else if (state == Event.KeeperState.Expired) {
					info("Session expired.");
					scheduleSessionExpiryHandler();
				}
			} else {
				switch (event.getType()) {
					case NodeChildrenChanged:
						zkHandlerMap.getAndExecute(path,v->v.handleChildChange());
						break;
					case NodeDataChanged:
						zkHandlerMap.getAndExecute(path,v->v.handleDataChange());
						break;
					case NodeCreated:
						zkHandlerMap.getAndExecute(path,v->v.handleCreation());
						break;
					case NodeDeleted:
						zkHandlerMap.getAndExecute(path,v->v.handleDeletion());
						break;
				}
			}

		}
	}

}
