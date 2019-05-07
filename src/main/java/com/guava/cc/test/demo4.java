package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class demo4 {

	public static void main(String[] args){

		String DATE_STRING = " ADD (89564 sasas) ADD (48595 sasas)";

		String P_UNCAP = "(?<=\\s)(ADD\\s\\([^\\)]+)";

		Pattern pattern = Pattern.compile(P_UNCAP);
		Matcher matcher = pattern.matcher(DATE_STRING);

		while(matcher.find()) {
			System.out.printf("\nmatcher.group(1) value:%s", matcher.group());
//			System.out.printf("\nmatcher.group(2) value:%s", matcher.group(1));
//			System.out.printf("\nmatcher.group(3) value:%s", matcher.group(2));
		}
	}
}
