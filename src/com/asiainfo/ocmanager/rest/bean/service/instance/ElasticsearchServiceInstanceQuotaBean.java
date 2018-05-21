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
public class ElasticsearchServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String REPLICAS = "replicas";
	public final static String VOLUME = "volume";

	private double replicas;
	private double volume;

	public ElasticsearchServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public ElasticsearchServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> elasticsearchQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType,
				quotaStr);
		this.replicas = elasticsearchQuotaMap.get(REPLICAS) == null ? 0
				: Double.valueOf(elasticsearchQuotaMap.get(REPLICAS)).doubleValue();
		this.volume = elasticsearchQuotaMap.get(VOLUME) == null ? 0
				: Double.valueOf(elasticsearchQuotaMap.get(VOLUME)).doubleValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public ElasticsearchServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.replicas = quotaMap.get(REPLICAS) == null ? 0 : Double.valueOf(quotaMap.get(REPLICAS)).doubleValue();
		this.volume = quotaMap.get(VOLUME) == null ? 0 : Double.valueOf(quotaMap.get(VOLUME)).doubleValue();

	}

	public static ElasticsearchServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		ElasticsearchServiceInstanceQuotaBean defaultServiceInstanceQuota = new ElasticsearchServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("elasticsearch");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("elasticsearch") == null
					|| ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(REPLICAS) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setReplicas(0);
			} else {
				defaultServiceInstanceQuota.setReplicas(
						ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(REPLICAS).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("elasticsearch") == null
					|| ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(VOLUME) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setVolume(0);
			} else {
				defaultServiceInstanceQuota.setVolume(
						ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(VOLUME).getDefaultQuota());
			}

		} else {
			if (parameters.get(REPLICAS) == null || parameters.get(REPLICAS).isJsonNull()
					|| parameters.get(REPLICAS).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setReplicas(
						ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(REPLICAS).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setReplicas(parameters.get(REPLICAS).getAsDouble());
			}

			if (parameters.get(VOLUME) == null || parameters.get(VOLUME).isJsonNull()
					|| parameters.get(VOLUME).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setVolume(
						ServicesDefaultQuotaConf.getInstance().get("elasticsearch").get(VOLUME).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setVolume(parameters.get(VOLUME).getAsDouble());
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

		// get all elasticsearch children bsi quota
		ElasticsearchServiceInstanceQuotaBean elasticsearchChildrenTotalQuota = new ElasticsearchServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			ElasticsearchServiceInstanceQuotaBean quota = new ElasticsearchServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			elasticsearchChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		ElasticsearchServiceInstanceQuotaBean elasticsearchParentTenantQuota = new ElasticsearchServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		elasticsearchParentTenantQuota.minus(elasticsearchChildrenTotalQuota);

		// get request bsi quota
		ElasticsearchServiceInstanceQuotaBean elasticsearchRequestServiceInstanceQuota = ElasticsearchServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		elasticsearchParentTenantQuota.minus(elasticsearchRequestServiceInstanceQuota);

		return elasticsearchParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// elasticsearch
		if (this.replicas < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.replicas, REPLICAS, "elasticsearch"));
			canChange = false;
		}
		if (this.volume < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.volume, VOLUME, "elasticsearch"));
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
	public void plus(ElasticsearchServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.replicas = this.replicas + otherServiceInstanceQuota.getReplicas();
		this.volume = this.volume + otherServiceInstanceQuota.getVolume();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(ElasticsearchServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.replicas = this.replicas - otherServiceInstanceQuota.getReplicas();
		this.volume = this.volume - otherServiceInstanceQuota.getVolume();

	}

	public double getReplicas() {
		return replicas;
	}

	public void setReplicas(double replicas) {
		this.replicas = replicas;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "ElasticsearchServiceInstanceQuotaBean [replicas: " + replicas + ", volume: " + volume
				+ ", serviceType: " + serviceType + "]";
	}

}
