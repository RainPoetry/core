package com.cc.oracle.monitor.modifyGroup.ddl;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DDL {

	public final String tableName;

	public DDL(String tableName) {
		this.tableName = tableName;
	}

	private static final String CORE_REGEX = "ALTER TABLE \"\\w+\"\\.\"([^\"]+)\"\\s+(\\w+).+";

	public static DDL of(String sql) {
		Pattern p = Pattern.compile(CORE_REGEX);
		Matcher m = p.matcher(sql);
		if (m.find()) {
			String tableName = m.group(1);
			String operation = m.group(2);
			DDL ddl;
			switch (operation) {
				case "MODIFY":
				case "ADD":
					 ddl = new AddOrModify(tableName);
					 break;
				case "RENAME":
					 ddl = new Rename(tableName);
					 break;
				case "DROP":
					 ddl = new Drop(tableName);
					 break;
				default:
					throw new DDLException("无法识别的操作： " + operation);
			}
			ddl.parse(sql);
			return ddl;
		} else {
			throw new DDLException("无法解析的 DDL 语句："+sql);
		}
	}

	abstract void parse(String sql);

	public static void main(String[] args){
		String sql1 = "ALTER TABLE \"PCCGZ\".\"11111\" \n" +
				"MODIFY (\"AAAA\" VARCHAR(255 BYTE) )\n" +
				"ADD (\"2323232qsasas\" VARCHAR2(255) )\n" +
				"ADD (\"sasas\" VARCHAR2(255) )\n" +
				"ADD (\"sasasasasa\" VARCHAR2(255) );";
		String sql2  ="ALTER TABLE \"PCCGZ\".\"11111\" RENAME COLUMN \"AAAA\" TO \"AAAA222222\";";
		String sql3 = "ALTER TABLE \"PCCGZ\".\"11111\" DROP (\"CCCC\", \"DDDDD\", \"EEEEEE\");";
		String sql4 = "ALTER TABLE \"PCCGZ\".\"11111\" \n" +
				"ADD (\"date2\" DATE )\n" +
				"ADD (\"date3\" DATE );";
		DDL ddl = DDL.of(sql4);
		System.out.println(ddl.toString());
	}

}
