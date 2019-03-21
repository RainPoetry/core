package com.rainpoetry.common.db.relation;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import java.lang.reflect.Method;

public enum  Types {

	CHAR,	 	// 定长字符
	VARCHAR, 	// 变长字符
	SMALLINT,   // 2 字节整型
	INT,		// 4 字节整型
	LONG,		// 8 字节整型
	FLOAT, 		// 单精度浮点
	DOUBLE,		// 双精度浮点
	DATE, 		// 日期+时间
	TIME,		// 时间
	TIMESTAMP,  // 日期+时间
	TEXT,		// 文本
	BLOB,		// 二进制
	DECIMAL,	// 可变精度
	NUMBER;		// 可控精度

	public <T extends DataBase> String to(Class<T> t) throws Exception {
		Method m =  t.getMethod("compose",Types.class);
		return m.invoke(null,this)+"";
	}

}
