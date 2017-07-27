package com.asiainfo.ocmanager.service.client;

import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Mongo client
 * 
 * @author EthanWang
 *
 */
public class MongoDBClient {
	private static MongoDBClient instance;
	private static final String HOST = "oc.mongo.server.host";
	private static final String PORT = "oc.mongo.server.port";
	private MongoClient mongo;

	public static MongoDBClient getInstance() {
		if (instance == null) {
			synchronized (MongoDBClient.class) {
				if (instance == null) {
					instance = new MongoDBClient();
				}
			}
		}
		return instance;
	}

	public MongoDatabase getDB(String dbName) {
		return mongo.getDatabase(dbName);
	}

	private MongoDBClient() {
		String host = ServerConfiguration.getConf().getProperty(HOST).trim();
		String port = ServerConfiguration.getConf().getProperty(PORT).trim();
		this.mongo = new MongoClient(host, Integer.valueOf(port));
	}
}
