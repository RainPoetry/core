package com.guava.cc.collect;

import org.apache.thrift.Option;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * User: chenchong
 * Date: 2019/2/27
 * description:
 */
public interface FileFormat<T> {

	String toLine(T t);

	Optional<T> fromLine(String line);
}

class OffsetFileFormat implements FileFormat<OffsetFileFormat.Record>{

	private static final String split = " ";
	private static final Pattern p = Pattern.compile("\\s+");

	@Override
	public String toLine(Record record) {
		return record.offset + split + record.host + split + record.pathNode;
	}

	@Override
	public Optional<Record> fromLine(String line) {
		String[] datas;
		if ((datas=p.split(line)).length == 3)
			return Optional.of(new Record(Integer.parseInt(datas[0]),datas[1],datas[2]));
		return Optional.empty();
	}

	public static class Record{
		int offset;
		String host;
		String pathNode;
		Record(int offset, String host, String pathNode) {
			this.offset = offset;
			this.host = host;
			this.pathNode = pathNode;
		}
	}

}


