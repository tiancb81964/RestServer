package com.asiainfo.ocmanager.service.client;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;

/**
 * HDFS client.
 * @author EthanWang
 *
 */
public class HDFSClient {
	private static final Logger LOG = Logger.getLogger(HDFSClient.class);
	private static FileSystem fs;

	public static FileSystem getFileSystem(){
		if (fs == null) {
			synchronized (HDFSClient.class) {
				if (fs == null) {
					try {
						fs = FileSystem.get(HadoopConfiguration.getConf());
					} catch (IOException e) {
						LOG.error("Init client error: ", e);
						throw new RuntimeException(e);
					}				
				}
			}
		}
		return fs;
	}
	
}
