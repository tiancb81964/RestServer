package com.asiainfo.ocmanager.service.client;
/**
 * Used to create cluster client
 * @author Ethan
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.rest.resource.persistence.ClusterPersistenceWrapper;
import com.asiainfo.ocmanager.service.client.RangerClient.UserExistedException;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Cluster-related operations
 * @author Ethan
 *
 */
public class ClusterClientFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ClusterClientFactory.class);
	private static Table<String, Class<?>, Object> clients = HashBasedTable.create(); // clusterName, clientType,

	public static RangerClient getRanger(String clusterName) {
		if (clients.contains(clusterName, RangerClient.class)) {
			return (RangerClient) clients.get(clusterName, RangerClient.class);
		}
		Cluster cluster = ClusterPersistenceWrapper.getClusterByName(clusterName);
		RangerClient ranger = new RangerClient(cluster);
		synchronized (clients) {
			clients.put(clusterName, RangerClient.class, ranger);
		}
		return ranger;
	}

	public static AmbariClient getAmbari(String clusterName) {
		if (clients.contains(clusterName, AmbariClient.class)) {
			return (AmbariClient) clients.get(clusterName, AmbariClient.class);
		}
		Cluster cluster = ClusterPersistenceWrapper.getClusterByName(clusterName);
		AmbariClient ambari = new AmbariClient(cluster); 
		synchronized (clients) {
			clients.put(clusterName, AmbariClient.class, ambari);
		}
		return ambari;
	}
	
	public static void main(String[] args) {
		RangerClient ranger = ClusterClientFactory.getRanger("ochadoop_testcluster");
		User user = new User();
		user.setUsername("ethanwang1");
		try {
			ranger.addUser(user);
		} catch (UserExistedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error("Exception while main(): ", e);
		}
	}
}
