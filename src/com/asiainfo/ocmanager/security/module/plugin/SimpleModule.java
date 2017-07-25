package com.asiainfo.ocmanager.security.module.plugin;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.security.module.SecurityModule;

/**
 * Simple authentication, basically doing nothing
 * @author EthanWang
 *
 */
public class SimpleModule implements SecurityModule {
	private static final Logger LOG = Logger.getLogger(SimpleModule.class);

	@Override
	public void login() throws Exception {
		LOG.info("This is simple security module.");

	}

	@Override
	public void relogin() throws Exception {
		LOG.info("This is simple security module.");
	}

}
