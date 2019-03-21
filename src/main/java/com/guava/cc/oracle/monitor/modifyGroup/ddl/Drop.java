package com.guava.cc.oracle.monitor.modifyGroup.ddl;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Drop extends DDL{

	private static final String DROP_REGEX = "(?<=[\\(,])\\s*([^\\,)]+)";

	public List<String> cols;

	public Drop(String tableName) {
		super(tableName);
	}

	@Override
	public void parse(String sql) {
		this.cols = new ArrayList<>();
		Pattern p = Pattern.compile(DROP_REGEX);
		Matcher m = p.matcher(sql);
		while(m.find()) {
			cols.add(m.group().replaceAll("\"",""));
		}
	}

	@Override
	public String toString() {
		return cols.toString();
	}
}
