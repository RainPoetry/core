package com.rainpoetry.common.db;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import com.rainpoetry.common.db.relation.*;
import com.rainpoetry.common.db.relation.Types;
import com.rainpoetry.common.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcManager {

	private static final Logger log = LoggerFactory.getLogger(JdbcManager.class);

	public static Connection connect(String userName, String password, String url, String driver) {
		Connection conn = null;
		try {
			// 加载驱动
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static List<Map<String, Object>> query(Connection conn, String sql) {
		List<Map<String, Object>> result = new ArrayList<>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			result.addAll(toListMap(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeIgnoreException(rs);
			closeIgnoreException(st);
		}
		return result;
	}

	public static List<Map<String, Object>> prepare(Connection conn, String sql, Object[] parameters) {
		List<Map<String, Object>> result = new ArrayList<>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = conn.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++)
				pstm.setObject(i+1, parameters[i]);
			rs = pstm.executeQuery();
			result.addAll(toListMap(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeIgnoreException(rs);
			closeIgnoreException(pstm);
		}
		return result;
	}

	public static void prepareExecute(Connection conn, String sql, Object[] parameters) {
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				pstm.setObject(i+1, parameters[i]);
			}
			pstm.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private static List<Map<String, Object>> toListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();
		ResultSetMetaData metaData = rs.getMetaData();
		String[] model = new String[metaData.getColumnCount()];
		for (int i = 0; i < model.length; i++)
			model[i] = metaData.getColumnName(i + 1);
		Map<String, Object> map;
		while (rs.next()) {
			map = new HashMap<>();
			for (String s : model)
				map.put(s, rs.getObject(s));
			result.add(map);
		}
		return result;
	}

	public static void execute(Connection conn, String sql) {
		try {
			Statement st = conn.createStatement();
			st.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> tableMetaInfo(String table, Connection conn, String schema) {
		Map<String, String> result = new HashMap<>();
		DatabaseMetaData metaData = null;
		ResultSet rs = null;
		try {
			metaData = conn.getMetaData();
			rs = metaData.getColumns(conn.getCatalog(), schema, table, null);
			while (rs.next()) {
				result.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeIgnoreException(rs);
		}
		return result;
	}

	public static Map<String, String> tableMetaInfo(String table, Connection conn) {
		return tableMetaInfo(table, conn, null);
	}


	public static boolean batchInsert(List<Map<String, Object>> dataList, Connection conn, String table, String schema) {
		PreparedStatement pstm = null;
		Class<? extends DataBase> c = sourceType(conn);
		if (c == null)
			return false;
		Map<String, String> columnModel = tableMetaInfo(table, conn, schema);
		if (columnModel == null || columnModel.size() == 0)
			return false;
		String sql = buildInsertSQL(columnModel, table, schema,c);
		try {
			int success = 0;
			conn.setAutoCommit(false);
			pstm = conn.prepareStatement(sql);
			for (int m = 0, n = dataList.size(); m < n; m++) {
				Map<String, Object> dataMap = dataList.get(m);
				dataMap = transMapToUpper(dataMap);
				int count = 0;
				for (Map.Entry<String, String> entry : columnModel.entrySet()) {
					String name = entry.getKey().toUpperCase();
					if (!dataMap.containsKey(name)) {
						pstm.setString(++count, null);
						continue;
					}
					Object value = dataMap.get(name);
					if (value == null || value.toString().trim().length() == 0) {
						pstm.setString(++count, null);
						continue;
					}
					Types type = DataBase.from(c, entry.getValue());
					typeDeal(type, pstm, value, ++count);
				}
				pstm.addBatch();
				if ((m + 1) % 1000 == 0) {
					pstm.executeBatch();
					conn.commit();
					pstm.clearBatch();
				}
			}
			pstm.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeIgnoreException(pstm);
		}
	}

	private static void typeDeal(Types types, PreparedStatement pstm, Object value, int count) throws SQLException, ParseException {
		switch (types) {
			case CHAR:
			case TEXT:
			case VARCHAR:
				pstm.setString(count, value.toString());
				break;
			case SMALLINT:
				Short s;
				if (value instanceof Short) {
					s = (Short) value;
				} else if (value instanceof String) {
					try {
						s = Short.parseShort(value.toString().trim());
					} catch (Exception e) {
						log.error(value + "  转换为 Short时异常,因此入库为 null ");
						s = null;
					}
				} else {
					s = null;
				}
				pstm.setShort(count, s);
				break;
			case INT:
				Integer i;
				if (value instanceof Integer) {
					i = (Integer) value;
				} else if (value instanceof String) {
					try {
						i = Integer.parseInt(value.toString().trim());
					} catch (Exception e) {
						log.error(value + "  转换为 int 时异常,因此入库为 null ");
						i = null;
					}
				} else {
					i = null;
				}
				pstm.setInt(count, i);
				break;
			case LONG:
				Long l;
				if (value instanceof Long) {
					l = (Long) value;
				} else if (value instanceof String) {
					try {
						l = Long.parseLong(value.toString().trim());
					} catch (Exception e) {
						log.error(value + "  转换为 long 时异常,因此入库为 null ");
						l = null;
					}
				} else {
					l = null;
				}
				pstm.setLong(count, l);
				break;
			case FLOAT:
				Float f;
				if (value instanceof Float) {
					f = (Float) value;
				} else if (value instanceof String) {
					try {
						f = Float.parseFloat(value.toString().trim());
					} catch (Exception e) {
						log.error(value + "  转换为 float 时异常,因此入库为 null ");
						f = null;
					}
				} else {
					f = null;
				}
				pstm.setFloat(count, f);
				break;
			case DOUBLE:
			case NUMBER:
			case DECIMAL:
				Double d;
				if (value instanceof Double) {
					d = (Double) value;
				} else if (value instanceof String) {
					try {
						d = Double.parseDouble(value.toString().trim());
					} catch (Exception e) {
						log.error(value + "  转换为 double 时异常,因此入库为 null ");
						d = null;
					}
				} else {
					d = null;
				}
				pstm.setDouble(count, d);
				break;
			case BLOB:
				byte[] bytes;
				if (value instanceof byte[]) {
					bytes = (byte[]) value;
				} else if (value instanceof String) {
					bytes = value.toString().getBytes();
				} else {
					bytes = null;
				}
				pstm.setObject(count, bytes);
				break;
			case DATE:
				Date date = null;
				if (value instanceof Date) {
					date = (Date) value;
				} else if (value instanceof java.util.Date) {
					java.util.Date date1 = (java.util.Date) value;
					date = new Date(date1.getTime());
				} else if (value instanceof String) {
					String date2 = (String) value;
					if (date2.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						date = new Date(sdf.parse(date2).getTime());
					} else if (date2.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
						date = new Date(sdf2.parse(date2).getTime());
					} else if (date2.matches("\\d{4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						date = new Date(sdf3.parse(date2).getTime());
					} else if (date2.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
						date = new Date(sdf4.parse(date2).getTime());
					} else {
						date = null;
					}
				} else {
					date = null;
				}
				pstm.setDate(count, date);
				break;
			case TIMESTAMP:
				Timestamp timestamp;
				if (value instanceof Timestamp) {
					timestamp = (Timestamp) value;
				} else if (value instanceof java.util.Date) {
					java.util.Date date1 = (java.util.Date) value;
					timestamp = new Timestamp(date1.getTime());
				} else if (value instanceof String) {
					String date2 = (String) value;
					if (date2.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						timestamp = new Timestamp(sdf.parse(date2).getTime());
					} else if (date2.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
						timestamp = new Timestamp(sdf2.parse(date2).getTime());
					} else if (date2.matches("\\d{4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						timestamp = new Timestamp(sdf3.parse(date2).getTime());
					} else if (date2.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
						timestamp = new Timestamp(sdf4.parse(date2).getTime());
					} else {
						timestamp = null;
					}
				} else {
					timestamp = null;
				}
				pstm.setTimestamp(count, timestamp);
				break;
			case TIME:
				Time time;
				if (value instanceof Time) {
					time = (Time) value;
				} else if (value instanceof java.util.Date) {
					java.util.Date tmp = (java.util.Date) value;
					time = new Time(tmp.getTime());
				} else if (value instanceof String) {
					String tmp = (String) value;
					if (tmp.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
						time = new Time(time1.parse(tmp).getTime());
					} else if (tmp.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						time = new Time(sdf.parse(tmp).getTime());
					} else if (tmp.matches("\\d{4}/\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}.*")) {
						time = new Time(sdf3.parse(tmp).getTime());
					} else {
						time = null;
					}
				} else {
					time = null;
				}
				pstm.setTime(count, time);
				break;
			default:
				pstm.setObject(count, null);
		}
	}


	public static Class<? extends DataBase> sourceType(Connection conn) {
		try {
			String driver = conn.getMetaData().getDriverName();
			if (driver.matches("(?i).*oracle.*"))
				return Oracle.class;
			else if (driver.matches("(?i).*mysql.*"))
				return MySql.class;
			else if (driver.matches("(?i).*postgres.*"))
				return Postgres.class;
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	private static String decorate(Class<? extends DataBase> c) {
		if (c.equals(Oracle.class))
			return "\"";
		else if (c.equals(MySql.class))
			return "";
		else if (c.equals(Postgres.class))
			return "";
		else
			return "";
	}

	private static Map<String, Object> transMapToUpper(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<>();
		for (String s : map.keySet()) {
			retMap.put(s.toUpperCase(), map.get(s));
		}
		return retMap;
	}

	private static String buildInsertSQL(Map<String, String> columnModel, String table, String schema,Class<? extends DataBase> c) {
		String decorate = decorate(c);
		List<String> keyList = new ArrayList<>();
		for (Map.Entry<String, String> m : columnModel.entrySet())
			keyList.add(m.getKey());
		StringBuilder index = new StringBuilder("?");
		StringBuilder columns = new StringBuilder(Strings.around(keyList.get(0),decorate));
		for (int i = 1, j = keyList.size(); i < j; i++) {
			index.append(",?");
			columns.append("," + Strings.around(keyList.get(i),decorate) + "");
		}
		table = Strings.around(table,decorate);
		String tableSchema = schema == null ? table : Strings.around(schema,decorate)+ "." + table;
		String sql = "insert into " + tableSchema + " ("
				+ columns.toString() + ")" + " VALUES(" + index.toString()
				+ ")";
		return sql;
	}

	public static boolean batchInsert(List<Map<String, Object>> dataMap, Connection conn, String table) {
		return batchInsert(dataMap, conn, table, null);
	}

	private static void closeIgnoreException(AutoCloseable c) {
		if (c != null) {
			try {
				c.close();
				c = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd");

	private static final SimpleDateFormat time1 = new SimpleDateFormat(
			"HH:mm:ss");
}
