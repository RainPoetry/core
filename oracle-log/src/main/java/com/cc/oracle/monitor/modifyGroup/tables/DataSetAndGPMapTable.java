package com.cc.oracle.monitor.modifyGroup.tables;

/*
 * User: chenchong
 * Date: 2019/3/20
 * description:
 */

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.rainpoetry.common.db.JdbcManager;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DataSetAndGPMapTable extends RelativeTable {

	private static final String query = "select * from in_config_fieldmapping \n" +
			"where in_db_config_id in " +
			"(select in_db_config_id from in_db_config where source_table_name='?') ";

	private static final String update = " update in_config_fieldmapping " +
			"set source_field=?,target_field=?," +
			"source_field_name=?, target_field_name=?" +
			" where source_field=? and in_db_config_id in " +
			"(select in_db_config_id from in_db_config where source_table_name=?) ";

	private static final String drop = "delete from in_config_fieldmapping " +
			"where source_field in (?) " +
			"and in_db_config_id in " +
			"(select in_db_config_id from in_db_config where source_table_name=?) ";


	private static final ImmutableMap<String, Function<AddOrModify.Column, String>> cache;

	static {
		cache = new ImmutableMap.Builder<String, Function<AddOrModify.Column, String>>()
				.put("source_field", column -> column.name)
				.put("target_field", column -> column.name)
				.put("source_field_name", column -> column.name)
				.put("target_field_name", column -> column.name)
				.put("source_field_type", column -> column.type)
				.put("target_field_type", column -> column.type)
				.put("source_field_length", column -> column.size)
				.put("target_field_length", column -> column.size)
				.build();
	}


	public DataSetAndGPMapTable(Connection connection) {
		super(connection);
	}

	@Override
	public void drop(String table, List<String> cols) {
		commonDrop(cols, table, drop);
		logger.info("表: in_config_fieldmapping , 删除字段： " + cols);
	}

	@Override
	public void reName(String table, String oldName, String newName) {
		if (oldName == null || newName == null)
			return;
		Object[] o = new Object[6];
		o[0] = o[1] = o[2] = o[3] = newName;
		o[4] = oldName;
		o[5] = table;
		JdbcManager.prepareExecute(conn, update, o);
		logger.info("表: in_config_fieldmapping , 字段重命名：" + oldName + " ==> " + newName);
	}


	@Override
	void add(String table, List<AddOrModify.Column> list) {
		String sql = query.replace("?", table);
		List<Map<String, Object>> dataList = JdbcManager.query(conn, sql);
		Map<String, Object> baseMap = dataList.get(0);
		List<Map<String, Object>> storeList = new ArrayList<>();
		int count = dataList.size();
		for (AddOrModify.Column column : list) {
			Map<String, Object> builderMap = Maps.newHashMap(baseMap);
			builderMap.put("in_config_fieldmapping_id", uuid());
			for (Map.Entry<String, Function<AddOrModify.Column, String>> entry : cache.entrySet()) {
				String title = entry.getKey();
				builderMap.put(title, entry.getValue().apply(column));
			}
			storeList.add(builderMap);
		}
		boolean b = JdbcManager.batchInsert(storeList, conn, "in_config_fieldmapping");
		String head = "表: in_config_fieldmapping ,新增 " + (b == true ? "成功" : "失败");
		logger.info(head + ": " + list);
	}


}
