package com.guava.cc.di;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public class DirectoryWatcher {


	private Path path;

	public DirectoryWatcher(Path path) {
		this.path = path;
	}

	// print the events and the affected file
	private void printEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
		if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
			Path pathCreated = (Path) event.context();
			System.out.println("Entry created:" + pathCreated);
		} else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
			Path pathDeleted = (Path) event.context();
			System.out.println("Entry deleted:" + pathDeleted);
		} else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
			Path pathModified = (Path) event.context();
			System.out.println("Entry modified:" + pathModified);
		} //丢失或放弃事件时被触发
		else if (kind == StandardWatchEventKinds.OVERFLOW) {
			System.out.println("Entry OVERFLOW:" + event.toString());
		}
	}

	public void run() {
		try {
			WatchService watchService = path.getFileSystem().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

			// loop forever to watch directory
			while (true) {
				WatchKey watchKey;
				watchKey = watchService.take(); // this call is blocking until events are present

				// poll for file system events on the WatchKey
				for (final WatchEvent<?> event : watchKey.pollEvents()) {
					printEvent(event);
				}

				// if the watched directed gets deleted, get out of run method
				if (!watchKey.reset()) {
					System.out.println("No longer valid");
					watchKey.cancel();
					watchService.close();
					break;
				}
			}

		} catch (InterruptedException ex) {
			System.out.println("interrupted. Goodbye");
			return;
		} catch (IOException ex) {
			ex.printStackTrace();  // don't do this in production code. Use a loggin framework
			return;
		}
	}
}

