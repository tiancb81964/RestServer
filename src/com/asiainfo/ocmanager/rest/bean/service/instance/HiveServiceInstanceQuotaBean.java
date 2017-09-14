package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;

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
