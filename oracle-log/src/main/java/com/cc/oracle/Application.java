package com.cc.oracle;

/*
 * User: chenchong
 * Date: 2019/3/21
 * description:
 */

import com.cc.oracle.monitor.Monitor;
import com.cc.oracle.monitor.oracle.OracleMonitorManager;

import java.io.IOException;
import java.util.Properties;

public class Application {

	public static void main(String[] args) throws IOException {
		Properties p = new Properties();
		p.load(Application.class.getClassLoader().getResourceAsStream("db.properties"));
		Monitor monitor = new OracleMonitorManager.Builder()
				.config(p)
				.build();
		monitor.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			monitor.stop();
		}));
		try {
			monitor.awaitShutdown();
		} catch (InterruptedException e) {
			monitor.stop();
		}
	}
}
