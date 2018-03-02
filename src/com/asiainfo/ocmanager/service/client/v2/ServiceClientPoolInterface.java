package com.asiainfo.ocmanager.service.client.v2;

import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool.ClientNotFoundException;

public interface ServiceClientPoolInterface <T> {
	
	public void register(T t);
	
	public void unregister(T t);
	
	public T getClient(String serviceName) throws ClientNotFoundException;

}
