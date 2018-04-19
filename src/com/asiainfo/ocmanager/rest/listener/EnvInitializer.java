package com.asiainfo.ocmanager.rest.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.security.AuthenticatorManager;
import com.asiainfo.ocmanager.service.broker.utils.ResourcePeekerFactory;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.tenant.management.LifetimeManager;

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
		LOG.info("EnvInitializer initializing ...");
		AuthenticatorManager.getInstance().start();
		ServiceClientPool.getInstance();
		LifetimeManager.getInstance().start();
		try {
			Class.forName(ResourcePeekerFactory.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.info("EnvInitializer re-initializing ...");
		
	}
}
