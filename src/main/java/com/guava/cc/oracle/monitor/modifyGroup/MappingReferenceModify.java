package com.guava.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/15
 * description:
 */

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.guava.cc.oracle.monitor.modifyGroup.tables.DataSetAndGPMapTable;
import com.guava.cc.oracle.monitor.modifyGroup.tables.DataSetMetaTable;
import com.guava.cc.oracle.monitor.modifyGroup.tables.RelativeTable;

import java.sql.Connection;
import java.util.List;

public class MappingReferenceModify extends Modify {

	private final ImmutableList<RelativeTable> cache;

	public MappingReferenceModify(Connection connection) {
		super(connection);
		cache = new ImmutableList.Builder<RelativeTable>()
				.add(new DataSetAndGPMapTable(connection))
				.add(new DataSetMetaTable(connection))
				.build();
	}

	@Subscribe
	public void deal(MetaCache meta) {
		String sql = meta.sql;
		commonDeal(sql);
	}


	@Override
	public void add(String table, Multimap<String, AddOrModify.Column> maps) {
		cache.forEach(it -> it.add(table, maps));
	}

	@Override
	public void drop(String table, List<String> cols) {
		cache.forEach(it -> it.drop(table, cols));
	}

	@Override
	public void reName(String table, String oldName, String newName) {
		cache.forEach(it -> it.reName(table, oldName, newName));
	}

}
