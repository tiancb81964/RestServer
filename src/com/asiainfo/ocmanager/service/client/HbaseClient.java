package com.asiainfo.ocmanager.service.client;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.asiainfo.ocmanager.utils.HadoopConfiguration;

/**
 * Hbase client.
 * @author EthanWang
 *
 */
public class HbaseClient {
	private static HbaseClient instance;
	private Connection conn;
	
	public static HbaseClient getInstance(){
		if (instance == null) {
			synchronized(HbaseClient.class){
				if (instance == null) {
					instance =new HbaseClient();
				}
			}
		}
		return instance;
	}
	
	private HbaseClient(){
		try {
	        conn = ConnectionFactory.createConnection(HadoopConfiguration.getConf());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Init hbase client connection failed: ", e);
		}
	}
	
	/**
	 * Get admin client of hbase. Create admin whenever necessary
	 * and the returned admin should be closed after finished.
	 * @return
	 */
	public Admin createAdmin(){
		try {
			return conn.getAdmin();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
