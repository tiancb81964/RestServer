package com.asiainfo.ocmanager.rest.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.security.AuthScheduller;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * Initialize env at start-up
 * 
 * @author EthanWang
 *
 */
public class EnvInitializer implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(EnvInitializer.class);
	private Properties conf = ServerConfiguration.getConf();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("EnvInitializer start.");
		// auth scheduller
		AuthScheduller.schedule(Long.valueOf(conf.getProperty(Constant.SECURITY_PERIOD, "85800")),
				AuthScheduller.DELAY_SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AuthScheduller.stop();
	}
}
