package com.asiainfo.ocmanager.rest.bean;

import java.util.List;

/**
 * 
 * @author zhaoyim
 *
 */
public class AccessedClusterBean {

	private List<String> accessedCluster;

	public List<String> getAccessedCluster() {
		return accessedCluster;
	}

	public void setAccessedCluster(List<String> accessedCluster) {
		this.accessedCluster = accessedCluster;
	}

	@Override
	public String toString() {
		return "AccessedClusterBean [accessedCluster=" + accessedCluster + "]";
	}

}
