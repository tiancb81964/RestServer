package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.MongoDBClient;
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

	public MongoResourcePeeker(String serviceName) {
		super(serviceName);
		if (serviceName.isEmpty()) return;
		try {
			ServiceClientInterface cli = ServiceClientPool.getInstance().getClient(serviceName);
			if (!(cli instanceof MongoDBClient)) {
				LOG.error("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
				throw new RuntimeException("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
			}
			client = (MongoDBClient)cli;
		} catch (Exception e) {
			LOG.error("Exception when init peeker: ", e);
			throw new RuntimeException("Exception when init peeker: ", e);
		}
	}
	
	@Override
	protected void setup() {
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
	public Class<? extends ServiceClient> getClientClass() {
		return MongoDBClient.class;
	}

}
