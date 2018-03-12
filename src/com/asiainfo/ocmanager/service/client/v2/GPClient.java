package com.asiainfo.ocmanager.service.client.v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Greenplum client.
 * 
 * @author EthanWang
 *
 */
public class GPClient extends ServiceClient{
	private Map<String, Connection> map = new ConcurrentHashMap<>();
	private static String prefix_conn;
	private static String user;
	private static String password;
	
	public GPClient(String serviceName, Delegator subject) {
		super(serviceName, subject);
		try {
			Class.forName("org.postgresql.Driver");
			user = this.serviceConfig.getProperty("oc.greenplum.user").trim();
			password = this.serviceConfig.getProperty("oc.greenplum.password").trim();
			String host = this.serviceConfig.getProperty("oc.greenplum.server.host").trim();
			String port = this.serviceConfig.getProperty("oc.greenplum.server.port").trim();
			prefix_conn = "jdbc:postgresql://" + host + ":" + port + "/";

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get connection of specified database.
	 * 
	 * @param dbName
	 * @return
	 */
	public Connection getConnection(String dbName) {
		if (this.map.containsKey(dbName) && !closed(dbName)) {
			return map.get(dbName);
		}
		try {
			cache(dbName, create(dbName));
			return map.get(dbName);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private boolean closed(String dbName) {
		try {
			return map.get(dbName).isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	private Connection create(String dbName) throws SQLException {
		Connection conn = DriverManager.getConnection(prefix_conn + dbName, user, password);
		return conn;
	}

	private void cache(String db, Connection conn) {
		this.map.put(db, conn);
	}

}
