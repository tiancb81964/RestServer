package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.YarnClient;
import com.google.gson.JsonObject;

public class SparkResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(SparkResourcePeeker.class);
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
		if (!resourceType.equals("yarnQueueQuota")) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		try {
			JsonObject queue = client.fetchQueueInfoByName(queueName);
			double capacity = queue.get("absoluteCapacity").getAsDouble() / 100;
			return new Double(capacity * totalMB()).longValue();
		} catch (Exception e) {
			LOG.error("Error while fetching queue infos: ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Total memory of yarn.
	 * 
	 * @return
	 */
	private double totalMB() {
		try {
			String mb = YarnClient.getInstance().fetchMetircs().get("totalMB");
			return Double.valueOf(mb);
		} catch (Exception e) {
			LOG.error("Error while fetch yarn metrics: ", e);
			throw new RuntimeException("Error while fetch yarn metrics: ", e);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String queueName) {
		if (!resourceType.equals("yarnQueueQuota")) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		try {
			JsonObject queue = client.fetchQueueInfoByName(queueName);
			long used = queue.getAsJsonObject("resourcesUsed").getAsJsonPrimitive("memory").getAsLong();
			return used;
		} catch (Exception e) {
			LOG.error("Error while fetching queue infos: ", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<String> resourceTypes() {
		return Arrays.asList("yarnQueueQuota");
	}

}
