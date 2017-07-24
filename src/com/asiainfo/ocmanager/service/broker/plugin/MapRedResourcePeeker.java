package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.YarnClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Resource peeker for Mapreduce. 
 * @author EthanWang
 *
 */
public class MapRedResourcePeeker extends BaseResourcePeeker{
	private static final Logger LOG = Logger.getLogger(MapRedResourcePeeker.class);
	private static final String TYPE = "yarnQueueQuota";
	private YarnClient client;
	private String resourceInfo;
	
	@Override
	protected void setup() {
		this.client = YarnClient.getInstance();
		try {
			this.resourceInfo = client.fetchQueueInfo();
		} catch (Exception e) {
			LOG.error("Error while fetching queue info from RM: ", e);
			throw new RuntimeException("Error while fetching queue info from RM: ", e);
		}
	}
	
	@Override
	protected Long fetchTotalQuota(String resourceType, String queueName) {
		if (!resourceType.equals(TYPE)) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		JsonObject jsonObj = new JsonParser().parse(this.resourceInfo).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		//TODO: total memory can not be retrieved from rm.
		queue.getAsJsonObject("");
		return -999l;
	}

	/**
	 * Get specified queue by the given name.
	 * @param json
	 * @param queueName
	 * @return
	 */
	private JsonObject findQueueByName(JsonObject json, String queueName) {
		JsonArray queues = json.getAsJsonObject("scheduler").getAsJsonObject("schedulerInfo").getAsJsonObject("queues").getAsJsonArray("queue");
		Iterator<JsonElement> it = queues.iterator();
		String name = "";
		while (it.hasNext()) {
			JsonObject queue = it.next().getAsJsonObject();
			name = queue.getAsJsonPrimitive("queueName").getAsString();
			if (name.equals(queueName)) {
				return queue;
			}
		}
		LOG.error("Queue not exist: " + queueName);
		throw new RuntimeException("Queue not exist: " + queueName);
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String queueName) {
		if (!resourceType.equals(TYPE)) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		JsonObject jsonObj = new JsonParser().parse(this.resourceInfo).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		long used = queue.getAsJsonObject("resourcesUsed").getAsJsonPrimitive("memory").getAsLong();
		//TODO: if need to change to "usedCapacity"(percentage) instead of "resourcesUsed/memory"
		// if so, neeed to transform value unit to GB.
		return used;
	}

	@Override
	protected void cleanup() {
	}
}
