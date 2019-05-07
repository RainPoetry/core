package com.cc.oracle.monitor.oracle;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:	Oracle 日志监控
 */

import com.cc.oracle.monitor.Monitor;
import com.rainpoetry.common.db.JdbcManager;
import com.rainpoetry.common.scheduler.Scheduler;
import com.rainpoetry.common.scheduler.SchedulerPool;
import com.rainpoetry.common.utils.EventLoop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OracleMonitorManager implements Monitor {

	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private static final int thread_nums = 3;
	private static final String offsetFile = "oracle-ddl-offset";

	private final Connection connection;
	private final Connection target;

	private final AtomicBoolean isStart = new AtomicBoolean(false);

	private final Scheduler scheduler;
	private final EventLoop loop;

	private final OracleManagerConfig config;

	public OracleMonitorManager(OracleManagerConfig config) {
		this.config = config;
		this.scheduler = new SchedulerPool(thread_nums);
		String userName = config.getString(OracleManagerConfig.SOURCE_DATABASE_USERNAME);
		String password = config.getString(OracleManagerConfig.SOURCE_DATABASE_PASSWORD);
		String url = config.getString(OracleManagerConfig.SOURCE_DATABASE_URL);
		String driver = config.getString(OracleManagerConfig.DATABASE_DRIVER);
		connection = JdbcManager.connect(userName, password, url, driver);

		String userName2 = config.getString(OracleManagerConfig.Target_DATABASE_USERNAME);
		String password2 = config.getString(OracleManagerConfig.Target_DATABASE_PASSWORD);
		String url2 = config.getString(OracleManagerConfig.Target_DATABASE_URL);
		String driver2 = config.getString(OracleManagerConfig.Target_DATABASE_DRIVER);
		target = JdbcManager.connect(userName2, password2, url2, driver2);

		this.loop = new OracleMonitorProcessLoop("oracle-deal-purgatory", connection, target);
	}

	public void schedule() {
		scheduler.scheduler("oracleDDL",
				new OracleDDLThread(offsetFile, loop, connection, config.toMap()),
				0, 1, TimeUnit.HOURS);
	}

	@Override
	public boolean isStart() {
		return isStart.get();
	}

	@Override
	public void start() {
		if (!isStart.get()) {
			synchronized (this) {
				if (!isStart.get()) {
					loop.start();
					scheduler.startUp();
					schedule();
					isStart.compareAndSet(false, true);
				}
			}
		}
	}

	@Override
	public void stop() {
		isStart.compareAndSet(true, false);
		synchronized (this) {
			scheduler.shutdown();
			this.loop.stop();
			closeConnect(connection);
			closeConnect(target);
			shutdownLatch.countDown();
		}
	}

	@Override
	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

	private void closeConnect(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Builder {
		private Map<String, Object> map;
		private Connection conn;

		public Builder() {
			this.map = new HashMap<>();
		}

		public Builder scn(String scn) {
			map.put(OracleManagerConfig.LAST_SCN, scn);
			return this;
		}

//		public Builder connect(Connection conn) {
//			this.conn = conn;
//			return this;
//		}

		public Builder connUserName(String userName) {
			map.put(OracleManagerConfig.SOURCE_DATABASE_USERNAME, userName);
			return this;
		}

		public Builder connPassword(String password) {
			map.put(OracleManagerConfig.SOURCE_DATABASE_PASSWORD, password);
			return this;
		}

		public Builder connUrl(String url) {
			map.put(OracleManagerConfig.SOURCE_DATABASE_URL, url);
			return this;
		}

		public Builder targetUserName(String userName) {
			map.put(OracleManagerConfig.Target_DATABASE_USERNAME, userName);
			return this;
		}

		public Builder targetPassword(String password) {
			map.put(OracleManagerConfig.Target_DATABASE_PASSWORD, password);
			return this;
		}

		public Builder targetUrl(String url) {
			map.put(OracleManagerConfig.Target_DATABASE_URL, url);
			return this;
		}

		public Builder targetDriver(String driver) {
			map.put(OracleManagerConfig.Target_DATABASE_DRIVER, driver);
			return this;
		}

		public Builder client(String clients) {
			map.put(OracleManagerConfig.SOURCE_CLIENT_USERNAME, clients);
			return this;
		}

		public Builder logPath(String path) {
			map.put(OracleManagerConfig.LOG_PATH, path);
			return this;
		}

		public Builder dataPath(String path) {
			map.put(OracleManagerConfig.DATA_DICTIONARY, path);
			return this;
		}

		public Builder dataFile(String fileName) {
			map.put(OracleManagerConfig.DATA_DICTIONARY_FILENAME, fileName);
			return this;
		}

		public Builder config(Map<?, ?> config) {
			map.putAll((Map<String, Object>) config);
			return this;
		}

		public OracleMonitorManager build() {
			return new OracleMonitorManager(new OracleManagerConfig(map));
		}
	}
}
