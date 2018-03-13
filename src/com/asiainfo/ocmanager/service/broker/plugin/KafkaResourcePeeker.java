package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.KafkaClient;

/**
 * Kafka resource monitor.
 * 
 * @author EthanWang
 *
 */
public class KafkaResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(KafkaResourcePeeker.class);
	private KafkaClient client;
	
	public KafkaResourcePeeker(String serviceName) {
		super(serviceName);
		if (serviceName.isEmpty()) return;
		try {
			ServiceClientInterface cli = ServiceClientPool.getInstance().getClient(serviceName);
			if (!(cli instanceof KafkaClient)) {
				LOG.error("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
				throw new RuntimeException("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
			}
			this.client = (KafkaClient)cli;
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
	public Class<? extends ServiceClient> getClientClass() {
		return KafkaClient.class;
	}

}
