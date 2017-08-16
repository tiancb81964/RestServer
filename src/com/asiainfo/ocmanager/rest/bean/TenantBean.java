package com.asiainfo.ocmanager.rest.bean;

import com.asiainfo.ocmanager.persistence.model.Tenant;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantBean {

	private Tenant databaseInfo;
	private String dataFoundryInfo;

	public TenantBean() {
	}

	public TenantBean(Tenant databaseInfo, String dataFoundryInfo) {
		this.databaseInfo = databaseInfo;
		this.dataFoundryInfo = dataFoundryInfo;
	}

	public Tenant getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(Tenant databaseInfo) {
		this.databaseInfo = databaseInfo;
	}

	public String getDataFoundryInfo() {
		return dataFoundryInfo;
	}

	public void setDataFoundryInfo(String dataFoundryInfo) {
		this.dataFoundryInfo = dataFoundryInfo;
	}

	@Override
	public String toString() {
		return "TenantBean [databaseInfo: " + databaseInfo.toString() + "; dataFoundryInfo: " + dataFoundryInfo + "]";
	}

}
