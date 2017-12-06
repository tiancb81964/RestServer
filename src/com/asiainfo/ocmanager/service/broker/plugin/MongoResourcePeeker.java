package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;
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
		if (!resourceType.equals("volumeSize")) {
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

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("volumeSize");
	}

	@Override
	public ServiceType getType() {
		return ServiceType.mongo;
	}

}
