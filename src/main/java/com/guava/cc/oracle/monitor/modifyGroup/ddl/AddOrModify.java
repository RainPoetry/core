package com.guava.cc.oracle.monitor.modifyGroup.ddl;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddOrModify extends DDL {

	private static final String ADD_MODIFY_REGEX = "(?<=\\s)((ADD|MODIFY)\\s)\\(([^\\)]+)\\)";

	public Multimap<String, Column> maps;

	public AddOrModify(String tableName) {
		super(tableName);
	}

	@Override
	public void parse(String sql) {
		maps = LinkedListMultimap.create();
		Pattern p = Pattern.compile(ADD_MODIFY_REGEX);
		Matcher m = p.matcher(sql);
		while (m.find()) {
			String operate = m.group(2);
			String data = m.group(3).replaceAll("\"", "");
			String name,type,size;
			if(data.contains("(")) {
				String former = data.split("\\(")[0];
				String later = data.split("\\(")[1].split("\\s+")[0];
				name = former.split("\\s+")[0];
				type = former.split("\\s+")[1];
				size = later;
			} else {
				name = data.split("\\s+")[0];
				type = data.split("\\s+")[1];
				size = "0";
			}
			maps.put(operate,new Column(name,type,size));
		}
	}

	@Override
	public String toString() {
		return maps.toString();
	}

	public class Column {
		public String name;
		public String type;
		public String size;

		public Column(String name, String type, String size) {
			this.name = name;
			this.type = type;
			this.size = size;
		}

		@Override
		public String toString() {
			return "{" +
					"name=" + name +
					", type=" + type +
					", size=" + size +
					'}';
		}
	}
}
