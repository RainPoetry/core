package com.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/15
 * description:
 */

import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import com.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.rainpoetry.common.db.JdbcManager;
import com.rainpoetry.common.db.relation.Oracle;
import com.rainpoetry.common.db.relation.Postgres;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GPMetaModify extends Modify {

	private static final String sql = "select a.*,b.* from\n" +
			"(select target_connect_id,target_table_name,source_table_name " +
			"from in_db_config where source_table_name=\"?\") a left JOIN\n" +
			"(select connect_id,database_type,database_name,db_server_ip,db_server_port," +
			"db_username,db_password,database_driver " +
			"from db_config_connect) b on a.target_connect_id = b.connect_id";

	public GPMetaModify(Connection connection) {
		super(connection);
	}

	@Subscribe
	public void deal(MetaCache meta) {
		String sql = meta.sql;
		commonDeal(sql);
	}

	public Map<String, Object> gpInfo(String table) {
		List<Map<String, Object>> list = JdbcManager.query(conn, sql.replace("?", table));
		if (list.size() > 0)
			return list.get(0);
		else
			logger.warn("could not found the mapping table for the source: " + table);
		return null;
	}

	public Connection parseConnect(Map<String, Object> gpInfo) {
		String ip = gpInfo.get("db_server_ip").toString();
		String port = gpInfo.get("db_server_port").toString();
		String database = gpInfo.get("database_name").toString();
		String username = gpInfo.get("db_username").toString();
		String password = gpInfo.get("db_password").toString();
		String driver = gpInfo.get("database_driver").toString();
		String url = "jdbc:postgresql://" + ip + ":" + port + "/" + database;
		return JdbcManager.connect(username, password, url, driver);
	}

	@Override
	public void add(String table, Multimap<String, AddOrModify.Column> maps) {
		Map<String, Object> gpInfo = gpInfo(table);
		if (gpInfo == null)
			return;
		String targetTable = gpInfo.get("target_table_name").toString();
		Connection conn = parseConnect(gpInfo);
		try {
			List<String> append = toList(maps);
			execute(conn,targetTable, append);
		} catch (Exception e) {
			e.printStackTrace();
		}
		close(conn);
	}

	private List<String> toList(Multimap<String, AddOrModify.Column> maps) throws Exception {
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, AddOrModify.Column> map : maps.entries()) {
			String operate = map.getKey();
			AddOrModify.Column column = map.getValue();
			String type = Oracle.of(column.type).to(Postgres.class);
			String append = "";
			if (!column.size.equalsIgnoreCase("0"))
				append = "(" + column.size + ")";
			result.add(operate + " " + column.name + " " + type + append);
		}
		return result;
	}

	private void execute(Connection conn,String table, List<String> append) {
		StringBuilder builder = new StringBuilder();
		builder.append("ALTER TABLE " + table + " ");
		for (int i = 0; i < append.size(); i++) {
			builder.append(append.get(i));
			if (i != append.size() - 1)
				builder.append(",");
		}
		logger.info("GP execute:" + builder.toString());
		JdbcManager.execute(conn, builder.toString());
	}

	@Override
	public void drop(String table, List<String> cols) {
		Map<String, Object> gpInfo = gpInfo(table);
		if (gpInfo == null)
			return;
		String targetTable = gpInfo.get("target_table_name").toString();
		Connection conn = parseConnect(gpInfo);
		List<String> append = new ArrayList<>();
		for(String s : cols) {
			append.add(" drop " + s);
		}
		execute(conn,targetTable,append);
		close(conn);
	}

	@Override
	public void reName(String table, String oldName, String newName) {
		Map<String, Object> gpInfo = gpInfo(table);
		if (gpInfo == null)
			return;
		String targetTable = gpInfo.get("target_table_name").toString();
		Connection conn = parseConnect(gpInfo);
		String sql = "ALTER TABLE "+targetTable+" rename "+oldName+" to "+newName;
		JdbcManager.execute(conn,sql);
		logger.info("GP execute:" + sql);
		close(conn);
	}

	private void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
