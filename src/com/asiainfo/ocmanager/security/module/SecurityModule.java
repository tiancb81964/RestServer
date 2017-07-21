package com.asiainfo.ocmanager.security.module;

/**
 * Any custom security module should implements this interface.
 * @author EthanWang
 *
 */
public interface SecurityModule {
	
	/**
	 * Do login.
	 * @throws Exception
	 */
	public void login() throws Exception;
	
	/**
	 * Relogin action.
	 * @throws Exception
	 */
	public void relogin() throws Exception;
	
}
