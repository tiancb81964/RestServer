package com.asiainfo.ocmanager.rest.resource.utils.model;

import com.asiainfo.ocmanager.rest.bean.TenantBean;

public class TenantResponse {

	private TenantQuotaCheckerResponse checkerRes;
	private TenantBean tenantBean;

	public TenantQuotaCheckerResponse getCheckerRes() {
		return checkerRes;
	}

	public void setCheckerRes(TenantQuotaCheckerResponse checkerRes) {
		this.checkerRes = checkerRes;
	}

	public TenantBean getTenantBean() {
		return tenantBean;
	}

	public void setTenantBean(TenantBean tenantBean) {
		this.tenantBean = tenantBean;
	}
}
