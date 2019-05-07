package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */




import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Demo {

	/** 上次数据同步最后SCN号 */
	public static String LAST_SCN = "0";

	/** 源数据库配置 */
	public static String DATABASE_DRIVER="oracle.jdbc.driver.OracleDriver";
	public static String SOURCE_DATABASE_URL="jdbc:oracle:thin:@localhost:1521:orcl";
	public static String SOURCE_DATABASE_USERNAME="pccgz";
	public static String SOURCE_DATABASE_PASSWORD="pccgz";
	public static String SOURCE_CLIENT_USERNAME = "pccgz";

	/** 源数据库配置 */
	// 定义数据库的用户名
	private final static String USERNAME = "admin";
	// 定义数据库的密码
	private final static String PASSWORD = "admin";
	// 定义数据库的驱动信息
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	// 定义访问数据库的地址
	private final static String URL = "jdbc:mysql://172.18.130.33:3306/hkbdst";
	/** 日志文件路径 */
	public static String LOG_PATH = "E:\\ORACLEDB\\ORADATA\\ORCL";

	/** 数据字典路径 */
	public static String DATA_DICTIONARY = "E:\\oracledb\\dic";

	public static void main(String[] args) throws Exception {
//		String type = MySql.of("BIGINT").to(Postgres.class);
//		String type2 = DataBase.from(MySql.class,"BIGINT").to(Postgres.class);
//		System.out.println(type);
	}
}
