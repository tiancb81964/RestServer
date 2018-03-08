package com.asiainfo.ocmanager.service.client.v2;

/**
 * Hive client
 * @author Ethan
 *
 */
public class HiveClient extends HDFSClient{

	protected HiveClient(String serviceName, Delegator subject) {
		super(serviceName, subject);
	}

}
