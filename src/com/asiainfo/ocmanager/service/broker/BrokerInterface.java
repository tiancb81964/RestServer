package com.asiainfo.ocmanager.service.broker;

import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.Cluster;

/**
 * Broker adapter should implements this interfaces
 * @author Ethan
 *
 */
public interface BrokerInterface extends Broker{

	/**
	 * broker type
	 * @return
	 */
	public String getType();
	
	/**
	 * get image url
	 * @return
	 */
	public String getImage();
	
	/**
	 * get broker environment kvs
	 * @return
	 */
	public Map<String, String> getEnv();
	
	/**
	 * get cLuster
	 * @return
	 */
	public Cluster getCluster();
}
