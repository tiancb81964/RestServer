package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * New server configuration
 * 
 * @author Ethan
 *
 */
public class ServicesIni {
	private static final Logger LOG = LoggerFactory.getLogger(ServicesIni.class);
	private static String PATH;
	private Map<String, Section> conf;
	private static ServicesIni instance;

	static {
		try {
			String base = ServicesIni.class.getResource("/").getPath() + ".." + File.separator;
			PATH = base + "conf" + File.separator + "services.ini";
			if (!new File(PATH).exists()) {
				LOG.error("File not found: " + PATH);
				throw new RuntimeException("File not found: " + PATH);
			}
		} catch (Exception e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}

	public static ServicesIni getInstance() {
		if (instance == null) {
			synchronized (ServicesIni.class) {
				if (instance == null) {
					instance = new ServicesIni();
				}
			}
		}
		return instance;
	}

	private ServicesIni() {
		init();
	}

	/**
	 * Get all configurations grouping by services.
	 * 
	 * @return
	 */
	public Map<String, Properties> getAllProperties() {
		Map<String, Properties> map = Maps.newHashMap();
		this.conf.forEach((k, v) -> {
			map.put(k, getProperties(k));
		});
		return map;
	}

	private void init() {
		conf = Maps.newHashMap();
		try {
			Wini file = new Wini(new File(PATH));
			for (Entry<String, Section> en : file.entrySet()) {
				conf.put(en.getKey(), en.getValue());
			}
		} catch (InvalidFileFormatException e) {
			LOG.error("Illegal format in file: " + PATH, e);
			throw new RuntimeException("Illegal format in file: " + PATH, e);
		} catch (IOException e) {
			LOG.error("Error while parsing config file: " + PATH, e);
			throw new RuntimeException("Error while parsing config file: " + PATH, e);
		}
	}

	public static void main(String[] args) throws InvalidFileFormatException, IOException {
		String v = ServicesIni.getInstance().getProperty("newSection", "kkk");
		System.out.println("end of mian: " + v);
	}

	/**
	 * Get properties of certain section
	 * 
	 * @param section
	 * @return
	 */
	public Properties getProperties(String section) {
		Section collection = conf.get(section);
		Preconditions.checkNotNull(collection, "Section not exist: " + section);
		Properties prop = new Properties();
		for (Entry<String, String> en : collection.entrySet()) {
			prop.setProperty(en.getKey(), en.getValue());
		}
		return prop;
	}

	/**
	 * Get property by the specified key of specified section
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public String getProperty(String section, String key) {
		return conf.get(section).get(key);
	}

}
