package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class SSOConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(SSOConfiguration.class);
	private static Properties conf;

	public static Properties getConf() {
		if (conf == null) {
			synchronized (SSOConfiguration.class) {
				if (conf == null) {
					new SSOConfiguration();
				}
			}
		}
		return conf;
	}

	private SSOConfiguration() {
		loadConf();
	}

	private void loadConf() {
		InputStream inputStream = null;
		try {
			String base = this.getClass().getResource("/").getPath() + ".." + File.separator;
			String confpath = base + "conf" + File.separator + "sso.properties";
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
