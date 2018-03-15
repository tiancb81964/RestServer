package com.asiainfo.ocmanager.security;

import java.io.IOException;
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
	private final Delegator delegator;

	public SimpleAthenticator(Properties serviceConfig) {
		super(serviceConfig);
		UserGroupInformation ugi = UserGroupInformation.createRemoteUser(System.getProperty("user.name", "root"),
				AuthMethod.SIMPLE);
		delegator = new Delegator(ugi);
	}

	@Override
	public void login() {
		try {
			delegator.refreshCredential();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("login exception for delegator: " + delegator);
		}
	}

	@Override
	public void relogin() {
		try {
			delegator.refreshCredential();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("relogin exception for delegator: " + delegator);
		}
	}

	@Override
	public Delegator getDelegator() {
		return delegator;
	}
}
