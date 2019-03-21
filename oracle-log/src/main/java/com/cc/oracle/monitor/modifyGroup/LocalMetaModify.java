package com.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/15
 * description:
 */

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import com.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.cc.oracle.monitor.modifyGroup.tables.DataSetAndGPMapTable;
import com.cc.oracle.monitor.modifyGroup.tables.DataSetMetaTable;
import com.cc.oracle.monitor.modifyGroup.tables.RelativeTable;

import java.sql.Connection;
import java.util.List;

public class LocalMetaModify extends Modify{



	public LocalMetaModify(Connection connection) {
		super(connection);

	}

	@Subscribe
	public void deal(MetaCache meta) {
		String sql = meta.sql;
		commonDeal(sql);
	}


	@Override
	public void add(String table, Multimap<String, AddOrModify.Column> maps) {

	}

	@Override
	public void drop(String table, List<String> cols) {

	}

	@Override
	public void reName(String table, String oldName, String newName) {

	}
}
