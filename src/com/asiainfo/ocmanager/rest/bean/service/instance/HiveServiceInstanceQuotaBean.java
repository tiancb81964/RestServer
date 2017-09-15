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
public class HiveServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	private long storageSpaceQuota;
	private long yarnQueueQuota;

	public HiveServiceInstanceQuotaBean() {

	}

	public HiveServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.storageSpaceQuota = hdfsQuotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("storageSpaceQuota")).longValue();
		this.yarnQueueQuota = hdfsQuotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("yarnQueueQuota")).longValue();

	}

	public HiveServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.yarnQueueQuota = quotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(quotaMap.get("yarnQueueQuota")).longValue();
		this.storageSpaceQuota = quotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(quotaMap.get("storageSpaceQuota")).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static HiveServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		HiveServiceInstanceQuotaBean defaultServiceInstanceQuota = new HiveServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hive");
		defaultServiceInstanceQuota.setYarnQueueQuota(
				ServicesDefaultQuotaConf.getInstance().get("hive").get("yarnQueueQuota").getDefaultQuota());
		defaultServiceInstanceQuota.setStorageSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hive").get("storageSpaceQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId) {

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
				.createDefaultServiceInstanceQuota();

		// left quota minus request quota
		hiveParentTenantQuota.minus(hiveRequestServiceInstanceQuota);

		return hiveParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// hive
		if (this.yarnQueueQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.yarnQueueQuota, "yarnQueueQuota", "hive"));
			canChange = false;
		}
		if (this.storageSpaceQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.storageSpaceQuota, "storageSpaceQuota", "hive"));
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
		this.yarnQueueQuota = this.yarnQueueQuota + otherServiceInstanceQuota.getYarnQueueQuota();
		this.storageSpaceQuota = this.storageSpaceQuota + otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(HiveServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota - otherServiceInstanceQuota.getYarnQueueQuota();
		this.storageSpaceQuota = this.storageSpaceQuota - otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	public long getStorageSpaceQuota() {
		return storageSpaceQuota;
	}

	public void setStorageSpaceQuota(long storageSpaceQuota) {
		this.storageSpaceQuota = storageSpaceQuota;
	}

	public long getYarnQueueQuota() {
		return yarnQueueQuota;
	}

	public void setYarnQueueQuota(long yarnQueueQuota) {
		this.yarnQueueQuota = yarnQueueQuota;
	}

	@Override
	public String toString() {
		return "HiveServiceInstanceQuotaBean [storageSpaceQuota: " + storageSpaceQuota + ", yarnQueueQuota: "
				+ yarnQueueQuota + ", serviceType: " + serviceType + "]";
	}

}
