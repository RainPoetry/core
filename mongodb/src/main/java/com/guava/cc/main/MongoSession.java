package com.guava.cc.main;

/*
 * User: chenchong
 * Date: 2019/3/11
 * description:
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoSession {

	private final MongoClient client;
	private Map<String,MongoDB> cache;

	private MongoSession(MongoClient client) {
		this.client = client;
		cache = new HashMap<>();
	}

	public static class Builder{
		private String address;
		private int port = 27017;
		private String userName;
		private String password;
		private String db;
		public Builder address(String address) {
			this.address = address;
			return this;
		}
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		 public Builder db(String db) {
			this.db = db;
			return this;
		 }
		public MongoSession build() {
			MongoClient client = null;
			if (userName == null || password == null)
				client = new MongoClient(address,port);
			else {
				ServerAddress server = new ServerAddress(address,port);
				List<ServerAddress> servers = new ArrayList<ServerAddress>();
				servers.add(server);
				//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
				MongoCredential credential = MongoCredential.createScramSha1Credential(userName, db, password.toCharArray());
				List<MongoCredential> credentials = new ArrayList<MongoCredential>();
				credentials.add(credential);
				//通过连接认证获取MongoDB连接
				client = new MongoClient(servers,credentials);
			}
			return new MongoSession(client);
		}
	}

	public MongoDB db(String name) {
		MongoDatabase db = client.getDatabase(name);
		return cache.getOrDefault(name,new MongoDB(db));
	}

	public void close() {
		if (client != null)
			client.close();
		cache = null;
	}

}
