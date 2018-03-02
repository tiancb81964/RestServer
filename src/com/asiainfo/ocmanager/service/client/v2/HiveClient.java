package com.asiainfo.ocmanager.service.client.v2;

import javax.security.auth.Subject;

/**
 * Hive client
 * @author Ethan
 *
 */
public class HiveClient extends HDFSClient{

	protected HiveClient(String serviceName, Subject subject) {
		super(serviceName, subject);
	}

}
