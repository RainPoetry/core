package com.guava.cc.collect;

import com.guava.cc.source.CommonDirSourceConfig;
import com.guava.cc.utils.Utils;
import com.guava.cc.utils.schedule.CollectionScheduler;
import com.guava.cc.utils.schedule.Scheduler;
import com.google.common.eventbus.EventBus;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public abstract class AbstractCollector<T> implements Collector<T> {

	private EventBus bus = new EventBus("collector");
	protected Scheduler scheduler;
	private OffsetCheckpointFile offsetFile;
	private static final String CHECK_OFFSET_NAME = "offset-file";

	private final String machPatten;
	private String ignorePatten;

	// offset 映射缓存
	private ConcurrentHashMap<String, OffsetFileFormat.Record> offsetMap = new ConcurrentHashMap<>(64);

	protected final CommonDirSourceConfig config;

	protected boolean running = true;

	private boolean isUpdate = false;

	public AbstractCollector(CommonDirSourceConfig config) {
		this.config = config;
		this.scheduler = new CollectionScheduler(5, "daemon-thread");
		scheduler.startUp();
		this.machPatten = (String) config.get(CommonDirSourceConfig.INCLUDE_PATTERN);
		if (config.get(CommonDirSourceConfig.IGNORE_PATTERN) != null)
			this.ignorePatten = (String) config.get(CommonDirSourceConfig.IGNORE_PATTERN);
		String dir = (String) config.get(CommonDirSourceConfig.SPOOL_DIRECTORY);
		File f = new File(dir);
		offsetFile = new OffsetCheckpointFile(new File(f.getParent() + File.separator + CHECK_OFFSET_NAME),
				new OffsetFileFormat());
	}

	public void poll() {
		readOffset();
		// 定时更新 offset 文件
		scheduler.scheduler("offset-refresh" + Thread.currentThread().getId() + "-%d", offsetRefresh()
				, 0, 3, TimeUnit.SECONDS);
		start();
	}

	public Runnable offsetRefresh() {
		return () -> {
			if (isUpdate) {
				// execute update operate
				offsetFile.write(offsetMap.values());
			}

		};
	}

	// 从 txt 中读取 offset 到本地
	public void readOffset() {
		ConcurrentHashMap<String, OffsetFileFormat.Record> readOffset = new ConcurrentHashMap<>(64);
		List<OffsetFileFormat.Record> lines = offsetFile.readLine();
		for(OffsetFileFormat.Record record : lines) {
			readOffset.put(record.pathNode,record);
		}
		offsetMap = readOffset;
	}

	/**
	 *  描述：
	 *  	处理日志文件
	 *  1. 获取文件 的 offset
	 *
	 * @param path
	 */
	public void fileUpdate(String path) {
		OffsetFileFormat.Record record = offsetMap.get(path.hashCode()+"");
		FileLock lock = null;
		int startOffset = 0;
		if (record != null)
			startOffset = record.offset;
		try {
			RandomAccessFile out = new RandomAccessFile(path, "rw");
			FileChannel channel = out.getChannel();
			lock = channel.lock(startOffset, channel.size(), true);
			if (lock != null) {
				ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
				channel.read(buffer);
//				byte[] bytes = ByteStreams.toByteArray(new FileInputStream(path));
//				ByteBuffer buffer = ByteBuffer.wrap(bytes);
				buffer.position(startOffset);
				int limit = buffer.limit();
				if (limit > startOffset) {
					ByteBuffer newBuffer = buffer.slice();
					byte[] newByte = new byte[newBuffer.remaining()];
					newBuffer.get(newByte);
					bus.post(new String(newByte,"gbk"));
					updateOffset(path, limit);
				}
				lock.release();
			}
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 注册监听器
	public void register(CollectionListener o) {
		bus.register(new CommonListener(o));
	}

	public boolean match(String dir) {
		if (dir.matches(machPatten) && (ignorePatten==null || !dir.matches(ignorePatten)))
			return true;
		else
			return false;
	}

	// 更新 offset 缓存
	public void updateOffset(String path, int position) {
		try {
			OffsetFileFormat.Record record = new OffsetFileFormat.Record(position, InetAddress.getLocalHost().getHostAddress(),path.hashCode()+"");
			offsetMap.put(path.hashCode()+"",record);
//			System.out.println(offsetMap);
			isUpdate = true;
		} catch (UnknownHostException e) {
			System.out.println("hash error");
			e.printStackTrace();
		}
	}

	// 实现数据采集
	public abstract void start();

	public static class Builder {
		public static AbstractCollector build(CommonDirSourceConfig config) throws UnknownHostException {
			String host = config.getString(CommonDirSourceConfig.HOST_CONFIG);
			if (Utils.isLocal(host))
				return new LocalCollector(config);
			else
				return new RemoteCollector(config);
		}
	}

	@Override
	public void interrupt() {
		running = false;
	}

}
