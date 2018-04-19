package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Mail server properties.
 * @author EthanWang
 *
 */
public class MailServerProperties {
	private static Properties conf;
	
	public static Properties getConf(){
		if (conf == null) {
			synchronized(MailServerProperties.class){
				if (conf == null) {
					new MailServerProperties();
				}
			}
		}
		return conf;
	}
	
	private MailServerProperties(){
		loadConf();
	}

	private void loadConf() {
		InputStream inputStream = null;
		try {
			String base = this.getClass().getResource("/").getPath() + ".." + File.separator;
			String confpath = base + "conf" + File.separator + "mailserver.properties";
			inputStream = new FileInputStream(new File(confpath));
			conf = new Properties();
			conf.load(inputStream);	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
