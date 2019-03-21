package com.rainpoetry.common.io;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.google.common.io.ByteStreams;
import com.rainpoetry.common.utils.Functions;
import com.rainpoetry.common.utils.Utils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ReadWriteFile<T> {

	private final Path path;
	private final Path tempPath;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


	public ReadWriteFile(File f) {
		try {
			if (!f.exists() || f.isDirectory()) {
				f.getParentFile().mkdirs();
				f.delete();
				f.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.path = Paths.get(f.getAbsolutePath());
		this.tempPath = Paths.get(f.getAbsolutePath() + ".tmp");
	}

	public void write(ByteBuffer buffer) {

	}

	// 参考： https://blog.csdn.net/dreamsky1989/article/details/7456875
	// http://imushan.com/2018/06/18/java/language/Java%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E6%96%87%E4%BB%B6%E8%90%BD%E7%9B%98%EF%BC%9F/

	/**
	 * 数据强行写入磁盘：
	 * fsync: // 强制文件数据与元数据落盘
	 * 1. outputStream.getFD().sync();
	 * 2. outputStream.getChannel().force(true);
	 * <p>
	 * fdatasync:
	 * // 强制文件数据落盘，不关心元数据是否落盘
	 * 1. 	outputStream.getChannel().force(false);
	 *
	 * @param list
	 */
	public void write(Collection<T> list) {
		Functions.inWriteLock(lock, () -> {
			try {
				FileOutputStream fs = new FileOutputStream(new File(tempPath.toString()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fs));
				try {
					for (T s : list) {
						bw.write(toLine(s));
						bw.newLine();
					}
					// 刷新数据并将数据转交给操作系统
					bw.flush();
					// 强制系统缓冲区与基础设备同步
					// 将系统缓冲区数据写入到文件
					// fsync
					fs.getChannel().force(true);
				} finally {
					bw.close();
				}
				Utils.atomicMoveWithFallback(tempPath, path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Void.class;
		});
	}

	public ByteBuffer read() {
		return Functions.inReadLock(lock, () -> {
			FileChannel fileChannel = null;
			try {
				fileChannel = FileChannel.open(path);
				ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
				fileChannel.read(buffer);
				return buffer;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				if (fileChannel!=null) {
					try {
						fileChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public List<T> readLine() {
		return Functions.inReadLock(lock, () -> {
			try {
				BufferedReader reader = Files.newBufferedReader(path);
				String line = null;
				List<T> lines = new ArrayList<>();
				while ((line = reader.readLine()) != null)
					lines.add(fromLine(line).orElseThrow(IOException::new));
				reader.close();
				return lines;
			} catch (IOException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		});
	}

	private Exception malformedLineException(String line) {
		return new IOException(String.format("Malformed line in checkpoint file (%s): %s",
				path.toString(), line));
	}

	protected abstract String toLine(T t);

	protected abstract Optional<T> fromLine(String line);
}
