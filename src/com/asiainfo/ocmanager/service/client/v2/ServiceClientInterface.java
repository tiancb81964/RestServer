package com.asiainfo.ocmanager.service.client.v2;

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
}
