package com.asiainfo.ocmanager.rest.resource.utils.model;

import com.asiainfo.ocmanager.rest.bean.TenantBeanV2;

public class TenantResponseV2 {

	private TenantQuotaCheckerResponse checkerRes;
	private TenantBeanV2 tenantBean = new TenantBeanV2();

	public TenantQuotaCheckerResponse getCheckerRes() {
		return checkerRes;
	}

	public void setCheckerRes(TenantQuotaCheckerResponse checkerRes) {
		this.checkerRes = checkerRes;
	}

	public TenantBeanV2 getTenantBean() {
		return tenantBean;
	}

	public void setTenantBean(TenantBeanV2 tenantBean) {
		this.tenantBean = tenantBean;
	}
}
