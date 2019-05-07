package com.cc.oracle.monitor.logminer;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogMiner {

	private static final Logger logger = LoggerFactory.getLogger(LogMiner.class);

	private Connection conn;

	private final LogMinerConfig config;
	private final String clients;
	public int maxScn;

	public LogMiner(Connection conn, LogMinerConfig config) {
		this.conn = conn;
		this.config = config;
		String[] clients = config.getString(LogMinerConfig.SOURCE_CLIENT_USERNAME).split(",");
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (int i = 0; i < clients.length; i++) {
			String s = clients[i];
			builder.append("'" + s.toUpperCase() + "'");
			if (i != clients.length - 1)
				builder.append(",");
		}
		builder.append(")");
		this.clients = builder.toString();
	}

	public List<Map<String,String>> analysis(String scn) {
		if (scn == null)
			scn = config.getString(LogMinerConfig.LAST_SCN);
		try {
			ResultSet resultSet = null;
			Statement statement = conn.createStatement();
			// 确保字典文件存在，没有则生成一个
			validDictionary();
			// 添加日志文件
			CallableStatement callableStatement = conn.prepareCall(addLogsSQL());
			callableStatement.execute();
			// 打印获分析日志文件信息
			resultSet = statement.executeQuery("SELECT db_name, thread_sqn, filename FROM v$logmnr_logs");
			while (resultSet.next())
				logger.debug("add log file ==>" + resultSet.getObject(3));
			// 分析字典文件,
			boolean retry = false;
			callableStatement = conn.prepareCall(analysisLogSQL(scn));
			try {
				callableStatement.execute();
			}catch (SQLException e) {
				logger.warn("scn is unExpected ：" + scn +" ,  set scn = 0 and retry");
				retry = true;
				callableStatement = conn.prepareCall(analysisLogSQL("0"));
				callableStatement.execute();
			}
			String sql = "SELECT scn,operation,timestamp,status,sql_redo " +
						" FROM v$logmnr_contents" +
						" WHERE seg_owner in " + clients + " " +
						" AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'";
			if (retry)
				sql += "  and scn > "+scn;
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] model = new String[metaData.getColumnCount()];
			for (int i = 0; i < model.length; i++)
				model[i] = metaData.getColumnName(i + 1);
			boolean update = false;
			maxScn = Integer.parseInt(scn.trim());
			List<Map<String,String>> result = new ArrayList<>();
			Map<String,String> map;
			while (resultSet.next()) {
				map = new HashMap<>();
				String scn_new = resultSet.getString("scn");
				if (scn_new.equals(scn.trim()))
					continue;
				maxScn = Math.max(maxScn, Integer.parseInt(scn_new));
				String operation = resultSet.getString("operation");
				// 如果发生了 DDL 操作，需要重新生成字典文件
				if (operation.equalsIgnoreCase("DDL"))
					update = true;
				String sql_redo = resultSet.getString("sql_redo");
				map.put("operation",operation);
				map.put("sql",sql_redo);
				result.add(map);
			}
			if (update) {
				logger.debug("updating the dictionary");
				generateDir();
				logger.debug("updated the dictionary");
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
//			throw new IllegalStateException("SQL error: " + e.getMessage());
		}
	}

	// 检验 字典文件的可用性
	public void validDictionary() throws SQLException {
		String fileName = config.getString(LogMinerConfig.DATA_DICTIONARY_FILENAME);
		String dictionary = config.getString(LogMinerConfig.DATA_DICTIONARY);
		String dicPath = dictionary + File.separator + fileName;
		if (!new File(dicPath).exists())
			generateDir();
	}

	// 当发生 DDL 时，重新生成字典文件
	private void generateDir() throws SQLException {
		String fileName = config.getString(LogMinerConfig.DATA_DICTIONARY_FILENAME);
		String dictionary = config.getString(LogMinerConfig.DATA_DICTIONARY);
		StringBuilder builder = new StringBuilder();
		builder.append(" BEGIN");
		builder.append(" dbms_logmnr_d.build(dictionary_filename => '" + fileName + "' ,dictionary_location =>'"
				+ dictionary + "'); ");
		builder.append(" END;");
		CallableStatement callableStatement = conn.prepareCall(builder.toString());
		callableStatement.execute();
	}

	// 添加分析日志
	private String addLogsSQL() {
		String logPath = config.getString(LogMinerConfig.LOG_PATH);
		// 多线程操作字符串缓冲区下操作大量数据 StringBuffer；
		// 单线程操作字符串缓冲区下操作大量数据 StringBuilder（推荐使用）。
		StringBuilder sbSQL = new StringBuilder();
		sbSQL.append(" BEGIN");
		sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + logPath + "\\REDO01.LOG', " +
				"options=>dbms_logmnr.NEW);");
		sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + logPath + "\\REDO02.LOG', " +
				"options=>dbms_logmnr.ADDFILE);");
		sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + logPath + "\\REDO03.LOG', " +
				"options=>dbms_logmnr.ADDFILE);");
		sbSQL.append(" END;");
		return sbSQL.toString();
	}

	// 分析日志
	private String analysisLogSQL(String scn) throws SQLException {
		String fileName = config.getString(LogMinerConfig.DATA_DICTIONARY_FILENAME);
		String dictionary = config.getString(LogMinerConfig.DATA_DICTIONARY);
		String filePath = dictionary + File.separator + fileName;
		StringBuilder builder = new StringBuilder();
		builder.append(" BEGIN");
		builder.append(" dbms_logmnr.start_logmnr( ");
		builder.append(" startScn=>" + scn + ", ");
		builder.append(" dictfilename=>'" + filePath + "', ");
		builder.append(" OPTIONS =>DBMS_LOGMNR.COMMITTED_DATA_ONLY+dbms_logmnr.NO_ROWID_IN_STMT ");
		builder.append(" );");
		builder.append(" END;");
		return builder.toString();
	}

	public static class Builder {
		private Map<String, Object> map;
		private Connection conn;

		public Builder() {
			this.map = new HashMap<>();
		}

		public Builder scn(String scn) {
			map.put(LogMinerConfig.LAST_SCN, scn);
			return this;
		}

		public Builder connect(Connection conn) {
			this.conn = conn;
			return this;
		}

//		public Builder connUserName(String userName) {
//			map.put(LogMinerConfig.SOURCE_DATABASE_USERNAME, userName);
//			return this;
//		}
//
//		public Builder connPassword(String password) {
//			map.put(LogMinerConfig.SOURCE_DATABASE_PASSWORD, password);
//			return this;
//		}
//
//		public Builder connUrl(String url) {
//			map.put(LogMinerConfig.SOURCE_DATABASE_URL, url);
//			return this;
//		}

		public Builder client(String clients) {
			map.put(LogMinerConfig.SOURCE_CLIENT_USERNAME, clients);
			return this;
		}

		public Builder logPath(String path) {
			map.put(LogMinerConfig.LOG_PATH, path);
			return this;
		}

		public Builder dataPath(String path) {
			map.put(LogMinerConfig.DATA_DICTIONARY, path);
			return this;
		}

		public Builder dataFile(String fileName) {
			map.put(LogMinerConfig.DATA_DICTIONARY_FILENAME, fileName);
			return this;
		}

		public Builder config(Map<String, Object> configs) {
			map.putAll(configs);
			return this;
		}

		public LogMiner build() {
			if (conn == null)
				throw new LogMinerConfigException("connection info must not be empty!");
			return new LogMiner(conn, new LogMinerConfig(map));
		}
	}
}
