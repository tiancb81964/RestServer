package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;
import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.KafkaClient;

/**
 * Kafka resource monitor.
 * 
 * @author EthanWang
 *
 */
public class KafkaResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(KafkaResourcePeeker.class);

	private KafkaClient client;

	@Override
	protected void setup() {
		this.client = KafkaClient.getInstance();
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String topic) {
		if (resourceType.equals("topicQuota")) {
			return Long.valueOf(client.getPartitionCount(topic));
		} else if (resourceType.equals("partitionSize")) {
			return client.getRetensionSize(topic);
		} else {
			LOG.error("Unknow ResourceType: " + resourceType);
			throw new RuntimeException("Unknow ResourceType: " + resourceType);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String topic) {
		if (resourceType.equals("topicQuota")) {
			return Long.valueOf(client.getPartitionCount(topic));
		} else if (resourceType.equals("partitionSize")) {
			long sizeBytes = client.fetchTopicSize(topic);
			return sizeBytes;
		} else {
			LOG.error("Unknow ResourceType: " + resourceType);
			throw new RuntimeException("Unknow ResourceType: " + resourceType);
		}
	}

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("topicQuota", "partitionSize");
	}

	@Override
	public ServiceType getType() {
		return ServiceType.kafka;
	}

}
