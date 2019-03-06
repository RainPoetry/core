package com.cc.source;

import com.cc.config.AbstractConfig;
import com.cc.config.ConfigDef;
import com.cc.config.validate.NonEmptyString;

import static com.cc.config.ConfigDef.Type.*;


import java.util.Map;

/**
 * User: chenchong
 * Date: 2019/2/26
 * description:
 */
public class CommonDirSourceConfig extends AbstractConfig{

	private static final ConfigDef CONFIG;

	// 采集目录
	public static final String SPOOL_DIRECTORY = "dir";
	// 采集主机
	public static final String HOST_CONFIG = "host";
	public static final String HOST_CONFIG_DEFAULT = "localhost";
	// 匹配规则
	public static final String INCLUDE_PATTERN = "includePattern";
	private static final String INCLUDE_PATTERN_DEFAULT = "^.*$";
	// 过滤规则
	public static final String IGNORE_PATTERN = "ignorePattern";
	public static final String IGNORE_PATTERN_DEFAULT = "^$";

	static {
		CONFIG = new ConfigDef()
				.define(SPOOL_DIRECTORY,STRING,new NonEmptyString())
				.define(HOST_CONFIG,STRING,HOST_CONFIG_DEFAULT,new NonEmptyString())
				.define(INCLUDE_PATTERN,STRING,INCLUDE_PATTERN_DEFAULT,null)
				.define(IGNORE_PATTERN,STRING,IGNORE_PATTERN_DEFAULT,null);

	}

	public CommonDirSourceConfig(Map<?, ?> originals) {
		super(CONFIG,originals);
	}
}
