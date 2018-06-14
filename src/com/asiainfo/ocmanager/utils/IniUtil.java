package com.asiainfo.ocmanager.utils;

import java.util.Properties;
import java.util.Map.Entry;

import org.ini4j.Profile.Section;

/**
 * 
 * @author zhaoyim
 *
 */
public class IniUtil {

	/**
	 * change section to properties
	 * @param value
	 * @return
	 */
	public static Properties toProperties(Section value) {
		Properties props = new Properties();
		for (Entry<String, String> entry : value.entrySet()) {
			props.put(entry.getKey(), entry.getValue());
		}
		return props;
	}

}
