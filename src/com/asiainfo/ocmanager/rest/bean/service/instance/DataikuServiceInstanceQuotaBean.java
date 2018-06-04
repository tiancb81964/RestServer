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
public class DataikuServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String CPU = "cpu";
	public final static String MEMORY = "memory";

	private double cpu;
	private double memory;

	public DataikuServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public DataikuServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> dataikuQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType,
				quotaStr);
		this.cpu = dataikuQuotaMap.get(CPU) == null ? 0
				: Double.valueOf(dataikuQuotaMap.get(CPU)).doubleValue();
		this.memory = dataikuQuotaMap.get(MEMORY) == null ? 0
				: Double.valueOf(dataikuQuotaMap.get(MEMORY)).doubleValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public DataikuServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.cpu = quotaMap.get(CPU) == null ? 0 : Double.valueOf(quotaMap.get(CPU)).doubleValue();
		this.memory = quotaMap.get(MEMORY) == null ? 0 : Double.valueOf(quotaMap.get(MEMORY)).doubleValue();

	}

	public static DataikuServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		DataikuServiceInstanceQuotaBean defaultServiceInstanceQuota = new DataikuServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("dataiku");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("dataiku") == null
					|| ServicesDefaultQuotaConf.getInstance().get("dataiku").get(CPU) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setCpu(0);
			} else {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("dataiku").get(CPU).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("dataiku") == null
					|| ServicesDefaultQuotaConf.getInstance().get("dataiku").get(MEMORY) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setMemory(0);
			} else {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("dataiku").get(MEMORY).getDefaultQuota());
			}

		} else {
			if (parameters.get(CPU) == null || parameters.get(CPU).isJsonNull()
					|| parameters.get(CPU).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("dataiku").get(CPU).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setCpu(parameters.get(CPU).getAsDouble());
			}

			if (parameters.get(MEMORY) == null || parameters.get(MEMORY).isJsonNull()
					|| parameters.get(MEMORY).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("dataiku").get(MEMORY).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setMemory(parameters.get(MEMORY).getAsDouble());
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

		// get all dataiku children bsi quota
		DataikuServiceInstanceQuotaBean dataikuChildrenTotalQuota = new DataikuServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			DataikuServiceInstanceQuotaBean quota = new DataikuServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			dataikuChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		DataikuServiceInstanceQuotaBean dataikuParentTenantQuota = new DataikuServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		dataikuParentTenantQuota.minus(dataikuChildrenTotalQuota);

		// get request bsi quota
		DataikuServiceInstanceQuotaBean dataikuRequestServiceInstanceQuota = DataikuServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		dataikuParentTenantQuota.minus(dataikuRequestServiceInstanceQuota);

		return dataikuParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// dataiku
		if (this.cpu < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.cpu, CPU, "dataiku"));
			canChange = false;
		}
		if (this.memory < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.memory, MEMORY, "dataiku"));
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
	public void plus(DataikuServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.cpu = this.cpu + otherServiceInstanceQuota.getCpu();
		this.memory = this.memory + otherServiceInstanceQuota.getMemory();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(DataikuServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.cpu = this.cpu - otherServiceInstanceQuota.getCpu();
		this.memory = this.memory - otherServiceInstanceQuota.getMemory();

	}

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		return "DataikuServiceInstanceQuotaBean [cpu: " + cpu + ", memory: " + memory
				+ ", serviceType: " + serviceType + "]";
	}

}
