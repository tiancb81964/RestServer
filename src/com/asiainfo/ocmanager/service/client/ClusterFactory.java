package com.asiainfo.ocmanager.service.client;
/**
 * Used to create cluster client
 * @author Ethan
 *
 */

import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.service.client.RangerClient.UserExistedException;
import com.asiainfo.ocmanager.utils.ClustersIni;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Cluster-related operations
 * @author Ethan
 *
 */
public class ClusterFactory {
	private static Table<String, Class<?>, Object> clients = HashBasedTable.create(); // clusterName, clientType,

	public static RangerClient getRanger(String clusterName) {
		if (clients.contains(clusterName, RangerClient.class)) {
			return (RangerClient) clients.get(clusterName, RangerClient.class);
		}
		RangerClient ranger = new RangerClient(ClustersIni.getInstance().getCluster(clusterName));
		synchronized (clients) {
			clients.put(clusterName, RangerClient.class, ranger);
		}
		return ranger;
	}

	public static AmbariClient getAmbari(String clusterName) {
		if (clients.contains(clusterName, AmbariClient.class)) {
			return (AmbariClient) clients.get(clusterName, AmbariClient.class);
		}
		AmbariClient ambari = new AmbariClient(ClustersIni.getInstance().getCluster(clusterName)); 
		synchronized (clients) {
			clients.put(clusterName, AmbariClient.class, ambari);
		}
		return ambari;
	}
	
	public static void main(String[] args) {
		RangerClient ranger = ClusterFactory.getRanger("ochadoop_testcluster");
		User user = new User();
		user.setUsername("ethanwang1");
		try {
			ranger.addUser(user);
		} catch (UserExistedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
