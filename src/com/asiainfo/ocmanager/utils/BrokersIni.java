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

import com.google.common.collect.Maps;

/**
 * Broker properties
 * 
 * @author Ethan
 *
 */
public class BrokersIni {
	private static final Logger LOG = LoggerFactory.getLogger(BrokersIni.class);
	private static String PATH;
	private Map<String, Properties> conf;
	private static BrokersIni instance;

	static {
		try {
			String base = BrokersIni.class.getResource("/").getPath() + ".." + File.separator;
			PATH = base + "conf" + File.separator + "brokers.ini";
			if (!new File(PATH).exists()) {
				LOG.error("File not found: " + PATH);
				throw new RuntimeException("File not found: " + PATH);
			}
		} catch (Exception e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}

	public static BrokersIni getInstance() {
		if (instance == null) {
			synchronized (BrokersIni.class) {
				if (instance == null) {
					instance = new BrokersIni();
				}
			}
		}
		return instance;
	}

	private BrokersIni() {
		init();
	}

	private void init() {
		conf = Maps.newHashMap();
		try {
			Wini file = new Wini(new File(PATH));
			for (Entry<String, Section> en : file.entrySet()) {
				conf.put(en.getKey(), IniUtil.toProperties(en.getValue()));
			}
		} catch (InvalidFileFormatException e) {
			LOG.error("Illegal format in file: " + PATH, e);
			throw new RuntimeException("Illegal format in file: " + PATH, e);
		} catch (IOException e) {
			LOG.error("Error while parsing config file: " + PATH, e);
			throw new RuntimeException("Error while parsing config file: " + PATH, e);
		}
	}

	/**
	 * Get cluster of specified name
	 * 
	 * @param section
	 * @return
	 */
	public Properties getBroker(String brokername) {
		Properties broker = conf.get(brokername);
		return broker;
	}

}
