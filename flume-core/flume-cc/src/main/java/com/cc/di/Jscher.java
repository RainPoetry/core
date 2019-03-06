package com.cc.di;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public class Jscher extends AbstractNotifier{

	private ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>(16);

	@Override
	public void unRegister(String path) {
		synchronized (this) {
			int watchId = map.get(path);
			try {
				JNotify.removeWatch(watchId);
				map.remove(path);
				super.unRegister(path);
			} catch (JNotifyException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		for(Map.Entry<String,Collection<WatchListener>> entry : cacheMap.entrySet()) {
			int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
			// 是否监视子目录
			boolean watchSubtree = true;
			try {
				int watchID = JNotify.addWatch(entry.getKey(), mask, watchSubtree, new Listener(entry.getValue()));
				map.put(entry.getKey(),watchID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String ident() {
		return "Jnotifier";
	}

	/**
	 *  对子目录有效
	 */
	private  class Listener implements JNotifyListener {

		private Collection<WatchListener> watchList;

		Listener(Collection<WatchListener> watchList) {
			this.watchList = watchList;
		}

		public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
			logger.info("rename : " + oldName +"->"+newName);
			String path = rootPath + File.separator + newName;
			String oldPath = rootPath + File.separator + oldName;
			for(WatchListener listener : watchList)
				listener.reName(oldPath,path);
		}

		public void fileModified(int wd, String rootPath, String name) {
			logger.info("modify : " + name);
			String path = rootPath + File.separator + name;
			for(WatchListener listener : watchList)
				listener.modify(path);
		}

		public void fileDeleted(int wd, String rootPath, String name) {
			logger.info("delete : " + name);
			String path = rootPath + File.separator + name;
			for(WatchListener listener : watchList)
				listener.delete(path);
		}

		public void fileCreated(int wd, String rootPath, String name) {
			logger.info("create : " + name);
			String path = rootPath + File.separator + name;
			for(WatchListener listener : watchList)
				listener.create(path);
		}

	}

}
