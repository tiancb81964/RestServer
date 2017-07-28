package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.security.module.plugin.KrbModule;

public class HadoopConfiguration {
	private static Configuration conf;
	private static final String CONF_NAMESERVICE = "oc.hdfs.dfs.nameservices";
	private static final String CONF_NNS = "oc.hdfs.dfs.ha.namenodes";
	private static final String PROXY = "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider";
	private static final String CONF_HBASE_MASTER_PRINCIPAL = "oc.hbase.master.krb.principal";
	private static final String CONF_HBASE_RS_PRINCIPAL = "oc.hbase.regionserver.krb.principal";

	public static Configuration getConf() {
		if (conf == null) {
			synchronized (HadoopConfiguration.class) {
				if (conf == null) {
					new HadoopConfiguration();
				}
			}
		}
		return conf;
	}

	private static void init() {
		conf = new Configuration();
		initHDFSCommon();
		initHbaseCommon();
		if (secure()) {
			conf.set("hadoop.security.authentication", "KERBEROS");
			conf.set("hbase.security.authentication", "KERBEROS");
			conf.set("hdfs.kerberos.principal", ServerConfiguration.getConf().getProperty(Constant.KRB_PRINCIPAL));
			conf.set("hdfs.keytab.file", keytabPath());
			conf.set("hbase.master.kerberos.principal",
					ServerConfiguration.getConf().getProperty(CONF_HBASE_MASTER_PRINCIPAL));
			conf.set("hbase.regionserver.kerberos.principal",
					ServerConfiguration.getConf().getProperty(CONF_HBASE_RS_PRINCIPAL));
			conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase-secure");
		}
	}

	private static void initHbaseCommon() {
		conf.set(HConstants.ZOOKEEPER_QUORUM, ServerConfiguration.getConf().getProperty(Constant.ZOOKEEPER));
		conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, ServerConfiguration.getConf().getProperty(Constant.ZOOKEEPER_PORT));
		conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase-unsecure");
	}

	private static void initHDFSCommon() {
		String ns = ServerConfiguration.getConf().getProperty(CONF_NAMESERVICE).trim();
		conf.set("fs.defaultFS", "hdfs://" + ns);
		conf.set("dfs.nameservices", ns);
		Map<String, String> map = decomposeNNString();
		conf.set("dfs.ha.namenodes." + ns, joinKey(map));
		for (Entry<String, String> nn : map.entrySet()) {
			conf.set("dfs.namenode.rpc-address." + ns + "." + nn.getKey(), nn.getValue());
		}
		conf.set("dfs.client.failover.proxy.provider." + ns, PROXY);
	}

	private static String joinKey(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> en : map.entrySet()) {
			sb.append(en.getKey()).append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	private static Map<String, String> decomposeNNString() {
		String rawNN = ServerConfiguration.getConf().getProperty(CONF_NNS);
		if (rawNN == null || rawNN.isEmpty()) {
			throw new RuntimeException(
					CONF_NNS + " can not be null! should be like: nn1#master1HostName:8020,nn2#master2HostNameIP:8020");
		}
		Map<String, String> map = new HashMap<>();
		for (String element : rawNN.split(",")) {
			map.put(element.trim().split("#")[0].trim(), element.trim().split("#")[1].trim());
		}
		return map;
	}

	private HadoopConfiguration() {
		init();
	}

	private static String keytabPath() {
		String keytab = ServerConfiguration.getConf().getProperty(Constant.KRB_KEYTAB);
		if (!new File(keytab).exists()) {
			System.out.println("Krb keytab file not exist: " + keytab);
			throw new RuntimeException("Krb keytab file not exist: " + keytab);
		}
		return keytab;
	}

	private static boolean secure() {
		String module = ServerConfiguration.getConf().getProperty(Constant.SECURITY_MODULE).trim();
		return module.equals(KrbModule.class.getName());
	}

}
