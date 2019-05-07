package com.guava.cc.collect;

import com.guava.cc.di.Jscher;
import com.guava.cc.di.Notifier;
import com.guava.cc.di.WatchListener;
import com.guava.cc.source.CommonDirSourceConfig;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.File;
import java.util.concurrent.*;

/**
 * User: chenchong
 * Date: 2019/2/26
 * description:
 */
public class LocalCollector extends AbstractCollector {

	private static final int notify_interval = 200;

	private static final int poll_interval = 2000;

	// 所有监控文件的集合， 用于 轮询操作
	private CopyOnWriteArrayList<String> monitorFiles;

	// 缓存 所有的 notify 信息，
	private ConcurrentSkipListSet<String> cacheList;

	// 处于处理中的队列
	private ConcurrentSkipListSet<String> offLineList = new ConcurrentSkipListSet<>();

	Notifier notifier;

	private ExecutorService executors = Executors.newCachedThreadPool(
			new ThreadFactoryBuilder().setNameFormat("notify-event").build());

	public LocalCollector(CommonDirSourceConfig config) {
		super(config);
		this.monitorFiles = new CopyOnWriteArrayList();
		this.notifier = new Jscher();
	}


	@Override
	public void start() {
		String dir = (String) config.get(CommonDirSourceConfig.SPOOL_DIRECTORY);
		rootTraversing(dir);
		// 使用 Inotify 机制
		if (notifier != null)
			useInotify(dir);
		scheduler.scheduler("notify-event" + Thread.currentThread().getId() + "-%d", ()->schedulerNotifyEvent()
				, 0, -1, TimeUnit.SECONDS);
		scheduler.scheduler("poll-event" + Thread.currentThread().getId() + "-%d", ()->pollDir()
				, 0, -1, TimeUnit.SECONDS);
	}

	// 扫描根节点，注册所有的文件
	public void rootTraversing(String dir) {
		File root = new File(dir);
		if (root.isDirectory()) {
			File[] files = root.listFiles();
			for(File f : files) {
				rootTraversing(f.getAbsolutePath());
			}
		} else {
			if (match(dir))
				monitorFiles.add(dir);
		}
	}

	@Override
	public void close() {
		interrupt();
		if (notifier != null)
			notifier.unRegister((String)config.get( CommonDirSourceConfig.SPOOL_DIRECTORY));
		try {
			scheduler.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void pollDir() {
		while (running) {
			long now = System.currentTimeMillis();
			for (String s : monitorFiles)
				executors.submit(() -> fileUpdate(s));
			long waitTime = System.currentTimeMillis() - now;
			try {
				if (waitTime < poll_interval)
					Thread.sleep(poll_interval - waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//
	public void schedulerNotifyEvent() {
		while (running) {
			long now = System.currentTimeMillis();
			ConcurrentSkipListSet clone = cacheList.clone();
			offLineList.addAll(clone);
			cacheList.removeAll(clone);
			for (String s : offLineList)
				executors.submit(() -> fileUpdate(s));
			long waitTime = System.currentTimeMillis() - now;
			try {
				if (waitTime < notify_interval)
					Thread.sleep(notify_interval - waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	// 启动 Inotify
	public void useInotify(String dir) {
		notifier.register(dir, new DefaultListener());
		scheduler.scheduler("notify", () -> notifier.run(), 0, -1L, TimeUnit.MILLISECONDS);
	}

	private class DefaultListener implements WatchListener {
		@Override
		public void create(String path) {
			if (match(path))
				monitorFiles.add(path);
		}

		@Override
		public void delete(String path) {
			monitorFiles.remove(path);
		}

		@Override
		public void modify(String path) {
			cacheList.add(path);
		}

		@Override
		public void reName(String oldPath, String newPath) {
			monitorFiles.remove(oldPath);
			if (match(newPath))
				monitorFiles.add(newPath);
		}

		@Override
		public void overflow(String path) {

		}
	}


}
