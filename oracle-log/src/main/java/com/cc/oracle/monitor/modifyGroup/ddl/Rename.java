package com.cc.oracle.monitor.modifyGroup.ddl;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rename extends DDL {

	private static final String RENAME_REGEX = "(?<=RENAME COLUMN)\\s+\"(\\w+)\"\\s+TO\\s+\"(\\w+)\"";

	public String oldName;
	public String newName;

	public Rename(String tableName) {
		super(tableName);
	}

	@Override
	public void parse(String sql) {
		Pattern p = Pattern.compile(RENAME_REGEX);
		Matcher m = p.matcher(sql);
		if (m.find()) {
			oldName = m.group(1);
			newName = m.group(2);
		}
	}

	@Override
	public String toString() {
		return "oldName: " + oldName + " , newName=" + newName;
	}
}
