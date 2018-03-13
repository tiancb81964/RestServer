package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.YarnClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Resource monitor for Mapreduce.
 * 
 * @author EthanWang
 *
 */
public class MapRedResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(MapRedResourcePeeker.class);
	private YarnClient client;
	private String queuesInfo;

	public MapRedResourcePeeker(String serviceName) {
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
		try {
			this.queuesInfo = client.fetchQueuesInfo();
		} catch (Exception e) {
			LOG.error("Error while fetching queue info from RM: ", e);
			throw new RuntimeException("Error while fetching queue info from RM: ", e);
		}
	}
	
	@Override
	protected Long fetchTotalQuota(String resourceType, String queueName) {
		if (!resourceType.equals("yarnQueueQuota")) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		JsonObject jsonObj = new JsonParser().parse(this.queuesInfo).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		double capacity = queue.get("absoluteCapacity").getAsDouble() / 100;
		return new Double(capacity * totalMB()).longValue();
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

	/**
	 * Get specified queue by the given name.
	 * 
	 * @param json
	 * @param queueName
	 * @return
	 */
	private JsonObject findQueueByName(JsonObject json, String queueName) {
		JsonArray queues = json.getAsJsonObject("scheduler").getAsJsonObject("schedulerInfo").getAsJsonObject("queues")
				.getAsJsonArray("queue");
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
		if (!resourceType.equals("yarnQueueQuota")) {
			LOG.error("Unknown resource type: " + resourceType);
			throw new RuntimeException("Unknown resource type: " + resourceType);
		}
		JsonObject jsonObj = new JsonParser().parse(this.queuesInfo).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		long used = queue.getAsJsonObject("resourcesUsed").getAsJsonPrimitive("memory").getAsLong();
		return used;
	}

	@Override
	protected void cleanup() {
	}

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("yarnQueueQuota");
	}

	@Override
	public Class<? extends ServiceClient> getClientClass() {
		return YarnClient.class;
	}

	@Override
	protected void setup() {
		
	}

}
