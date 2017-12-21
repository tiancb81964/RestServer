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
public class RedisServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String MEMORY = "memory";
	public final static String NODES = "nodes";
	public final static String VOLUMESIZE = "volumeSize";

	private double memory;
	private double nodes;
	private double volumeSize;

	public RedisServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public RedisServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> redisQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.memory = redisQuotaMap.get(MEMORY) == null ? 0 : Long.valueOf(redisQuotaMap.get(MEMORY)).longValue();
		this.nodes = redisQuotaMap.get(NODES) == null ? 0 : Long.valueOf(redisQuotaMap.get(NODES)).longValue();
		this.volumeSize = redisQuotaMap.get(VOLUMESIZE) == null ? 0
				: Long.valueOf(redisQuotaMap.get(VOLUMESIZE)).longValue();
	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public RedisServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.memory = quotaMap.get(MEMORY) == null ? 0 : Long.valueOf(quotaMap.get(MEMORY)).longValue();
		this.nodes = quotaMap.get(NODES) == null ? 0 : Long.valueOf(quotaMap.get(NODES)).longValue();
		this.volumeSize = quotaMap.get(VOLUMESIZE) == null ? 0 : Long.valueOf(quotaMap.get(VOLUMESIZE)).longValue();
	}

	public static RedisServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		RedisServiceInstanceQuotaBean defaultServiceInstanceQuota = new RedisServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("redis");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("redis") == null
					|| ServicesDefaultQuotaConf.getInstance().get("redis").get(MEMORY) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setMemory(0);
			} else {
				defaultServiceInstanceQuota
						.setMemory(ServicesDefaultQuotaConf.getInstance().get("redis").get(MEMORY).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("redis") == null
					|| ServicesDefaultQuotaConf.getInstance().get("redis").get(NODES) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setNodes(0);
			} else {
				defaultServiceInstanceQuota
						.setNodes(ServicesDefaultQuotaConf.getInstance().get("redis").get(NODES).getDefaultQuota());
			}
			if (ServicesDefaultQuotaConf.getInstance().get("redis") == null
					|| ServicesDefaultQuotaConf.getInstance().get("redis").get(VOLUMESIZE) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setVolumeSize(0);
			} else {
				defaultServiceInstanceQuota.setVolumeSize(
						ServicesDefaultQuotaConf.getInstance().get("redis").get(VOLUMESIZE).getDefaultQuota());
			}

		} else {
			if (parameters.get(MEMORY) == null || parameters.get(MEMORY).isJsonNull()
					|| parameters.get(MEMORY).getAsString().isEmpty()) {
				defaultServiceInstanceQuota
						.setMemory(ServicesDefaultQuotaConf.getInstance().get("redis").get(MEMORY).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setMemory(parameters.get(MEMORY).getAsLong());
			}

			if (parameters.get(NODES) == null || parameters.get(NODES).isJsonNull()
					|| parameters.get(NODES).getAsString().isEmpty()) {
				defaultServiceInstanceQuota
						.setNodes(ServicesDefaultQuotaConf.getInstance().get("redis").get(NODES).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setNodes(parameters.get(NODES).getAsLong());
			}

			if (parameters.get(VOLUMESIZE) == null || parameters.get(VOLUMESIZE).isJsonNull()
					|| parameters.get(VOLUMESIZE).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setVolumeSize(
						ServicesDefaultQuotaConf.getInstance().get("redis").get(VOLUMESIZE).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setVolumeSize(parameters.get(VOLUMESIZE).getAsLong());
			}

		}

		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId,
			JsonObject parameters) {
		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceType(tenantId, backingServiceName);
		Tenant parentTenant = TenantPersistenceWrapper.getTenantById(tenantId);

		// get all redis children bsi quota
		RedisServiceInstanceQuotaBean redisChildrenTotalQuota = new RedisServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			RedisServiceInstanceQuotaBean quota = new RedisServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			redisChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		RedisServiceInstanceQuotaBean redisParentTenantQuota = new RedisServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		redisParentTenantQuota.minus(redisChildrenTotalQuota);

		// get request bsi quota
		RedisServiceInstanceQuotaBean redisRequestServiceInstanceQuota = RedisServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		redisParentTenantQuota.minus(redisRequestServiceInstanceQuota);

		return redisParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// redis
		if (this.memory < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.memory, MEMORY, "redis"));
			canChange = false;
		}
		if (this.nodes < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.nodes, NODES, "redis"));
			canChange = false;
		}
		if (this.volumeSize < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.volumeSize, VOLUMESIZE, "redis"));
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
	public void plus(RedisServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.memory = this.memory + otherServiceInstanceQuota.getMemory();
		this.nodes = this.nodes + otherServiceInstanceQuota.getNodes();
		this.volumeSize = this.volumeSize + otherServiceInstanceQuota.getVolumeSize();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(RedisServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.memory = this.memory - otherServiceInstanceQuota.getMemory();
		this.nodes = this.nodes - otherServiceInstanceQuota.getNodes();
		this.volumeSize = this.volumeSize - otherServiceInstanceQuota.getVolumeSize();
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getNodes() {
		return nodes;
	}

	public void setNodes(double nodes) {
		this.nodes = nodes;
	}

	public double getVolumeSize() {
		return volumeSize;
	}

	public void setVolumeSize(double volumeSize) {
		this.volumeSize = volumeSize;
	}

	@Override
	public String toString() {
		return "RedisServiceInstanceQuotaBean [memory: " + memory + ", nodes: " + nodes + ", volumeSize: " + volumeSize
				+ ", serviceType: " + serviceType + "]";
	}

}
