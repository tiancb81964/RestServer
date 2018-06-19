package com.asiainfo.ocmanager.service.client;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.justinsb.etcd.EtcdClient;
import com.justinsb.etcd.EtcdClientException;
import com.justinsb.etcd.EtcdNode;
import com.justinsb.etcd.EtcdResult;
import com.asiainfo.ocmanager.utils.ETCDProperties;

/**
 * ETCD client for DP-Brokers. Opertions of both read/write to ETCD by this client
 *
 * @author tiancb
 *
 */
public class etcdClient {
	
	private static Logger logger = LoggerFactory.getLogger(etcdClient.class);
	
	private Properties props = new Properties();
	private static final String ETCD_HOST = "ETCD_HOST";
	private static final String ETCD_PORT = "ETCD_PORT";
	private static final String ETCD_USER = "ETCD_USER";
	private static final String ETCD_PWD = "ETCD_PWD";
	private static etcdClient instance;
	
	private EtcdClient innerClient;
	public final String PATH_PREFIX = "/dp-brokers/";
	
	public static etcdClient getInstance() {
		if (instance == null) {
			synchronized (etcdClient.class) {
				if (instance == null) {
					instance = new etcdClient();
				}
			}
		}
		return instance;
	}

	public etcdClient() {
		props = ETCDProperties.getConf();
		String etcd_user = props.getProperty(ETCD_USER);
		String etcd_password = props.getProperty(ETCD_PWD);
		String etcd_host = props.getProperty(ETCD_HOST);
		String etcd_port = props.getProperty(ETCD_PORT);
		this.innerClient = new EtcdClient(
				URI.create("http://" + etcd_user + ":" + etcd_password + "@" + etcd_host + ":" + etcd_port));
	}

	public void check(String brokerId) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(brokerId), "BrokerID must not be null");
		if (!noChildren(PATH_PREFIX + brokerId)) {
			logger.error("Broker path already exist in ETCD: " + PATH_PREFIX);
			throw new RuntimeException("Broker path already exist in ETCD: " + PATH_PREFIX);
		}
	}

	private boolean noChildren(String pATH_PREFIX2) {
		List<EtcdNode> children = null;
		try {
			children = innerClient.listDirectory(pATH_PREFIX2);
		} catch (EtcdClientException e) {
			logger.error("noChildren() hit EtcdClientException",e);
			throw new RuntimeException("fail to check: " + pATH_PREFIX2);
		}
		return (children == null || children.isEmpty());
	}

	private String assemblePath(String suffix, String brokerId) {
		//check(brokerId);
		if (!suffix.startsWith("/")) {
			logger.error("Path not start with '/': " + suffix);
			throw new RuntimeException("Path must start with '/': " + suffix);
		}
		return this.PATH_PREFIX + brokerId + suffix;
	}

	public EtcdResult read(String key, String brokerId) {
		EtcdResult result = new EtcdResult();
		try {
			result = this.innerClient.get(assemblePath(key, brokerId));
		} catch (EtcdClientException e) {
			logger.error("read() hit EtcdClientException",e);
			throw new RuntimeException("fail to read DIR: " + key + brokerId);
		}
		return result;
	}

	public String readToString(String key, String brokerId) {
		EtcdResult result = this.read(key, brokerId);
		return (result != null && result.node != null) ? result.node.value : null;
	}

	public EtcdResult write(String key, String value, String brokerId) {
		EtcdResult result = new EtcdResult();
		try {
			result = this.innerClient.set(assemblePath(key, brokerId), value);
		} catch (EtcdClientException e) {
			logger.error("write() hit EtcdClientException",e);
			throw new RuntimeException("fail to write to: " + key + brokerId + value);
		}
		return result;
	}

	public EtcdResult createDir(String key, String brokerId) {
		EtcdResult result = new EtcdResult();
		try {
			result = this.innerClient.createDirectory(assemblePath(key, brokerId));
		} catch (EtcdClientException e) {
			logger.error("createDir() hit EtcdClientException",e);
			throw new RuntimeException("fail to create DIR: " + key + brokerId);
		}
		return result;
	}

	public EtcdResult delete(String key, String brokerId) {
		EtcdResult result = new EtcdResult();
		try {
			result = this.innerClient.delete(assemblePath(key, brokerId));
		} catch (EtcdClientException e) {
			logger.error("delete() hit EtcdClientException",e);
			throw new RuntimeException("fail to delete: " + key + brokerId);
		}
		return result;
	}

	public EtcdResult deleteDir(String key, String brokerId) {
		EtcdResult result = new EtcdResult();
		try {
			result = this.innerClient.deleteDirectory(assemblePath(key, brokerId));
		} catch (EtcdClientException e) {
			logger.error("deleteDir() hit EtcdClientException",e);
			throw new RuntimeException("fail to delete DIR: " + key + brokerId);
		}
		return result;
	}

}
