package com.asiainfo.ocmanager.service.broker;

import java.util.List;
import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.bean.CustomEvnBean;

/**
 * Broker adapter should implements this interfaces
 * 
 * @author Ethan
 *
 */
public interface BrokerAdapterInterface extends Broker {

	/**
	 * Get broker type
	 * 
	 * @return
	 */
	public String getType();

	/**
	 * Generate image url
	 * 
	 * @return
	 */
	public String getImage();

	/**
	 * Generate broker environment kvs
	 * 
	 * @return
	 */
	public Map<String, String> getEnv();

	/**
	 * Return the environments which need be customized by end user
	 * 
	 * @return
	 */
	public List<CustomEvnBean> customEnvs();

	/**
	 * Get corresponded cLuster of current broker
	 * 
	 * @return
	 */
	public Cluster getCluster();
}
