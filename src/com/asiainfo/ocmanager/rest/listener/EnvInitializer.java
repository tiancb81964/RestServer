package com.asiainfo.ocmanager.rest.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initialize env at start-up
 * 
 * @author EthanWang
 *
 */
public class EnvInitializer implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(EnvInitializer.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("EnvInitializer start.");
		// auth scheduller
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
