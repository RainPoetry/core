package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */



import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Arrays;

public class Demo2 {

	// 定义数据库的驱动信息
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	// 定义访问数据库的地址
	private final static String URL = "jdbc:mysql://172.18.130.33:3306/hkbdst";

	public static void main(String[] args) throws Exception {
//		Connection connection = JdbcManager.connect("admin","admin",URL, DRIVER);
//		DataSetMetaTable table = new DataSetMetaTable(connection);
//		table.reName("TME_LOG_BASE","addd","newName");
//		table.reName("TME_LOG_BASE","newName","shuoming");
	}
}
