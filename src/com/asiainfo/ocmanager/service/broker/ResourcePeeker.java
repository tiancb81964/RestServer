package com.asiainfo.ocmanager.service.broker;

import java.util.List;

import com.asiainfo.ocmanager.service.broker.utils.ResourcePeekerFactory;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;

/**
 * Interface used to get service quota.
 * 
 * @author EthanWang
 *
 */
public interface ResourcePeeker extends Broker {

	/**
	 * Have a peek at the specified resources.
	 * 
	 * @param resources
	 * @return
	 */
	public ResourcePeeker peekOn(List<String> resources) throws Exception;

	/**
	 * Get total quota.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Long getTotalQuota(String type, String name);

	/**
	 * Get used quota.
	 * 
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public Long getUsedQuota(String key, String name);

	/**
	 * Get free quota.
	 * 
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public Long getFreeQuota(String key, String name);

	/**
	 * Return specific resource type names.
	 * 
	 * @return
	 */
	public List<String> resourceTypes();

	/**
	 * Get the corresponding resources of specified resourceType.
	 * 
	 * @param resourceType
	 * @return
	 */
	public List<String> getResourcesByType(String resourceType);
	
	/**
	 * Return the client type of current peeker. Used to
	 * find peeker by {@link ResourcePeekerFactory}
	 * @return
	 */
	public Class<? extends ServiceClient> getClientClass();
	
}
