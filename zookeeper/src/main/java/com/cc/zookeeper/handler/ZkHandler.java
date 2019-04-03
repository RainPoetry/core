package com.cc.zookeeper.handler;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public interface ZkHandler {
	String path();
	void handleCreation();
	void handleDeletion();
	void handleDataChange();
	void handleChildChange();
}
