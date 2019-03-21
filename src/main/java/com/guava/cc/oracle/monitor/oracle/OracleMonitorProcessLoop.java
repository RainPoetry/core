package com.guava.cc.oracle.monitor.oracle;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import com.guava.cc.oracle.monitor.modifyGroup.*;

import com.guava.cc.oracle.utils.db.JdbcManager;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.rainpoetry.common.utils.EventLoop;

import java.sql.Connection;
import java.util.Map;

public class OracleMonitorProcessLoop extends EventLoop<Map<String,String>> {

	//	private final Filter<String> filter;
	private final Connection conn;
	private final EventBus bus;

	private final ImmutableList<Modify> modifyList;

	public OracleMonitorProcessLoop(Connection conn, Connection target) {
		this("default", conn, target);
	}

	public OracleMonitorProcessLoop(String name, Connection conn,Connection target) {
		super(name);
		this.conn = conn;
		this.bus = new EventBus();
		this.modifyList = ImmutableList.of(new GPMetaModify(target),
//				new LocalMetaModify(target),
				new MappingReferenceModify(target));
	}


	@Override
	protected void onStart() {
		for (Modify m : modifyList)
			bus.register(m);
	}

	@Override
	protected void onStop() {
		for (Modify m : modifyList)
			bus.unregister(m);
	}

	@Override
	protected void onReceive(Map<String,String> map) {
		String sql = map.get("sql");
		String operation = map.get("operation");
		if (operation.equalsIgnoreCase("DDL"))
			bus.post(new MetaCache(sql, operation));
	}

	@Override
	protected void onError(Throwable t) {

	}
}
