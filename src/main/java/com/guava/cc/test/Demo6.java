package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo6 {

	public static final String P_UNCAP = "(?<=\\s)((ADD|MODIFY)\\s)\\(([^\\)]+\\))";
	public static  String DATE_STRING = "ALTER TABLE \"PCCGZ\".\"11111\" \n" +
			"MODIFY (\"AAAA\" VARCHAR(255 BYTE) )\n" +
			"ADD (\"2323232qsasas\" VARCHAR2(255) )\n" +
			"ADD (\"sasas\" VARCHAR2(255) )\n" +
			"ADD (\"sasasasasa\" VARCHAR2(255) );";


	public static void main(String[] args){
		DATE_STRING = DATE_STRING.replaceAll("\\n"," ");
//		System.out.println(DATE_STRING);
		Pattern pattern = Pattern.compile(P_UNCAP);
		Matcher matcher = pattern.matcher(DATE_STRING);
//		System.out.println(DATE_STRING);
//		System.out.println(DATE_STRING.matches(P_UNCAP));
//		matcher.find();
//		System.out.printf("\nmatcher.group(0) value:%s", matcher.group(0));
//		System.out.printf("\nmatcher.group(1) value:%s", matcher.group(1));
		while(matcher.find()) {
			System.out.printf("\nmatcher.group(1) value:%s", matcher.group(1));
			System.out.printf("\nmatcher.group(2) value:%s", matcher.group(2));
			System.out.printf("\nmatcher.group(3) value:%s", matcher.group(3));
//			System.out.printf("\nmatcher.group(4) value:%s", matcher.group(4));
		}

	}
}
