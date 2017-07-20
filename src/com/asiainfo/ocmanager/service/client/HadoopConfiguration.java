package com.asiainfo.ocmanager.service.client;

import org.apache.hadoop.conf.Configuration;

public class HadoopConfiguration {
	private static Configuration conf;
	
	public static Configuration getConf(){
		if (conf == null) {
			synchronized(conf){
				if (conf == null) {
					init();
				}
			}
		}
		return conf;
	}

	private static void init() {
		// TODO Auto-generated method stub
		conf = null;
	}

}
