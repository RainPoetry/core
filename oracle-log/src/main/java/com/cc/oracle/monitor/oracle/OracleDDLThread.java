package com.cc.oracle.monitor.oracle;

/*
 * User: chenchong
 * Date: 2019/3/14
 * description:
 */

import com.cc.oracle.Application;
import com.cc.oracle.monitor.logminer.LogMiner;
import com.cc.oracle.monitor.offset.OracleDBFile;

import com.rainpoetry.common.io.ReadWriteFile;
import com.rainpoetry.common.utils.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class OracleDDLThread implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(OracleDDLThread.class);

	private String offset;
	private final ReadWriteFile<String> offsetFile;
	private final EventLoop loop;
	private final Connection connection;
	private LogMiner logMiner;


	public OracleDDLThread(String offsetFile, EventLoop loop, Connection connection,Map<String,Object> maps) {
		String path = Application.class.getClassLoader().getResource(offsetFile).getFile();
		File f;
		if (path.indexOf("!/")!=-1) {
			String parent = new File(path.substring(6, path.indexOf("!/"))).getParent();
			f = new File(parent + File.separator + offsetFile);
		} else {
			f = new File(path);
		}
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		this.offsetFile = new OracleDBFile(f);
		this.loop = loop;
		this.connection = connection;
		this.logMiner = new LogMiner.Builder()
				.config(maps)
				.connect(connection)
				.build();
	}

	@Override
	public void run() {
		logger.info("start Oracle log collection");
		ByteBuffer buffer = offsetFile.read();
		if (buffer.capacity() > 0 )
			offset = new String(buffer.array());
		List<Map<String,String>> list = logMiner.analysis(offset);
		if (offset !=null && !offset.trim().equals("0"))
			list.forEach(loop::post);
		offsetFile.write(Arrays.asList(logMiner.maxScn+""));
		logger.info("update scn = " + logMiner.maxScn);
	}
}
