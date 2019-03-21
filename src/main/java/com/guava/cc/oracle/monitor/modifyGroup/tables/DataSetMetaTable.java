package com.guava.cc.oracle.monitor.modifyGroup.tables;

/*
 * User: chenchong
 * Date: 2019/3/20
 * description:
 */

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.guava.cc.oracle.utils.db.JdbcManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DataSetMetaTable extends RelativeTable {

	private static final String sql = "select * from com_class_item_rel where rescataid in " +
			" (select dataclassid from in_db_config where source_table_name='?')";

	private static final String update = " update com_class_item_rel set name=?,name_en=?,name_item=?" +
			" where name_item=? and rescataid in " +
			" (select dataclassid from in_db_config where source_table_name=?)";

	private static final String drop = "delete from com_class_item_rel where name_item in (?) and rescataid in " +
			" (select dataclassid from in_db_config where source_table_name=?)";

	private static final ImmutableMap<String, Function<AddOrModify.Column, String>> cache;

	static {
		cache = new ImmutableMap.Builder<String, Function<AddOrModify.Column, String>>()
				.put("name", column -> column.name)
				.put("name_en", column -> column.name)
				.put("name_item", column -> column.name)
				.put("datatype", column -> column.type)
				.put("length", column -> column.size)
				.build();
	}

	public DataSetMetaTable(Connection connection) {
		super(connection);
	}

	@Override
	public void drop(String table, List<String> cols) {
		commonDrop(cols, "com_class_item_rel", drop);
		logger.info("表: com_class_item_rel , 删除字段： " + cols);
	}

	@Override
	public void reName(String table, String oldName, String newName) {
		if (oldName == null || newName == null)
			return;
		Object[] o = new Object[5];
		o[0] = o[1] = o[2] = newName;
		o[3] = oldName;
		o[4] = table;
		JdbcManager.prepareExecute(conn, update, o);
		logger.info("表: com_class_item_rel ,字段重命名：" + oldName + " ==> " + newName);
	}

	@Override
	void add(String table, List<AddOrModify.Column> list) {
		String query = sql.replace("?", table);
		List<Map<String, Object>> dataList = JdbcManager.query(conn, query);
		Map<String, Object> baseMap = dataList.get(0);
		List<Map<String, Object>> storeList = new ArrayList<>();
		int count = dataList.size();
		for (AddOrModify.Column column : list) {
			Map<String, Object> builderMap = Maps.newHashMap(baseMap);
			builderMap.put("sequence", ++count);
			builderMap.put("dataitemid", uuid());
			for (Map.Entry<String, Function<AddOrModify.Column, String>> entry : cache.entrySet()) {
				String title = entry.getKey();
				builderMap.put(title, entry.getValue().apply(column));
			}
			storeList.add(builderMap);
		}
		boolean b = JdbcManager.batchInsert(storeList, conn, "com_class_item_rel");
		String head = "表: com_class_item_rel ,新增 " + (b == true ? "成功" : "失败");
		logger.info(head + ": " + list);
	}


}
