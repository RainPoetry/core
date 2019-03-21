package com.guava.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/15
 * description:
 */

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.DDL;

import java.util.Map;

public class MetaCache{

	public final String  sql;
	public final String operation;

	public MetaCache(String sql, String operation) {
		this.sql = sql;
		this.operation = operation;
	}

}
