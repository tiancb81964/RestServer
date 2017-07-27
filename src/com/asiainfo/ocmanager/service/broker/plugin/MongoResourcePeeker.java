package com.asiainfo.ocmanager.service.broker.plugin;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.MongoDBClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Mongo resource monitor.
 * 
 * @author EthanWang
 *
 */
public class MongoResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(MongoResourcePeeker.class);
	private MongoDBClient client;
	private static final String RESOURCE_TYPE = "volumeSize";

	@Override
	protected void setup() {
		client = MongoDBClient.getInstance();
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String dbName) {
		return -1l;// no max volumeSize for mongoDB
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String dbName) {
		if (!resourceType.equals(RESOURCE_TYPE)) {
			LOG.error("ResourceType not defined: " + resourceType);
			throw new RuntimeException("ResourceType not defined: " + resourceType);
		}
		MongoDatabase db = client.getDB(dbName);
		MongoCursor<Document> it = db.listCollections().iterator();
		long size = 0;
		while (it.hasNext()) {
			Document doc = it.next();
			size = size + doc.toJson().getBytes().length;
		}
		return size;
	}

}
