package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.YarnClient;
import com.google.gson.JsonObject;

public class SparkResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(SparkResourcePeeker.class);
	private YarnClient client;

	public SparkResourcePeeker(String serviceName) {
		super(serviceName);
		if (serviceName.isEmpty()) return;
		try {
			ServiceClientInterface cli = ServiceClientPool.getInstance().getClient(serviceName);
			if (!(cli instanceof YarnClient)) {
				LOG.error("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
				throw new RuntimeException("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
			}
			client = (YarnClient)cli;
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
			String mb = client.fetchMetircs().get("totalMB");
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
	public List<String> resourceTypes() {
		return Arrays.asList("yarnQueueQuota");
	}

	@Override
	public Class<? extends ServiceClient> getClientClass() {
		return YarnClient.class;
	}

}
