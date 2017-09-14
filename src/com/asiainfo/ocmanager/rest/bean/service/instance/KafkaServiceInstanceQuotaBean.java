package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;

/**
 * 
 * @author zhaoyim
 *
 */
public class KafkaServiceInstanceQuotaBean {

	private long topicTTL;
	private long topicQuota;
	private long partitionSize;
	private String serviceType;

	public KafkaServiceInstanceQuotaBean() {

	}

	public KafkaServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.topicTTL = hdfsQuotaMap.get("topicTTL") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("topicTTL")).longValue();
		this.topicQuota = hdfsQuotaMap.get("topicQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("topicQuota")).longValue();
		this.partitionSize = hdfsQuotaMap.get("partitionSize") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("partitionSize")).longValue();
	}

	public long getTopicTTL() {
		return topicTTL;
	}

	public void setTopicTTL(long topicTTL) {
		this.topicTTL = topicTTL;
	}

	public long getTopicQuota() {
		return topicQuota;
	}

	public void setTopicQuota(long topicQuota) {
		this.topicQuota = topicQuota;
	}

	public long getPartitionSize() {
		return partitionSize;
	}

	public void setPartitionSize(long partitionSize) {
		this.partitionSize = partitionSize;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "KafkaServiceInstanceQuotaBean [topicTTL: " + topicTTL + ", topicQuota: " + topicQuota
				+ ", partitionSize: " + partitionSize + ", serviceType: " + serviceType + "]";
	}
}