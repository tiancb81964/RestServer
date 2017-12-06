package com.asiainfo.ocmanager.service.broker.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;
import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.google.common.collect.Maps;

import scala.collection.mutable.StringBuilder;

/**
 * Factory to create service resource broker.
 * 
 * @author EthanWang
 *
 */
public class ResourcePeekerFactory {
	private static final Logger LOG = Logger.getLogger(ResourcePeekerFactory.class);
	// failed instance will not be added into mapping
	private static Map<ServiceType, Class<ResourcePeeker>> mapping = Maps.newHashMap();
	
	static {
		try {
			initPeekers();
			LOG.info("Available peekers been found: " + mapping);
		} catch (Throwable  e) {
			LOG.error("Error while init class: ", e);
			throw new RuntimeException("Error while init class: ", e);
		}
	}

	/**
	 * Create a peeker by specified class
	 * 
	 * @param clz
	 * @param totalQuota
	 * @return
	 * @throws Exception
	 */
	public static ResourcePeeker getPeeker(Class<? extends BaseResourcePeeker> clz) {
		try {
			BaseResourcePeeker broker = clz.newInstance();
			return broker;
		} catch (Exception e) {
			LOG.error("Create peeker failed: " + clz.getName(), e);
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		ResourcePeeker p = getPeeker(ServiceType.greenpulm);
		System.out.println("end of main: " + p);
	}

	/**
	 * Get all classes that implements {@linkplain ResourcePeeker} interface.
	 * Peekers that failed to initialized will not be in mapping
	 * 
	 * @return
	 */
	private static void initPeekers() {
		String absoluteBase = ResourcePeeker.class.getResource("/").getPath();
		String pkg = ResourcePeeker.class.getPackage().getName();
		String relativePath = pkg.replace(".", File.separator);
		String pluginDir = new StringBuilder(absoluteBase + File.separator + relativePath).append(File.separator)
				.append("plugin").toString();
		File dir = new File(pluginDir);
		if (!dir.exists() || !dir.isDirectory()) {
			LOG.error("Directory not found: " + dir);
			throw new RuntimeException("Directory not found: " + dir);
		}
		dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".class")) {
					try {
						String clzName = pkg + ".plugin." + name.substring(0, name.length() - 6);
						@SuppressWarnings("unchecked")
						Class<ResourcePeeker> clz = (Class<ResourcePeeker>) Class.forName(clzName);
						if (ResourcePeeker.class.isAssignableFrom(clz)) {
							try {
								ResourcePeeker ins = clz.newInstance();
								mapping.put(ins.getType(), clz);
								return true;
							} catch (Exception e) {
								// failed instance will not be added into mapping
								LOG.error("Error while new instance by class: " + clz, e);
							}
						}
					} catch (ClassNotFoundException e) {
						LOG.error("Class not found: ", e);
						throw new RuntimeException("Class not found: ", e);
					}
				}
				return false;
			}
		});
	}

	/**
	 * Create a new peeker by specified service type
	 * @param type
	 * @return
	 */
	public static ResourcePeeker getPeeker(ServiceType type) {
		if (mapping.containsKey(type)) {
			try {
				return mapping.get(type).newInstance();
			} catch (Exception e) {
				LOG.error("Error while new instance by class: " + mapping.get(type), e);
				throw new RuntimeException("Error while new instance by class: " + mapping.get(type), e);
			}
		}
		// peekers that failed to initialize will not be in mapping
		LOG.warn("Trying to re-initialize all peekers due to lack of service type: " + type.serviceType());
		initPeekers();
		
		if (mapping.containsKey(type)) {
			try {
				return mapping.get(type).newInstance();
			} catch (Exception e) {
				LOG.error("Error while new instance by class: " + mapping.get(type), e);
				throw new RuntimeException("Error while new instance by class: " + mapping.get(type), e);
			}
		}
		LOG.error("Peeker of service type can not be initialized: " + type.serviceType());
		throw new RuntimeException("Peeker of service type can not be initialized: " + type.serviceType());
	}

}
