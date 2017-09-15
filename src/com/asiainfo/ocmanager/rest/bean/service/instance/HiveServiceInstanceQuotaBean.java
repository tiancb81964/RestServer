package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.QuotaCommonUtils;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.asiainfo.ocmanager.utils.ServicesDefaultQuotaConf;

/**
 * 
 * @author zhaoyim
 *
 */
public class HiveServiceInstanceQuotaBean {

	private long storageSpaceQuota;
	private long yarnQueueQuota;
	private String serviceType;

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

	public static HiveServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		HiveServiceInstanceQuotaBean defaultServiceInstanceQuota = new HiveServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hive");
		defaultServiceInstanceQuota.setYarnQueueQuota(
				ServicesDefaultQuotaConf.getInstance().get("hive").get("yarnQueueQuota").getDefaultQuota());
		defaultServiceInstanceQuota.setStorageSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hive").get("storageSpaceQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst() {
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

	public void plus(HiveServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota + otherServiceInstanceQuota.getYarnQueueQuota();
		this.storageSpaceQuota = this.storageSpaceQuota + otherServiceInstanceQuota.getStorageSpaceQuota();
	}

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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "HiveServiceInstanceQuotaBean [storageSpaceQuota: " + storageSpaceQuota + ", yarnQueueQuota: "
				+ yarnQueueQuota + ", serviceType: " + serviceType + "]";
	}

}
