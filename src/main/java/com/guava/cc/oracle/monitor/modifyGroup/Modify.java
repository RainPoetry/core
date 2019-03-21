package com.guava.cc.oracle.monitor.modifyGroup;

/*
 * User: chenchong
 * Date: 2019/3/15
 * description:
 */

import com.google.common.collect.Multimap;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.AddOrModify;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.DDL;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.Drop;
import com.guava.cc.oracle.monitor.modifyGroup.ddl.Rename;
import com.rainpoetry.common.utils.Logging;

import java.sql.Connection;
import java.util.List;

public abstract class Modify  extends DDLOperate{

	public Modify(Connection connection) {
		super(connection);
	}

	protected void commonDeal(String sql) {
		DDL ddl = DDL.of(sql);
		if (ddl instanceof AddOrModify) {
			AddOrModify modify = (AddOrModify) ddl;
			add(modify.tableName, modify.maps);
		} else if (ddl instanceof Drop) {
			Drop drop = (Drop) ddl;
			drop(drop.tableName, drop.cols);
		} else if (ddl instanceof Rename) {
			Rename rename = (Rename) ddl;
			reName(rename.tableName, rename.oldName, rename.newName);
		}
	}

	public String ident(){
		return getClass().getName();
	}

}
