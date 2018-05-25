package com.asiainfo.ocmanager.service.client.v2;

import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.security.AuthenticatorManager;

public class HbaseClient extends ServiceClient{
	private static final Logger LOG = LoggerFactory.getLogger(HbaseClient.class);
	private Connection conn;

	protected HbaseClient(String serviceName, Delegator subject) {
		super(serviceName, subject);
		try {
			init();
		} catch (Exception e) {
			LOG.error("Exception while init hbase client: ", e);
			throw new RuntimeException("Exception while init hbase client: ", e);
		}
	}

	private void init() throws Exception {
		conn = doPrivileged(new SomeAction<Connection>() {

			@Override
			public Connection run() throws Exception {
				return conn = ConnectionFactory.createConnection(getconf());
			}

			private Configuration getconf() {
				Properties props = getServiceConfigs();
				Configuration conf = new Configuration();
				for (Entry<Object, Object> p : props.entrySet()) {
					conf.set(String.valueOf(p.getKey()), String.valueOf(p.getValue()));
				}
				return conf;				
			}
		});
	}
	
	public static void main(String[] args) {
		try {
			Delegator dd = AuthenticatorManager.getInstance().getAuthenticator("hbase").getDelegator();
			Admin admin = new HbaseClient("hbase", dd).createAdmin();
			boolean exist = admin.tableExists(TableName.valueOf("demo001"));
			System.out.println(">>> exist: " + exist);
			NamespaceDescriptor des = admin.getNamespaceDescriptor("hbase");
			String max = des.getConfigurationValue("hbase.namespace.quota.maxtables");
			System.out.println(">>>end of main: " + max);
		} catch (Exception e) {
			LOG.error("Exception while main(): ", e);
		}
	}
	
	/**
	 * Get admin client of hbase. Create admin whenever necessary
	 * and the returned admin should be closed after finished.
	 * @return
	 * @throws Exception 
	 */
	public Admin createAdmin() throws Exception{
		return doPrivileged(new SomeAction<Admin>() {

			@Override
			public Admin run() throws Exception {
				return conn.getAdmin();
			}
		});
	}
}
