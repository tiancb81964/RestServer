package com.asiainfo.ocmanager.utils;

import java.util.Map.Entry;
import java.util.Properties;

import org.ini4j.Profile.Section;

/**
 * Cluter config, corespponding to the section in <code>../conf/clusters.ini</code> file.
 * @author Ethan
 *
 */
public class ClusterConfig {
	private Properties prop;
	private String clusterName;
	
	public ClusterConfig(String name, Section section) {
		this.clusterName = name;
		prop = new Properties();
		for(Entry<String, String> en : section.entrySet()) {
			prop.setProperty(en.getKey(), en.getValue());
		}
	}

	@Override
	public String toString() {
		return "ClusterName=" + clusterName;
	}
	
	public String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	public Properties getProperties() {
		return prop;
	}
}
