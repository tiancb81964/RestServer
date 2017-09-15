package com.asiainfo.ocmanager.rest.bean.service.instance;

/**
 * 
 * @author zhaoyim
 *
 */
public abstract class ServiceInstanceQuotaBean {
	protected String serviceType;

	// public abstract ServiceInstanceQuotaCheckerResponse checkCanChangeInst();
	//
	// public abstract void plus(ServiceInstanceQuotaBean
	// otherServiceInstanceQuota);
	//
	// public abstract void minus(ServiceInstanceQuotaBean
	// otherServiceInstanceQuota);

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
