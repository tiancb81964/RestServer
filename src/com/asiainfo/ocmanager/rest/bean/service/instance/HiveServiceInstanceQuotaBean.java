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
public class HiveServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String STORAGESPACEQUOTA = "storageSpaceQuota";
	private double storageSpaceQuota;

	public HiveServiceInstanceQuotaBean() {

	}

	public HiveServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.storageSpaceQuota = hdfsQuotaMap.get(STORAGESPACEQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(STORAGESPACEQUOTA)).longValue();

	}

	public HiveServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.storageSpaceQuota = quotaMap.get(STORAGESPACEQUOTA) == null ? 0
				: Long.valueOf(quotaMap.get(STORAGESPACEQUOTA)).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static HiveServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		HiveServiceInstanceQuotaBean defaultServiceInstanceQuota = new HiveServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hive");

		// if passby the params use the params otherwise use the default
		if (parameters == null) {
			defaultServiceInstanceQuota.setStorageSpaceQuota(
					ServicesDefaultQuotaConf.getInstance().get("hive").get(STORAGESPACEQUOTA).getDefaultQuota());
		} else {
			if (parameters.get(STORAGESPACEQUOTA) == null || parameters.get(STORAGESPACEQUOTA).isJsonNull()
					|| parameters.get(STORAGESPACEQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setStorageSpaceQuota(
						ServicesDefaultQuotaConf.getInstance().get("hive").get(STORAGESPACEQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setStorageSpaceQuota(parameters.get(STORAGESPACEQUOTA).getAsLong());
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

		// get all hive children bsi quota
		HiveServiceInstanceQuotaBean hiveChildrenTotalQuota = new HiveServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			HiveServiceInstanceQuotaBean quota = new HiveServiceInstanceQuotaBean(backingServiceName, inst.getQuota());
			hiveChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		HiveServiceInstanceQuotaBean hiveParentTenantQuota = new HiveServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		hiveParentTenantQuota.minus(hiveChildrenTotalQuota);

		// get request bsi quota
		HiveServiceInstanceQuotaBean hiveRequestServiceInstanceQuota = HiveServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		hiveParentTenantQuota.minus(hiveRequestServiceInstanceQuota);

		return hiveParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// hive
		if (this.storageSpaceQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.storageSpaceQuota, STORAGESPACEQUOTA, "hive"));
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
	public void plus(HiveServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.storageSpaceQuota = this.storageSpaceQuota + otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(HiveServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.storageSpaceQuota = this.storageSpaceQuota - otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	public double getStorageSpaceQuota() {
		return storageSpaceQuota;
	}

	public void setStorageSpaceQuota(double storageSpaceQuota) {
		this.storageSpaceQuota = storageSpaceQuota;
	}

	@Override
	public String toString() {
		return "HiveServiceInstanceQuotaBean [storageSpaceQuota: " + storageSpaceQuota + ", serviceType: " + serviceType + "]";
	}

}
