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
public class AnacondaServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String CPU = "cpu";
	public final static String MEMORY = "memory";

	private double cpu;
	private double memory;

	public AnacondaServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public AnacondaServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> anacondaQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType,
				quotaStr);
		this.cpu = anacondaQuotaMap.get(CPU) == null ? 0
				: Double.valueOf(anacondaQuotaMap.get(CPU)).doubleValue();
		this.memory = anacondaQuotaMap.get(MEMORY) == null ? 0
				: Double.valueOf(anacondaQuotaMap.get(MEMORY)).doubleValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public AnacondaServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.cpu = quotaMap.get(CPU) == null ? 0 : Double.valueOf(quotaMap.get(CPU)).doubleValue();
		this.memory = quotaMap.get(MEMORY) == null ? 0 : Double.valueOf(quotaMap.get(MEMORY)).doubleValue();

	}

	public static AnacondaServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		AnacondaServiceInstanceQuotaBean defaultServiceInstanceQuota = new AnacondaServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("anaconda");

		// if passby the params use the params otherwise use the default
		if (parameters == null || parameters.isJsonNull() || parameters.size() == 0) {
			if (ServicesDefaultQuotaConf.getInstance().get("anaconda") == null
					|| ServicesDefaultQuotaConf.getInstance().get("anaconda").get(CPU) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setCpu(0);
			} else {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("anaconda").get(CPU).getDefaultQuota());
			}

			if (ServicesDefaultQuotaConf.getInstance().get("anaconda") == null
					|| ServicesDefaultQuotaConf.getInstance().get("anaconda").get(MEMORY) == null) {
				// set default to 0, it means not limit
				defaultServiceInstanceQuota.setMemory(0);
			} else {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("anaconda").get(MEMORY).getDefaultQuota());
			}

		} else {
			if (parameters.get(CPU) == null || parameters.get(CPU).isJsonNull()
					|| parameters.get(CPU).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setCpu(
						ServicesDefaultQuotaConf.getInstance().get("anaconda").get(CPU).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setCpu(parameters.get(CPU).getAsDouble());
			}

			if (parameters.get(MEMORY) == null || parameters.get(MEMORY).isJsonNull()
					|| parameters.get(MEMORY).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setMemory(
						ServicesDefaultQuotaConf.getInstance().get("anaconda").get(MEMORY).getDefaultQuota());
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

		// get all anaconda children bsi quota
		AnacondaServiceInstanceQuotaBean anacondaChildrenTotalQuota = new AnacondaServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			AnacondaServiceInstanceQuotaBean quota = new AnacondaServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			anacondaChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		AnacondaServiceInstanceQuotaBean anacondaParentTenantQuota = new AnacondaServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		anacondaParentTenantQuota.minus(anacondaChildrenTotalQuota);

		// get request bsi quota
		AnacondaServiceInstanceQuotaBean anacondaRequestServiceInstanceQuota = AnacondaServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		anacondaParentTenantQuota.minus(anacondaRequestServiceInstanceQuota);

		return anacondaParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// anaconda
		if (this.cpu < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.cpu, CPU, "anaconda"));
			canChange = false;
		}
		if (this.memory < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.memory, MEMORY, "anaconda"));
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
	public void plus(AnacondaServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.cpu = this.cpu + otherServiceInstanceQuota.getCpu();
		this.memory = this.memory + otherServiceInstanceQuota.getMemory();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(AnacondaServiceInstanceQuotaBean otherServiceInstanceQuota) {
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
		return "AnacondaServiceInstanceQuotaBean [cpu: " + cpu + ", memory: " + memory
				+ ", serviceType: " + serviceType + "]";
	}

}
