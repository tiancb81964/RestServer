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
import com.asiainfo.ocmanager.exception.OcmanagerRuntimeException;
import com.asiainfo.ocmanager.persistence.test.TestDBConnectorFactory;
import com.google.common.collect.Maps;

/**
 * 
 * @author zhaoyim
 *
 */
public class OsClusterIni {

	private static final Logger logger = LoggerFactory.getLogger(OsClusterIni.class);

	private static String PATH;
	private static Map<String, Properties> conf;

	static {
		try {
			// String base = OsClusterIni.class.getResource("/").getPath() +
			// ".." + File.separator;
			// PATH = base + "conf" + File.separator + "osclusters.ini";
			String base = new TestDBConnectorFactory().getClass().getResource("/").getPath();
			PATH = base.substring(0, base.length() - 4) + "WebContent/WEB-INF/conf/osclusters.ini";
			if (!new File(PATH).exists()) {
				logger.error("OsClusterIni: File not found: " + PATH);
				throw new OcmanagerRuntimeException("File not found: " + PATH);
			}
		} catch (Exception e) {
			logger.error("OsClusterIni Exception while init class: ", e);
			throw new OcmanagerRuntimeException("Exception while init class: ", e);
		}
	}

	public static Map<String, Properties> getConf() {
		if (conf == null) {
			synchronized (OsClusterIni.class) {
				if (conf == null) {
					new OsClusterIni();
				}
			}
		}
		return conf;
	}

	private OsClusterIni() {
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
			logger.error("OsClusterIni: init: Illegal format in file: " + PATH, e);
			throw new RuntimeException("Illegal format in file: " + PATH, e);
		} catch (IOException e) {
			logger.error("OsClusterIni: init: Error while parsing config file: " + PATH, e);
			throw new RuntimeException("Error while parsing config file: " + PATH, e);
		}
	}

}
