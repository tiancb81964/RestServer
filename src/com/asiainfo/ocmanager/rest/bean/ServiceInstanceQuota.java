package com.asiainfo.ocmanager.rest.bean;

import java.util.List;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;

/**
 * instance quota
 * @author EthanWang
 *
 */
public class ServiceInstanceQuota {
	private ServiceType serviceType;
	private String instanceId;
	private List<QuotaBean> usage;
	
	public ServiceInstanceQuota (ServiceType type, String id) {
		this.serviceType = type;
		this.instanceId = id;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public List<QuotaBean> getUsage() {
		return usage;
	}
	public void setUsage(List<QuotaBean> usage) {
		this.usage = usage;
	}
	public ServiceType getService() {
		return serviceType;
	}
}
