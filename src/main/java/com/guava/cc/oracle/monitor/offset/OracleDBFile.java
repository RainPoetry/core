package com.guava.cc.oracle.monitor.offset;

/*
 * User: chenchong
 * Date: 2019/3/18
 * description:
 */

import com.rainpoetry.common.io.ReadWriteFile;

import java.io.File;
import java.util.Optional;

public class OracleDBFile extends ReadWriteFile<String>{

	public OracleDBFile(File f) {
		super(f);
	}

	@Override
	public String toLine(String str) {
		return str;
	}

	@Override
	public Optional<String> fromLine(String line) {
		return Optional.of(line);
	}
}
