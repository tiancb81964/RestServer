package com.asiainfo.ocmanager.service.broker;

import java.util.List;

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
	public ResourcePeeker peekOn(List<String> resources);

	/**
	 * Get total quota.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Long getTotalQuota(String type, String name) throws Exception;

	/**
	 * Get used quota.
	 * 
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public Long getUsedQuota(String key, String name) throws Exception;

	/**
	 * Get free quota.
	 * 
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public Long getFreeQuota(String key, String name) throws Exception;
}
