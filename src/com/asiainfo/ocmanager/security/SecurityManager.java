package com.asiainfo.ocmanager.security;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.security.module.SecurityModule;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * To do security authentication. Different security plugins (eg. Kerberos, Simple)
 * can be applied. Manager will use whichever is configured in <code>server.properties</code>.
 * Plugins should implement {@link SecurityModule} interface.
 * @author EthanWang
 *
 */
public class SecurityManager {
	private static final Logger LOG = Logger.getLogger(SecurityManager.class);
	private static final String MODULE = "server.security.mudule.class";
	private static SecurityManager instance;
	private SecurityModule module;
	
	public static SecurityManager getInstance(){
		if (instance == null) {
			synchronized(SecurityManager.class){
				if (instance == null) {
					instance = new SecurityManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Do login
	 */
	public void login(){
		try {
			this.module.login();
			LOG.info("Security module login successful!");
		} catch (Exception e) {
			LOG.error("login failed. ", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Relogin action
	 */
	public void relogin(){
		try {
			this.module.relogin();
			LOG.info("Security module relogin successful!");
		} catch (Exception e) {
			LOG.error("relogin failed. ", e);
			throw new RuntimeException(e);
		}
	}
	
	private SecurityManager(){
		initModule();
	}
	
	private void initModule() {
		try {
			String clz = ServerConfiguration.getConf().getProperty(MODULE).trim();
			Class<?> clazz = Class.forName(clz);
			module = (SecurityModule) clazz.newInstance();
		} catch (Exception e) {
			LOG.error("Error init security module: " + ServerConfiguration.getConf().getProperty(MODULE), e);
			throw new RuntimeException("Error init security module: " + ServerConfiguration.getConf().getProperty(MODULE), e);
		}
	}
}
