package com.guava.cc.test;

/*
 * User: chenchong
 * Date: 2019/3/11
 * description:
 */

import com.guava.cc.bean.Person;
import com.guava.cc.main.MongoSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Deal {

	public static void main(String[] args){
		MongoSession session = new MongoSession.Builder()
				.address("172.18.1.2")
				.build();

		Map<String,Object> map = new HashMap<>();
		map.put("name","JouHan");
		map.put("password","123456");
		map.put("age",15);
		map.put("date",new Date());

//		session.db("a1")
//				.collection("c1")
//				.insert(map);

		Pattern p = Pattern.compile("^.*e$");

		session
				.db("a1")
				.collection("c1")
				.agg("name","age")
				.forEach(it-> System.out.println(it));
	}
}
