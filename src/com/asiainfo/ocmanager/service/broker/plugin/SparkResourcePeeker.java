package com.asiainfo.ocmanager.service.broker.plugin;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.YarnClient;
import com.google.gson.JsonObject;

public class SparkResourcePeeker extends BaseResourcePeeker{
	private static final Logger LOG = Logger.getLogger(SparkResourcePeeker.class);
	private static final String TYPE = "yarnQueueQuota";
	private YarnClient client;
	

	@Override
	protected void setup() {
		this.client = YarnClient.getInstance();
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String queueName) {
		if (!resourceType.equals(TYPE)) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		try {
			JsonObject queue = client.fetchQueueInfoByName(queueName);
			//TODO: total memory can not be retrieved from rm.
			queue.getAsJsonObject("");
			return -999l;
		} catch (Exception e) {
			LOG.error("Error while fetching queue infos: ", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String queueName) {
		if (!resourceType.equals(TYPE)) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		try {
			JsonObject queue = client.fetchQueueInfoByName(queueName);
			long used = queue.getAsJsonObject("resourcesUsed").getAsJsonPrimitive("memory").getAsLong();
			//TODO: if need to change to "usedCapacity"(percentage) instead of "resourcesUsed/memory"
			// if so, neeed to transform value unit to GB.
			return used;
		} catch (Exception e) {
			LOG.error("Error while fetching queue infos: ", e);
			throw new RuntimeException(e);
		}
	}

}
