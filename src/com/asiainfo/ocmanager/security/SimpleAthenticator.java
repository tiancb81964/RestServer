package com.asiainfo.ocmanager.security;

import java.util.Properties;

import com.asiainfo.ocmanager.service.client.v2.Delegator;

/**
 * Insecure environment authenticator
 * @author Ethan
 *
 */
public class SimpleAthenticator extends BaseAuthenticator implements AuthenticatorInterface {

	public SimpleAthenticator(Properties serviceConfig) {
		super(serviceConfig);
	}

	@Override
	public void login() {
		
	}

	@Override
	public void relogin() {
		
	}

	@Override
	public Delegator getDelegator() {
		return new Delegator(null);
	}
}
