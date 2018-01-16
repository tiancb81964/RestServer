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

public class StormServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String MEMORY = "memory";
	public final static String SUPERVISORS = "supervisors";
	public final static String WORKERS = "workers";

	private double memory;
	private double supervisors;
	private double workers;

	public StormServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public StormServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> stormQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.memory = stormQuotaMap.get(MEMORY) == null ? 0 : Double.valueOf(stormQuotaMap.get(MEMORY)).doubleValue();
		this.supervisors = stormQuotaMap.get(SUPERVISORS) == null ? 0
				: Double.valueOf(stormQuotaMap.get(SUPERVISORS)).doubleValue();
		this.workers = stormQuotaMap.get(WORKERS) == null ? 0
				: Double.valueOf(stormQuotaMap.get(WORKERS)).doubleValue();
	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public StormServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.memory = quotaMap.get(MEMORY) == null ? 0 : Double.valueOf(quotaMap.get(MEMORY)).doubleValue();
		this.supervisors = quotaMap.get(SUPERVISORS) == null ? 0
				: Double.valueOf(quotaMap.get(SUPERVISORS)).doubleValue();
		this.workers = quotaMap.get(WORKERS) == null ? 0 : Double.valueOf(quotaMap.get(WORKERS)).doubleValue();
	}

	public static StormServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		StormServiceInstanceQuotaBean defaultServiceInstanceQuota = new StormServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("storm");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("storm") == null
					|| ServicesDefaultQuotaConf.getInstance().get("storm").get(MEMORY) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setMemory(0);
			} else {
				defaultServiceInstanceQuota
						.setMemory(ServicesDefaultQuotaConf.getInstance().get("storm").get(MEMORY).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("storm") == null
					|| ServicesDefaultQuotaConf.getInstance().get("storm").get(SUPERVISORS) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setSupervisors(0);
			} else {
				defaultServiceInstanceQuota.setSupervisors(
						ServicesDefaultQuotaConf.getInstance().get("storm").get(SUPERVISORS).getDefaultQuota());
			}
			if (ServicesDefaultQuotaConf.getInstance().get("storm") == null
					|| ServicesDefaultQuotaConf.getInstance().get("storm").get(WORKERS) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setWorkers(0);
			} else {
				defaultServiceInstanceQuota
						.setWorkers(ServicesDefaultQuotaConf.getInstance().get("storm").get(WORKERS).getDefaultQuota());
			}

		} else {
			if (parameters.get(MEMORY) == null || parameters.get(MEMORY).isJsonNull()
					|| parameters.get(MEMORY).getAsString().isEmpty()) {
				defaultServiceInstanceQuota
						.setMemory(ServicesDefaultQuotaConf.getInstance().get("storm").get(MEMORY).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setMemory(parameters.get(MEMORY).getAsDouble());
			}

			if (parameters.get(SUPERVISORS) == null || parameters.get(SUPERVISORS).isJsonNull()
					|| parameters.get(SUPERVISORS).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setSupervisors(
						ServicesDefaultQuotaConf.getInstance().get("storm").get(SUPERVISORS).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setSupervisors(parameters.get(SUPERVISORS).getAsDouble());
			}

			if (parameters.get(WORKERS) == null || parameters.get(WORKERS).isJsonNull()
					|| parameters.get(WORKERS).getAsString().isEmpty()) {
				defaultServiceInstanceQuota
						.setWorkers(ServicesDefaultQuotaConf.getInstance().get("storm").get(WORKERS).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setWorkers(parameters.get(WORKERS).getAsDouble());
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

		// get all storm children bsi quota
		StormServiceInstanceQuotaBean stormChildrenTotalQuota = new StormServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			StormServiceInstanceQuotaBean quota = new StormServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			stormChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		StormServiceInstanceQuotaBean stormParentTenantQuota = new StormServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		stormParentTenantQuota.minus(stormChildrenTotalQuota);

		// get request bsi quota
		StormServiceInstanceQuotaBean stormRequestServiceInstanceQuota = StormServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		stormParentTenantQuota.minus(stormRequestServiceInstanceQuota);

		return stormParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// storm
		if (this.memory < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.memory, MEMORY, "storm"));
			canChange = false;
		}
		if (this.supervisors < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.supervisors, SUPERVISORS, "storm"));
			canChange = false;
		}
		if (this.workers < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.workers, WORKERS, "storm"));
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
	public void plus(StormServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.memory = this.memory + otherServiceInstanceQuota.getMemory();
		this.supervisors = this.supervisors + otherServiceInstanceQuota.getSupervisors();
		this.workers = this.workers + otherServiceInstanceQuota.getWorkers();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(StormServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.memory = this.memory - otherServiceInstanceQuota.getMemory();
		this.supervisors = this.supervisors - otherServiceInstanceQuota.getSupervisors();
		this.workers = this.workers - otherServiceInstanceQuota.getWorkers();
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getSupervisors() {
		return supervisors;
	}

	public void setSupervisors(double supervisors) {
		this.supervisors = supervisors;
	}

	public double getWorkers() {
		return workers;
	}

	public void setWorkers(double workers) {
		this.workers = workers;
	}

	@Override
	public String toString() {
		return "StormServiceInstanceQuotaBean [memory: " + memory + ", supervisors: " + supervisors + ", workers: "
				+ workers + ", serviceType: " + serviceType + "]";
	}

}
