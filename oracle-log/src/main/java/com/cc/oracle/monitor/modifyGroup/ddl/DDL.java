package com.cc.oracle.monitor.modifyGroup.ddl;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DDL {


	private static final Logger logger = LoggerFactory.getLogger(DDL.class);

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
					throw new DDLException("unrecognized Operate： " + operation);
			}
			ddl.parse(sql);
			return ddl;
		} else {
			if (sql.startsWith("CREATE TABLE")) {
				logger.info("it's a create sql, we ignore it ： " + sql);
				return null;
			}
			else
				throw new DDLException("Unresolved DDL statement："+sql);
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
		DDL ddl = DDL.of(sql2);
		System.out.println(ddl.toString());
	}

}
