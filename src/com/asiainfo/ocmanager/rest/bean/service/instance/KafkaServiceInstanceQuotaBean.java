package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.QuotaCommonUtils;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.TenantQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.asiainfo.ocmanager.utils.ServicesDefaultQuotaConf;

/**
 * 
 * @author zhaoyim
 *
 */
public class KafkaServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	private long topicTTL;
	private long topicQuota;
	private long partitionSize;

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

	/**
	 * 
	 * @return
	 */
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

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId) {

		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceType(tenantId, backingServiceName);
		Tenant parentTenant = TenantPersistenceWrapper.getTenantById(tenantId);

		// get all mapreduce children bsi quota
		KafkaServiceInstanceQuotaBean kafkaChildrenTotalQuota = new KafkaServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			KafkaServiceInstanceQuotaBean quota = new KafkaServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			kafkaChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		KafkaServiceInstanceQuotaBean kafkaParentTenantQuota = new KafkaServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		kafkaParentTenantQuota.minus(kafkaChildrenTotalQuota);

		// get request bsi quota
		KafkaServiceInstanceQuotaBean kafkaRequestServiceInstanceQuota = KafkaServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota();

		// left quota minus request quota
		kafkaParentTenantQuota.minus(kafkaRequestServiceInstanceQuota);

		return kafkaParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
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

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void plus(KafkaServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.topicTTL = this.topicTTL + otherServiceInstanceQuota.getTopicTTL();
		this.topicQuota = this.topicQuota + otherServiceInstanceQuota.getTopicQuota();
		this.partitionSize = this.partitionSize + otherServiceInstanceQuota.getPartitionSize();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
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

	@Override
	public String toString() {
		return "KafkaServiceInstanceQuotaBean [topicTTL: " + topicTTL + ", topicQuota: " + topicQuota
				+ ", partitionSize: " + partitionSize + ", serviceType: " + serviceType + "]";
	}
}