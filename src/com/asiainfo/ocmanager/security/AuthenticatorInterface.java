package com.asiainfo.ocmanager.security;

import com.asiainfo.ocmanager.service.client.v2.Delegator;

/**
 * Authenticator interface. Authenticator is actor which is 
 * responsible for doing authentication actions. And each authenticator
 * acts as a identity of a user(which could be represented by {@link Delegator})
 * @author Ethan
 *
 */
public interface AuthenticatorInterface {
	
	/**
	 * login from subject
	 */
	public void login();
	
	/**
	 * relogin from subject
	 */
	public void relogin();

	/**
	 * Get the subject which is binded to current authenticator
	 * @return
	 */
	public Delegator getDelegator();
}
