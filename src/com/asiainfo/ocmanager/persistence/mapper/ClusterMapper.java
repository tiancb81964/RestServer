package com.asiainfo.ocmanager.persistence.mapper;

import com.asiainfo.ocmanager.persistence.model.Cluster;

/**
 * 
 * @author zhaoyim
 *
 */

public interface ClusterMapper {

	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	public Cluster getClusterByName(String clustername);
	
}
