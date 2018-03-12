package com.asiainfo.ocmanager.service.client.v2;

import java.util.Properties;

public interface ServiceClientInterface {

	/**
	 * Priviledge action
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public <T> T doPrivileged(SomeAction<T> action) throws Exception;

	/**
	 * Return the name of binded service
	 * @return
	 */
	public String getServiceName();
	
	/**
	 * Return the client class of current serviceClient
	 * @return
	 */
	public Class<? extends ServiceClient> getClientClass();
	
	/**
	 * Get configs of this client from file <code>../conf/services.ini</code>
	 * @return
	 */
	public Properties getServiceConfigs();
}
