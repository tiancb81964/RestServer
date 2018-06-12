package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RestServer configuration.
 * 
 * @author EthanWang
 *
 */
public class ETCDProperties {
	private static final Logger LOG = LoggerFactory.getLogger(ETCDProperties.class);
	private static Properties conf;

	public static Properties getConf() {
		if (conf == null) {
			synchronized (ETCDProperties.class) {
				if (conf == null) {
					new ETCDProperties();
				}
			}
		}
		return conf;
	}

	private ETCDProperties() {
		loadConf();
	}

	private void loadConf() {
		InputStream inputStream = null;
		try {
			String base = this.getClass().getResource("/").getPath() + ".." + File.separator;
			String confpath = base + "conf" + File.separator + "etcd.properties";
			inputStream = new FileInputStream(new File(confpath));
			conf = new Properties();
			conf.load(inputStream);
		} catch (Exception e) {
			LOG.error("Exception while loadConf(): ", e);
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.error("IOException while loadConf(): ", e);
				}
			}
		}
	}
}
