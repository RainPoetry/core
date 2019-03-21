package com.guava.cc.oracle.monitor.logminer;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.rainpoetry.common.config.AbstractConfig;
import com.rainpoetry.common.config.ConfigDef;
import com.rainpoetry.common.config.validate.NonEmptyString;

import java.util.Map;

import static com.rainpoetry.common.config.ConfigDef.Type.*;

public class LogMinerConfig extends AbstractConfig {

	private static final ConfigDef CONFIG;

	/**
	 * 上次数据同步最后SCN号
	 */
	public static String LAST_SCN = "scn";
	public static String LAST_SCN_DEFAULT = "0";

	/**
	 * 源数据库配置
	 */
	public static String DATABASE_DRIVER = "source.driver";
	public static String DATABASE_DRIVER_DEFAULT = "oracle.jdbc.driver.OracleDriver";

	// jdbc:oracle:thin:@localhost:1521:orcl
	public static String SOURCE_DATABASE_URL = "source.url";

	// pccgz
	public static String SOURCE_DATABASE_USERNAME = "source.username";

	// pccgz
	public static String SOURCE_DATABASE_PASSWORD = "source.password";

	// pccgz
	public static String SOURCE_CLIENT_USERNAME = "client.user";

	/**
	 * 日志文件路径
	 */
	// E:\ORACLEDB\ORADATA\ORCL
	public static String LOG_PATH = "log.path";

	/**
	 * 数据字典路径
	 */
	// E:\oracledb\dic
	public static String DATA_DICTIONARY = "data.path";

	// 数据字典文件名称
	// dictionary.ora
	public static String DATA_DICTIONARY_FILENAME = "data.file.name";

	static {
		CONFIG = new ConfigDef()
				.define(LAST_SCN, STRING, LAST_SCN_DEFAULT, new NonEmptyString())
//				.define(DATABASE_DRIVER, STRING, DATABASE_DRIVER_DEFAULT, new NonEmptyString())
//				.define(SOURCE_DATABASE_URL, STRING, new NonEmptyString())
//				.define(SOURCE_DATABASE_USERNAME, STRING, new NonEmptyString())
//				.define(SOURCE_DATABASE_PASSWORD, STRING, new NonEmptyString())
				.define(SOURCE_CLIENT_USERNAME, STRING, new NonEmptyString())
				.define(LOG_PATH, STRING, new NonEmptyString())
				.define(DATA_DICTIONARY, STRING, new NonEmptyString())
				.define(DATA_DICTIONARY_FILENAME, STRING, new NonEmptyString());
	}

	public LogMinerConfig(Map<?, ?> originals) {
		super(CONFIG, originals);
	}
}
