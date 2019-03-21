package com.guava.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/20
 * description:
 */

import com.google.common.collect.Multimap;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;


public abstract class DDLOperate {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Connection conn;

	public DDLOperate(Connection connection) {
		this.conn = connection;
	}

	public abstract void add(String table, Multimap<String, AddOrModify.Column> maps);

	public abstract void drop(String table, List<String> cols);

	public abstract void reName(String table, String oldName, String newName);
}
