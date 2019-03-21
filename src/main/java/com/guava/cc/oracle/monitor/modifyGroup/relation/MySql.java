package com.guava.cc.oracle.monitor.modifyGroup.relation;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public final class MySql extends DataBase {

	private static final BiMap<String, Types> cache;

	static {
		cache = new ImmutableBiMap.Builder<String, Types>()
				.put("CHAR", Types.CHAR)
				.put("VARCHAR", Types.VARCHAR)
				.put("SMALLINT", Types.SMALLINT)
				.put("INT", Types.INT)
				.put("BIGINT", Types.LONG)
				.put("FLOAT", Types.FLOAT)
				.put("DOUBLE", Types.DOUBLE)
				.put("TEXT", Types.TEXT)
				.put("DATE", Types.DATE)
				.put("TIMESTAMP", Types.TIMESTAMP)
				.put("BLOB", Types.BLOB)
				.put("DECIMAL", Types.DECIMAL)
				.build();
	}

	public static Types of(String msg) {
		msg = msg.trim().toUpperCase();
		if (cache.containsKey(msg))
			return cache.get(msg);
		else
			throw new IllegalArgumentException("  无法识别的数据类型： " + msg);
	}

	public static String compose(Types types) {
		if (cache.inverse().containsKey(types))
			return cache.inverse().get(types);
		else
			throw new IllegalArgumentException(" 无法识别的 Type 类型： " + types);
	}

}
