package com.guava.cc.main;

/*
 * User: chenchong
 * Date: 2019/3/11
 * description:
 */

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MongoCollect {

	private final MongoCollection<Document> collection;
	private BasicDBList list;

	public MongoCollect(MongoCollection collection) {
		this.collection = collection;
	}

	public void insert(List<Map<String,Object>> list) {
		List<Document> store = new ArrayList<>(list.size());
		for(Map<String, Object> map : list)
			store.add(new Document(map));
		collection.insertMany(store);
	}

	public void insert(Map<String,Object> o) {
		Document d = new Document(o);
		collection.insertOne(d);
	}

	public long size() {
		return collection.count();
	}

	// 模糊匹配
	public List<String> regex(String col, Pattern p) 	{
		Document match = new Document();
		match.put(col,p);
		return query(match);
	}

	private List<String> query(Bson b) {
		List<String> list = new ArrayList<>();
		for(Document t : collection.find(b))
			list.add(t.toJson());
		return list;
	}

	public List<String> query() {
		List<String> list = new ArrayList<>();
		for(Document t : collection.find())
			list.add(t.toJson());
		return list;
	}

	private Bson whereEq(String name,Object value) {
		// in、gt、it、nor
		return Aggregates.match(Filters.eq(name,value));
	}

	private Bson groupBy(String groupField, String newName) {
		return Aggregates.group("$"+groupField, Accumulators.sum(newName,1));
	}

	public List<String> agg(String groupField, String newName) {
		List<Bson> list = new ArrayList<>();
		list.add(groupBy(groupField,newName));
		List<String> result = new ArrayList<>();
		for(Document d : collection.aggregate(list))
			result.add(d.toJson());
		return result;
	}

	public static class Aggregate{

	}

}
