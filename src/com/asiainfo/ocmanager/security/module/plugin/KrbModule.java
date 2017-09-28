package com.asiainfo.ocmanager.security.module.plugin;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.security.module.SecurityModule;
import com.asiainfo.ocmanager.utils.HadoopConfiguration;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * Kerberos security module.
 * @author EthanWang
 *
 */
public class KrbModule implements SecurityModule{
	private static final Logger LOG = Logger.getLogger(KrbModule.class);
	private Configuration conf;
	private String principal;
	private String keytab;
	
	public KrbModule() {
		conf = HadoopConfiguration.getConf();
		this.principal = conf.get("hdfs.kerberos.principal");
		this.keytab = conf.get("hdfs.keytab.file");
		initKrbEnv();
	}
	
	private void initKrbEnv() {
		String krbConf = ServerConfiguration.getConf().getProperty(Constant.KRB_CONF);
		if (!new File(krbConf).exists()) {
			LOG.error("Krb conf file not exist: " + krbConf);
			throw new RuntimeException("Krb conf file not exist: " + krbConf);
		}
        System.setProperty("java.security.krb5.conf", krbConf);
        UserGroupInformation.setConfiguration(conf);
	}

	@Override
	public void login() throws Exception {
		try {
			LOG.info("Going to login as " + principal + " with keytab " + keytab);
	        UserGroupInformation.loginUserFromKeytab(principal, keytab);
	        LOG.info("Kerberos module login successful.");
		} catch (Exception e) {
			LOG.error("Login failed by: " + principal + ", " + keytab, e);
			throw e;
		}
	}

	@Override
	public void relogin() throws Exception {
		try {
			UserGroupInformation user = UserGroupInformation.getLoginUser();
			LOG.info("Going to relogin: " + user.getUserName());
			user.reloginFromKeytab();
	        LOG.info("Kerberos module relogin successful.");
		} catch (Exception e) {
			LOG.error("Relogin failed by: " +  UserGroupInformation.getLoginUser(), e);
			throw e;
		}
	}

}
