package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.QuotaCommonUtils;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.asiainfo.ocmanager.utils.ServicesDefaultQuotaConf;

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

	public KafkaServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.topicTTL = quotaMap.get("topicTTL") == null ? 0 : Long.valueOf(quotaMap.get("topicTTL")).longValue();
		this.topicQuota = quotaMap.get("topicQuota") == null ? 0 : Long.valueOf(quotaMap.get("topicQuota")).longValue();
		this.partitionSize = quotaMap.get("partitionSize") == null ? 0
				: Long.valueOf(quotaMap.get("partitionSize")).longValue();
	}

	public static KafkaServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		KafkaServiceInstanceQuotaBean defaultServiceInstanceQuota = new KafkaServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("kafka");
		defaultServiceInstanceQuota
				.setTopicTTL(ServicesDefaultQuotaConf.getInstance().get("kafka").get("topicTTL").getDefaultQuota());
		defaultServiceInstanceQuota
				.setTopicQuota(ServicesDefaultQuotaConf.getInstance().get("kafka").get("topicQuota").getDefaultQuota());
		defaultServiceInstanceQuota.setPartitionSize(
				ServicesDefaultQuotaConf.getInstance().get("kafka").get("partitionSize").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// kafka
		if (this.topicTTL < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.topicTTL, "topicTTL", "kafka"));
			canChange = false;
		}
		if (this.topicQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.topicQuota, "topicQuota", "kafka"));
			canChange = false;
		}
		if (this.partitionSize < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.partitionSize, "partitionSize", "kafka"));
			canChange = false;
		}

		if (canChange) {
			resStr.append("can change the bsi!");
		}

		checkRes.setCanChange(canChange);
		checkRes.setMessages(resStr.toString());

		return checkRes;
	}

	public void plus(KafkaServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.topicTTL = this.topicTTL + otherServiceInstanceQuota.getTopicTTL();
		this.topicQuota = this.topicQuota + otherServiceInstanceQuota.getTopicQuota();
		this.partitionSize = this.partitionSize + otherServiceInstanceQuota.getPartitionSize();
	}

	public void minus(KafkaServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.topicTTL = this.topicTTL - otherServiceInstanceQuota.getTopicTTL();
		this.topicQuota = this.topicQuota - otherServiceInstanceQuota.getTopicQuota();
		this.partitionSize = this.partitionSize - otherServiceInstanceQuota.getPartitionSize();
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