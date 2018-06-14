package com.asiainfo.ocmanager.service.broker.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.asiainfo.ocmanager.auth.LdapWrapper;
import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;
import com.asiainfo.ocmanager.service.client.AmbariClient;
import com.asiainfo.ocmanager.service.client.ClusterClientFactory;
import com.asiainfo.ocmanager.service.client.RangerClient;
import com.asiainfo.ocmanager.utils.BrokersIni;
import com.asiainfo.ocmanager.utils.ETCDProperties;
import com.asiainfo.ocmanager.utils.KerberosConfiguration;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class DPAdapter implements BrokerInterface {
	private Cluster cluster;

	public DPAdapter(Cluster cluster) {
		check(cluster);
		this.cluster = cluster;
	}

	private void check(Cluster cluster) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getAmbari_url()));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getAmbari_user()));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getAmbari_password()));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getCluster_name()));
	}

	public DPAdapter() {
	}

	@Override
	public String getType() {
		return "hadoop";
	}

	@Override
	public String getImage() {
		Properties props = BrokersIni.getInstance().getBroker(getType());
		return props.getProperty("image");
	}

	@Override
	public Map<String, String> getEnv() {
		AmbariClient ambari = ClusterClientFactory.getAmbari(this.cluster.getCluster_name());
		HashMap<String, String> envs = Maps.newHashMap();
		insert(ambari, envs);
		return envs;
	}

	private void insert(AmbariClient ambari, HashMap<String, String> map) {
		map.put("BROKER_USERNAME", "admin");
		map.put("BROKER_PASSWORD", "admin");
		map.put("BROKER_ID", cluster.getCluster_name());

		map.put("AMBARI_HOST", cluster.getAmbari_url());
		map.put("AMBARI_ADMIN_USER", cluster.getAmbari_user());
		map.put("AMBARI_ADMIN_PWD", cluster.getAmbari_password());

		map.put("ETCD_HOST", ETCDProperties.getConf().getProperty("ETCD_HOST"));
		map.put("ETCD_PORT", ETCDProperties.getConf().getProperty("ETCD_PORT"));
		map.put("ETCD_USER", ETCDProperties.getConf().getProperty("ETCD_USER"));
		map.put("ETCD_PWD", ETCDProperties.getConf().getProperty("ETCD_PWD"));

		map.put("LDAP_URL", LdapWrapper.getProps().getProperty("ldap.url"));
		map.put("LDAP_BASE", LdapWrapper.getProps().getProperty("ldap.base.name"));
		map.put("LDAP_USER_DN", LdapWrapper.getProps().getProperty("ldap.admin.dn"));
		map.put("LDAP_PASSWORD", LdapWrapper.getProps().getProperty("ldap.admin.password"));

		map.put("KRB_KDC_HOST", KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_KDC_HOST));
		map.put("KRB_USER_PRINCIPAL", KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_USER_PRINCIPAL));
		map.put("KRB_ADMIN_PASSWORD", KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_ADMIN_PASSWORD));
		map.put("KRB_REALM", KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_REALM));
		map.put("KRB_KRB5FILEPATH", KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_KRB5_LOCATION));

		map.put("CLUSTER_NAME", cluster.getCluster_name());
		map.put("KRB_ENABLE", String.valueOf(ambari.isSecurity()));

		RangerClient ranger = ClusterClientFactory.getRanger(cluster.getCluster_name());
		map.put("RANGER_URL", ranger.getRangerurl().toString());
		map.put("RANGER_ADMIN_USER", ranger.getRangeradmin());
		map.put("RANGER_ADMIN_PASSWORD", ranger.getRangerpassword());

		// TODO: to get all props from ambari rest api
		if (haenabled(ambari)) {
			map.put("HDFS_NAMESERVICES", "ABCD");
			map.put("HDFS_NAMENODE1_ADDR", "ABCD");
			map.put("HDFS_NAMENODE2_ADDR", "ABCD");
			map.put("HDFS_NAMENODE1", "ABCD");
			map.put("HDFS_NAMENODE2", "ABCD");
			map.put("HDFS_RPC_PORT", "ABCD");
		}
		else {
			map.put("HDFS_NAME_NODE", "ABCD");
			map.put("HDFS_RPC_PORT", "ABCD");
			map.put("HDFS_PORT", "ABCD");
		}

		map.put("HBASE_MASTER_URL", "ABCD");
		map.put("HBASE_MASTER", "ABCD");
		map.put("HBASE_REST_PORT", "ABCD");
		map.put("HBASE_ZOOKEEPER_QUORUM", "ABCD");
		map.put("HBASE_ZOOKEEPER_CLIENT_PORT", "ABCD");
		map.put("HBASE_ZOOKEEPER_ZNODE_PARENT", "ABCD");
		map.put("HBASE_ZOOKEEPER_ZNODE_PARENT", "/hbase-unsecure");

		map.put("HIVE_HOST", "ABCD");
		map.put("HIVE_PORT", "ABCD");

		map.put("YARN_RESOURCEMANAGER_HOST", "ABCD");
		map.put("YARN_RESOURCEMANAGER_PORT", "ABCD");
		map.put("YARN_RESOURCEMANAGER_URL", "ABCD");
		map.put("YARN_RESOURCEMANAGER_URL2", "ABCD");

		map.put("OC_ZK_CONNECTION", "ABCD");
		map.put("KAFKA_JAAS_PATH", "ABCD");
		map.put("KAFKA_HOSTS", "ABCD");
		map.put("KAFKA_PORT", "ABCD");
		map.put("KAFKA_REP_FACTOR", "ABCD");
		
		if (ambari.isSecurity()) {
			map.put("HDFS_SUPER_USER", "ABCD");
			map.put("HDFS_USER_KEYTAB", "ABCD");
			map.put("HBASE_MASTER_PRINCIPAL", "ABCD");
			map.put("HBASE_MASTER_USER_KEYTAB", "ABCD");
			map.put("HBASE_ZOOKEEPER_ZNODE_PARENT", "/hbase-secure");
			map.put("HIVE_SUPER_USER", "ABCD");
			map.put("HIVE_SUPER_USER_KEYTAB", "ABCD");
		}
		else {
			map.put("HADOOP_USER_NAME", "ABCD");
		}
	}

	private boolean haenabled(AmbariClient ambari) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cluster getCluster() {
		return this.cluster;
	}

}
