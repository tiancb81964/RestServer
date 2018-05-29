package com.asiainfo.ocmanager.security;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.security.SaslRpcServer.AuthMethod;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.service.client.v2.Delegator;

/**
 * Insecure environment authenticator
 * 
 * @author Ethan
 *
 */
public class SimpleAuthenticator extends BaseAuthenticator implements AuthenticatorInterface {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleAuthenticator.class);
	private final Delegator delegator;

	public SimpleAuthenticator(Properties serviceConfig) {
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
			LOG.error("IOException while login(): ", e);
			throw new RuntimeException("login exception for delegator: " + delegator);
		}
	}

	@Override
	public void relogin() {
		try {
			delegator.refreshCredential();
		} catch (IOException e) {
			LOG.error("IOException while relogin(): ", e);
			throw new RuntimeException("relogin exception for delegator: " + delegator);
		}
	}

	@Override
	public Delegator getDelegator() {
		return delegator;
	}
}
