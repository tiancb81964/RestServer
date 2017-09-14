package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;

/**
 * 
 * @author zhaoyim
 *
 */
public class HbaseServiceInstanceQuotaBean {

	private long maximumTablesQuota;
	private long maximumRegionsQuota;
	private String serviceType;

	public HbaseServiceInstanceQuotaBean() {

	}

	public HbaseServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.maximumTablesQuota = hdfsQuotaMap.get("maximumTablesQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("maximumTablesQuota")).longValue();
		this.maximumRegionsQuota = hdfsQuotaMap.get("maximumRegionsQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("maximumRegionsQuota")).longValue();

	}

	public long getMaximumTablesQuota() {
		return maximumTablesQuota;
	}

	public void setMaximumTablesQuota(long maximumTablesQuota) {
		this.maximumTablesQuota = maximumTablesQuota;
	}

	public long getMaximumRegionsQuota() {
		return maximumRegionsQuota;
	}

	public void setMaximumRegionsQuota(long maximumRegionsQuota) {
		this.maximumRegionsQuota = maximumRegionsQuota;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "HbaseServiceInstanceQuotaBean [maximumTablesQuota: " + maximumTablesQuota + ", maximumRegionsQuota: "
				+ maximumRegionsQuota + ", serviceType: " + serviceType + "]";
	}
}
