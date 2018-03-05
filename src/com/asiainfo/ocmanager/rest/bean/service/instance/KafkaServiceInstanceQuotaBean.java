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
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class KafkaServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String TOPICQUOTA = "topicQuota";
	public final static String PARTITIONSIZE = "partitionSize";
	private double topicQuota;
	private double partitionSize;

	public KafkaServiceInstanceQuotaBean() {

	}

	public KafkaServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.topicQuota = hdfsQuotaMap.get(TOPICQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(TOPICQUOTA)).longValue();
		this.partitionSize = hdfsQuotaMap.get(PARTITIONSIZE) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(PARTITIONSIZE)).longValue();
	}

	public KafkaServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.topicQuota = quotaMap.get(TOPICQUOTA) == null ? 0 : Long.valueOf(quotaMap.get(TOPICQUOTA)).longValue();
		this.partitionSize = quotaMap.get(PARTITIONSIZE) == null ? 0
				: Long.valueOf(quotaMap.get(PARTITIONSIZE)).longValue();
	}

	/**
	 * 
	 * @return
	 */
	public static KafkaServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		KafkaServiceInstanceQuotaBean defaultServiceInstanceQuota = new KafkaServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("kafka");

		// if passby the params use the params otherwise use the default
		if (parameters == null) {
			defaultServiceInstanceQuota.setTopicQuota(
					ServicesDefaultQuotaConf.getInstance().get("kafka").get(TOPICQUOTA).getDefaultQuota());
			defaultServiceInstanceQuota.setPartitionSize(
					ServicesDefaultQuotaConf.getInstance().get("kafka").get(PARTITIONSIZE).getDefaultQuota());
		} else {
			if (parameters.get(TOPICQUOTA) == null || parameters.get(TOPICQUOTA).isJsonNull()
					|| parameters.get(TOPICQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setTopicQuota(
						ServicesDefaultQuotaConf.getInstance().get("kafka").get(TOPICQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setTopicQuota(parameters.get(TOPICQUOTA).getAsLong());
			}

			if (parameters.get(PARTITIONSIZE) == null || parameters.get(PARTITIONSIZE).isJsonNull()
					|| parameters.get(PARTITIONSIZE).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setPartitionSize(
						ServicesDefaultQuotaConf.getInstance().get("kafka").get(PARTITIONSIZE).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setPartitionSize(parameters.get(PARTITIONSIZE).getAsLong());
			}
		}

		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId,
			JsonObject parameters) {

		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceName(tenantId, backingServiceName);
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
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		kafkaParentTenantQuota.minus(kafkaRequestServiceInstanceQuota);

		return kafkaParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;
		
		if (this.topicQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.topicQuota, TOPICQUOTA, "kafka"));
			canChange = false;
		}
		if (this.partitionSize < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.partitionSize, PARTITIONSIZE, "kafka"));
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
		this.topicQuota = this.topicQuota + otherServiceInstanceQuota.getTopicQuota();
		this.partitionSize = this.partitionSize + otherServiceInstanceQuota.getPartitionSize();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(KafkaServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.topicQuota = this.topicQuota - otherServiceInstanceQuota.getTopicQuota();
		this.partitionSize = this.partitionSize - otherServiceInstanceQuota.getPartitionSize();
	}

	public double getTopicQuota() {
		return topicQuota;
	}

	public void setTopicQuota(double topicQuota) {
		this.topicQuota = topicQuota;
	}

	public double getPartitionSize() {
		return partitionSize;
	}

	public void setPartitionSize(double partitionSize) {
		this.partitionSize = partitionSize;
	}

	@Override
	public String toString() {
		return "KafkaServiceInstanceQuotaBean [topicQuota: " + topicQuota
				+ ", partitionSize: " + partitionSize + ", serviceType: " + serviceType + "]";
	}
}