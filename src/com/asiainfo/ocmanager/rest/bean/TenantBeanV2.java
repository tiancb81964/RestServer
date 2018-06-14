package com.asiainfo.ocmanager.rest.bean;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.Tenant;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantBeanV2 {

	private Tenant cmInfo;
	private Map<String, String> osClustersInfo = new HashMap<String, String>();

	public TenantBeanV2() {

	}

	public TenantBeanV2(Tenant cmInfo, Map<String, String> osClustersInfo) {
		this.cmInfo = cmInfo;
		this.osClustersInfo = osClustersInfo;
	}

	public Tenant getCmInfo() {
		return cmInfo;
	}

	public void setCmInfo(Tenant cmInfo) {
		this.cmInfo = cmInfo;
	}

	public Map<String, String> getOsClustersInfo() {
		return osClustersInfo;
	}

	public void setOsClustersInfo(Map<String, String> osClustersInfo) {
		this.osClustersInfo = osClustersInfo;
	}

	@Override
	public String toString() {
		return "TenantBean [cmInfo: " + cmInfo.toString() + "; osClustersInfo: " + osClustersInfo + "]";
	}

}