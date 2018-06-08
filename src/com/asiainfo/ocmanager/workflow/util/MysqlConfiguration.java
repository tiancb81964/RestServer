package com.asiainfo.ocmanager.workflow.util;

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
public class MysqlConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(MysqlConfiguration.class);
	private static Properties conf;

	public static Properties getConf() {
		if (conf == null) {
			synchronized (MysqlConfiguration.class) {
				if (conf == null) {
					new MysqlConfiguration();
				}
			}
		}
		return conf;
	}

	private MysqlConfiguration() {
		loadConf();
	}

	private void loadConf() {
		InputStream inputStream = null;
		try {
//			String base = this.getClass().getResource("/").getPath() + ".." + File.separator;
			String base = this.getClass().getResource("/").getPath() + ".." + File.separator + "WebContent/WEB-INF/";
			String confpath = base + "conf" + File.separator + "mysql.properties";
			inputStream = new FileInputStream(new File(confpath));
			conf = new Properties();
			conf.load(inputStream);
		} catch (Exception e) {
			LOG.error("Exception while loadConf(): MysqlConfiguration: ", e);
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.error("IOException while loadConf(): MysqlConfiguration", e);
				}
			}
		}
	}
}
