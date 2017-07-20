package com.asiainfo.ocmanager.service.broker.utils;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.ResourceBroker;
import com.asiainfo.ocmanager.service.broker.imp.BaseResourceBroker;

/**
 * Factory to create service resource broker.
 * @author EthanWang
 *
 */
public class ResourceBrokerFactory {
	private static final Logger LOG = Logger.getLogger(ResourceBrokerFactory.class);
	
	/**
	 * totalQuota is a table of < resourceType, resourceName, value >
	 * @param clz
	 * @param totalQuota
	 * @return
	 * @throws Exception
	 */
	public static ResourceBroker getQuotaBroker(Class<? extends BaseResourceBroker> clz){
		try {
			BaseResourceBroker broker = clz.newInstance();
			return broker;
		} catch (Exception e) {
			LOG.error("Create broker failed: " + clz.getName(), e);
			throw new RuntimeException(e);
		}
	}

}
