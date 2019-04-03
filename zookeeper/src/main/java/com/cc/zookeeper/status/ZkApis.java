package com.cc.zookeeper.status;

/**
 * User: chenchong
 * Date: 2019/1/12
 * description:
 */
public enum  ZkApis {

	CREATE(0, "create"),
	DELETE(1, "delete"),
	EXISTS(2,"exists"),
	GETACL(3, "getACL"),
	GETCHILDREN(4,"getChildren"),
	GETDATA(5, "getData"),
	SETACL(6, "getAcl"),
	SETDATA(7, "setData");

	private short id;
	private String name;

	ZkApis(int id, String name) {
		this.name = name;
		this.id = (short)id;
	}
}
