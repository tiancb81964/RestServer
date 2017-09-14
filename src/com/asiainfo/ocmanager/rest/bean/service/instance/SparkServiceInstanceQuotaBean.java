package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;

/**
 * 
 * @author zhaoyim
 *
 */
public class SparkServiceInstanceQuotaBean {

	private long yarnQueueQuota;
	private String serviceType;

	public SparkServiceInstanceQuotaBean() {

	}

	public SparkServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.yarnQueueQuota = hdfsQuotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("yarnQueueQuota")).longValue();

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
		return "SparkServiceInstanceQuotaBean [yarnQueueQuota: " + yarnQueueQuota + ", serviceType: " + serviceType
				+ "]";
	}
}
