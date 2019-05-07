package com.guava.cc.main;

/*
 * User: chenchong
 * Date: 2019/3/11
 * description:
 */

import com.mongodb.client.MongoDatabase;

import java.util.HashMap;
import java.util.Map;

public class MongoDB {

	private final MongoDatabase db;
	private Map<String,MongoCollect> cache = null;

	public MongoDB(MongoDatabase db) {
		this.db = db;
		this.cache = new HashMap<>();
	}

	public MongoCollect collection(String name) {
		return cache.getOrDefault(name, new MongoCollect(db.getCollection(name)));
	}
}
