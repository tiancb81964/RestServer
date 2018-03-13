package com.asiainfo.ocmanager.security;

import java.util.Properties;

import org.apache.hadoop.security.SaslRpcServer.AuthMethod;
import org.apache.hadoop.security.UserGroupInformation;

import com.asiainfo.ocmanager.service.client.v2.Delegator;

/**
 * Insecure environment authenticator
 * 
 * @author Ethan
 *
 */
public class SimpleAthenticator extends BaseAuthenticator implements AuthenticatorInterface {
	private Delegator delegator;

	public SimpleAthenticator(Properties serviceConfig) {
		super(serviceConfig);
		UserGroupInformation ugi = UserGroupInformation.createRemoteUser(System.getProperty("user.name", "root"),
				AuthMethod.SIMPLE);
		delegator = new Delegator(ugi);
	}

	@Override
	public void login() {

	}

	@Override
	public void relogin() {

	}

	@Override
	public Delegator getDelegator() {
		return delegator;
	}
}
