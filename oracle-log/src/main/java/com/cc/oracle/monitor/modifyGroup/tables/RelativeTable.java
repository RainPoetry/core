package com.cc.oracle.monitor.modifyGroup.tables;

/*
 * User: chenchong
 * Date: 2019/3/20
 * description:
 */

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.cc.oracle.monitor.modifyGroup.DDLOperate;
import com.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.rainpoetry.common.db.JdbcManager;


import java.sql.Connection;
import java.util.*;
import java.util.function.Function;

public abstract class RelativeTable extends DDLOperate {

	public RelativeTable(Connection connection) {
		super(connection);
	}

	@Override
	public void add(String table, Multimap<String, AddOrModify.Column> maps) {
		add(table,toList(maps));
	}

	abstract void add(String table, List<AddOrModify.Column> list);

	private List<AddOrModify.Column> toList(Multimap<String, AddOrModify.Column> maps) {
		List<AddOrModify.Column> result = new ArrayList<>();
		for (Map.Entry<String, AddOrModify.Column> map : maps.entries()) {
			String operate = map.getKey();
			if (operate.equalsIgnoreCase("ADD"))
				result.add(map.getValue());
		}
		return result;
	}

	protected String uuid() {
		return UUID.randomUUID().toString().replaceAll("\\-","");
	}

	protected void commonDrop(List<String> cols, String table, String drop) {
		if (cols == null || cols.size() == 0)
			return;
		StringBuilder builder = new StringBuilder();
		Iterator<String> it = cols.iterator();
		while (it.hasNext()) {
			String s = it.next();
			builder.append("" + s + "");
			if (it.hasNext())
				builder.append(",");
		}
		Object[] o = new Object[2];
		o[0] = builder.toString();
		o[1] = table;
		JdbcManager.prepareExecute(conn, drop, o);
	}

}
