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
public class ZeppelinServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String CPU = "cpu";
	public final static String MEMORY = "memory";

	private double cpu;
	private double memory;

	public ZeppelinServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public ZeppelinServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> zeppelinQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType,
				quotaStr);
		this.cpu = zeppelinQuotaMap.get(CPU) == null ? 0
				: Double.valueOf(zeppelinQuotaMap.get(CPU)).doubleValue();
		this.memory = zeppelinQuotaMap.get(MEMORY) == null ? 0
				: Double.valueOf(zeppelinQuotaMap.get(MEMORY)).doubleValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public ZeppelinServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.cpu = quotaMap.get(CPU) == null ? 0 : Double.valueOf(quotaMap.get(CPU)).doubleValue();
		this.memory = quotaMap.get(MEMORY) == null ? 0 : Double.valueOf(quotaMap.get(MEMORY)).doubleValue();

	}

	public static ZeppelinServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		ZeppelinServiceInstanceQuotaBean defaultServiceInstanceQuota = new ZeppelinServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("zeppelin");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("zeppelin") == null
					|| ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(CPU) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setCpu(0);
			} else {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(CPU).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("zeppelin") == null
					|| ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(MEMORY) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setMemory(0);
			} else {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(MEMORY).getDefaultQuota());
			}

		} else {
			if (parameters.get(CPU) == null || parameters.get(CPU).isJsonNull()
					|| parameters.get(CPU).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(CPU).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setCpu(parameters.get(CPU).getAsDouble());
			}

			if (parameters.get(MEMORY) == null || parameters.get(MEMORY).isJsonNull()
					|| parameters.get(MEMORY).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("zeppelin").get(MEMORY).getDefaultQuota());
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

		// get all zeppelin children bsi quota
		ZeppelinServiceInstanceQuotaBean zeppelinChildrenTotalQuota = new ZeppelinServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			ZeppelinServiceInstanceQuotaBean quota = new ZeppelinServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			zeppelinChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		ZeppelinServiceInstanceQuotaBean zeppelinParentTenantQuota = new ZeppelinServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		zeppelinParentTenantQuota.minus(zeppelinChildrenTotalQuota);

		// get request bsi quota
		ZeppelinServiceInstanceQuotaBean zeppelinRequestServiceInstanceQuota = ZeppelinServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		zeppelinParentTenantQuota.minus(zeppelinRequestServiceInstanceQuota);

		return zeppelinParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// zeppelin
		if (this.cpu < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.cpu, CPU, "zeppelin"));
			canChange = false;
		}
		if (this.memory < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.memory, MEMORY, "zeppelin"));
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
	public void plus(ZeppelinServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.cpu = this.cpu + otherServiceInstanceQuota.getCpu();
		this.memory = this.memory + otherServiceInstanceQuota.getMemory();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(ZeppelinServiceInstanceQuotaBean otherServiceInstanceQuota) {
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
		return "ZeppelinServiceInstanceQuotaBean [cpu: " + cpu + ", memory: " + memory
				+ ", serviceType: " + serviceType + "]";
	}

}
