package com.asiainfo.ocmanager.service.broker.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.asiainfo.ocmanager.auth.LdapWrapper;
import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.bean.CustomEvnBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.service.broker.BrokerAdapterInterface;
import com.asiainfo.ocmanager.service.client.AmbariClient;
import com.asiainfo.ocmanager.service.client.ClusterClientFactory;
import com.asiainfo.ocmanager.service.client.RangerClient;
import com.asiainfo.ocmanager.utils.BrokersIni;
import com.asiainfo.ocmanager.utils.ETCDProperties;
import com.asiainfo.ocmanager.utils.KerberosConfiguration;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DPAdapter implements BrokerAdapterInterface {
	private Cluster cluster;
	private List<CustomEvnBean> customEnvs;

	public DPAdapter(Cluster cluster, List<CustomEvnBean> customEnvs) {
		check(cluster);
		this.cluster = cluster;
		this.customEnvs = customEnvs;
	}

	private void check(Cluster cluster) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getCluster_url()));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getCluster_admin()));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(cluster.getCluster_password()));
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
		insertInternalEnvs(ambari, envs);
		insertCustomizedEnvs(envs);
		return envs;
	}

	private void insertCustomizedEnvs(HashMap<String, String> envs) {
		for (CustomEvnBean kv : this.customEnvs) {
			envs.put(kv.getKey(), kv.getValue());
		}
	}

	private void insertInternalEnvs(AmbariClient ambari, HashMap<String, String> map) {
		map.put("BROKER_USERNAME", "admin");
		map.put("BROKER_PASSWORD", "admin");
		map.put("BROKER_ID", cluster.getCluster_name());

		map.put("AMBARI_HOST", cluster.getCluster_url());
		map.put("AMBARI_ADMIN_USER", cluster.getCluster_admin());
		map.put("AMBARI_ADMIN_PWD", cluster.getCluster_password());

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

		// TODO: to get all props from ambari rest api
		if (haenabled(ambari)) {
			map.put("HDFS_NAMESERVICES", "ABCD");
			map.put("HDFS_NAMENODE1_ADDR", "ABCD");
			map.put("HDFS_NAMENODE2_ADDR", "ABCD");
			map.put("HDFS_NAMENODE1", "ABCD");
			map.put("HDFS_NAMENODE2", "ABCD");
			map.put("HDFS_RPC_PORT", "ABCD");
		} else {
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
			map.put("HBASE_ZOOKEEPER_ZNODE_PARENT", "/hbase-secure");
		} else {
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

	@Override
	public List<CustomEvnBean> customEnvs() {
		List<CustomEvnBean> list = Lists.newArrayList();
		list.add(new CustomEvnBean("RANGER_ADMIN_USER", "ranger admin name"));
		list.add(new CustomEvnBean("RANGER_ADMIN_PASSWORD", "ranger admin password"));
		list.add(new CustomEvnBean("HDFS_SUPER_USER", "namenode principal"));
		list.add(new CustomEvnBean("HDFS_USER_KEYTAB", "namenode keytab"));
		list.add(new CustomEvnBean("HBASE_MASTER_PRINCIPAL", "hbase master principal"));
		list.add(new CustomEvnBean("HBASE_MASTER_USER_KEYTAB", "hbase master keytab"));
		list.add(new CustomEvnBean("HIVE_SUPER_USER", "hiveserver2 principal"));
		list.add(new CustomEvnBean("HIVE_SUPER_USER_KEYTAB", "hiveserver2 keytab"));
		return list;
	}

}
