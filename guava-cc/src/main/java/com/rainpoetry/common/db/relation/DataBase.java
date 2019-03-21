package com.rainpoetry.common.db.relation;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import java.lang.reflect.Method;

public abstract class DataBase {

	//	private final BiMap<String, Types> cache;
////
////	public DataBase(BiMap<String, Types> cache) {
////		this.cache = cache;
////	}
////
////	public Types of(String msg) {
////		msg = msg.trim().toUpperCase();
////		if (cache.containsKey(msg))
////			return cache.get(msg);
////		else
////			throw new IllegalArgumentException(this.getClass()+"  无法识别的数据类型： " + msg);
////	}
////
////	public String compose(Types types) {
////		if (cache.inverse().containsKey(types))
////			return cache.inverse().get(types);
////		else
////			throw new IllegalArgumentException(this.getClass()+ " 无法识别的 Type 类型： " + types);
////	}
	public static Types from(Class<? extends DataBase> t, String type) throws Exception {
		Method m = t.getMethod("of", String.class);
		return (Types) m.invoke(null, type);
	}

}
