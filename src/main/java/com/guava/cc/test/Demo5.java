package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/19
 * description:
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo5 {

	public static void main(String[] args){

		String content = "src: local('Open Sans Light'), local('OpenSans-Light'), url(http://fonts.gstatic.com/s/opensans/v13/DXI1ORHCpsQm3Vp6mXoaTa-j2U0lmluP9RWlSytm3ho.woff2) format('woff2')";
		// 匹配 ( 出现的后面的内容
		// 不以 ) 结尾
		Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}
}
