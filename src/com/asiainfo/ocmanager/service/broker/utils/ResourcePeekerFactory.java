package com.asiainfo.ocmanager.service.broker.utils;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;

/**
 * Factory to create service resource broker.
 * @author EthanWang
 *
 */
public class ResourcePeekerFactory {
	private static final Logger LOG = Logger.getLogger(ResourcePeekerFactory.class);
	
	/**
	 * totalQuota is a table of < resourceType, resourceName, value >
	 * @param clz
	 * @param totalQuota
	 * @return
	 * @throws Exception
	 */
	public static ResourcePeeker getQuotaBroker(Class<? extends BaseResourcePeeker> clz){
		try {
			BaseResourcePeeker broker = clz.newInstance();
			return broker;
		} catch (Exception e) {
			LOG.error("Create broker failed: " + clz.getName(), e);
			throw new RuntimeException(e);
		}
	}

}
