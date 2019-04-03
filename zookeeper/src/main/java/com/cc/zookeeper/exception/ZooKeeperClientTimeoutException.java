package com.cc.zookeeper.exception;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public class ZooKeeperClientTimeoutException extends ZooKeeperClientException {

	public ZooKeeperClientTimeoutException(String msg) {
		super(msg);
	}
}
