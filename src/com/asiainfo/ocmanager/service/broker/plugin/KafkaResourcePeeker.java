package com.asiainfo.ocmanager.service.broker.plugin;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.KafkaClient;

/**
 * Kafka resource monitor.
 * @author EthanWang
 *
 */
public class KafkaResourcePeeker extends BaseResourcePeeker{
	private static final Logger LOG = Logger.getLogger(KafkaResourcePeeker.class);
	private static final String TOPIC_QUOTA = "topicQuota";
	private static final String PARTITION_SIZE = "partitionSize";

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
		if (resourceType.equals(TOPIC_QUOTA)) {
			return Long.valueOf(client.getPartitionCount(topic));
		}
		else if (resourceType.equals(PARTITION_SIZE)) {
			return client.getRetensionSize(topic);
		}
		else {
			LOG.error("Unknow ResourceType: " + resourceType);
			throw new RuntimeException("Unknow ResourceType: " + resourceType);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String topic) {
		if (resourceType.equals(TOPIC_QUOTA)) {
			return Long.valueOf(client.getPartitionCount(topic));
		}
		else if (resourceType.equals(PARTITION_SIZE)) {
			long sizeBytes = client.fetchTopicSize(topic);
			return sizeBytes;
		}
		else {
			LOG.error("Unknow ResourceType: " + resourceType);
			throw new RuntimeException("Unknow ResourceType: " + resourceType);
		}
	}

}
