package com.rainpoetry.common.db.relation;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public final class Postgres extends DataBase{

	private static final BiMap<String, Types> cache;

	static {
		cache = new ImmutableBiMap.Builder<String, Types>()
				.put("CHAR", Types.CHAR)
				.put("VARCHAR", Types.VARCHAR)
				.put("INT2", Types.SMALLINT)
				.put("INT4", Types.INT)
				.put("INT8", Types.LONG)
				.put("FLOAT4", Types.FLOAT)
				.put("FLOAT8", Types.DOUBLE)
				.put("TEXT", Types.TEXT)
				.put("TIME", Types.TIME)
				.put("DATE", Types.DATE)
				.put("TIMESTAMP", Types.TIMESTAMP)
				.put("BYTEA", Types.BLOB)
				.put("DECIMAL", Types.DECIMAL)
				.put("NUMERIC", Types.NUMBER)
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
			throw new IllegalArgumentException( " 无法识别的 Type 类型： " + types);
	}
}
