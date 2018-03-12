package com.asiainfo.ocmanager.service.client.v2;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Mongo client
 * 
 * @author EthanWang
 *
 */
public class MongoDBClient extends ServiceClient{
	private static final String HOST = "oc.mongo.server.host";
	private static final String PORT = "oc.mongo.server.port";
	private MongoClient mongo;

	public MongoDBClient(String serviceName, Delegator subject) {
		super(serviceName, subject);
		String host = this.serviceConfig.getProperty(HOST).trim();
		String port = this.serviceConfig.getProperty(PORT).trim();
		this.mongo = new MongoClient(host, Integer.valueOf(port));
	}
	
	public MongoDatabase getDB(String dbName) {
		return mongo.getDatabase(dbName);
	}

}
