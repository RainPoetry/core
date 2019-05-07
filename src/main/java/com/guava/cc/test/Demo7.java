package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Demo7 {

	public static void main(String[] args) throws IOException {
		String file = "C:\\Users\\10604\\Desktop\\19031008.000";
		FileChannel fileChannel = FileChannel.open(Paths.get(file));
		ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
		fileChannel.read(buffer);
		byte[] bytes = new byte[4];
//		buffer.flip();
		System.out.println(buffer.position() +" | " + buffer.limit() +" | " + buffer.capacity());
//
		buffer.get(bytes,0,4);
		System.out.println(new String(bytes,"gbk"));
		System.out.println(Arrays.toString(bytes));

//		char[] data = new char[]{'m','d','f','s'};
//		System.out.println(Arrays.toString(data.getBytes()));

		char a = 'm';
		System.out.println((byte)a);

	}
}
