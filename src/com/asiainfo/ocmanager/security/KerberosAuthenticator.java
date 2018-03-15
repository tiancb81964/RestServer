package com.asiainfo.ocmanager.security;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.service.client.v2.Delegator;
import com.asiainfo.ocmanager.utils.KerberosConfiguration;
import com.google.common.base.Preconditions;

/**
 * Kerberos authenticator which deal with continuous authentication
 * 
 * @author Ethan
 *
 */
public class KerberosAuthenticator extends BaseAuthenticator implements AuthenticatorInterface {
	private static final Logger LOG = LoggerFactory.getLogger(KerberosAuthenticator.class);
	public static final String PRINCIPAL = "auth.principal";
	public static final String KEYTAB = "auth.keytab";
	private Properties config;
	private final Delegator delegator;

	static {
		try {
			initKrb();
		} catch (Throwable e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}

	public KerberosAuthenticator(Properties serviceConfig) {
		super(serviceConfig);
		config = serviceConfig;
		ensureKeytab();
		try {
			UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(config.getProperty(PRINCIPAL),
					config.getProperty(KEYTAB));
			delegator = new Delegator(ugi);
		} catch (IOException e) {
			LOG.error("Exception while login for principal [{}] with keytab [{}] ", config.getProperty(PRINCIPAL),
					config.getProperty(KEYTAB));
			throw new RuntimeException("Exception while login user from keytab: ", e);
		}
	}
	
	private static void initKrb() {
		String file = KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_KRB5_LOCATION);
		Preconditions.checkNotNull(file);
		if (!new File(file).exists()) {
			LOG.error("krb.conf file not found: " + file);
			throw new RuntimeException("krb.conf file not found: " + file);
		}
		LOG.info("java.security.krb5.conf set to: " + file);
		System.setProperty("java.security.krb5.conf", file);
		
		Configuration conf = new Configuration();
		conf.set("hadoop.security.authentication", "KERBEROS");
		UserGroupInformation.setConfiguration(conf);
	}

	private void ensureKeytab() {
		String keytab = config.getProperty(KEYTAB);
		Preconditions.checkNotNull(keytab);
		if (!new File(keytab).exists()) {
			LOG.error("Keytab file not found: " + keytab);
			throw new RuntimeException("Keytab file not found: " + keytab);
		}
		Preconditions.checkNotNull(config.get(PRINCIPAL));
	}

	@Override
	public void login(){
		try {
			delegator.refreshCredential();
		} catch (IOException e) {
			LOG.error("Exception while login for delegator: " + delegator);
			throw new RuntimeException("Exception while login for delegator: " + delegator, e);
		}
	}

	@Override
	public void relogin(){
		try {
			delegator.refreshCredential();
		} catch (IOException e) {
			LOG.error("Exception while relogin for delegator: " + delegator);

			throw new RuntimeException("Exception while relogin for delegator: " + delegator, e);
		}
	}

	@Override
	public Delegator getDelegator() {
		return delegator;
	}
}
