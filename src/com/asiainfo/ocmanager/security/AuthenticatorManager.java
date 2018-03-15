package com.asiainfo.ocmanager.security;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.auth.Authenticator;
import com.asiainfo.ocmanager.service.client.v2.Delegator;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.asiainfo.ocmanager.utils.ServicesIni;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * AuthenticatorManager is responsible for client authenticating. Each service
 * client(in <code> ../conf/services.ini</code> section) will create an
 * {@linkplain Authenticator} and then be registered in AuthenticatorManager.
 * Moreover, all registered {@linkplain Authenticator} will be continuous
 * authenticated under certain stategy(Simple mode or Kerberos mode)
 * 
 * @author Ethan
 * @param <T>
 *
 */
public class AuthenticatorManager extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticatorManager.class);
	private static AuthenticatorManager instance;
	private static long periodsec;
	private Map<String, AuthenticatorInterface> map;

	static {
		try {
			// 70200(19h) and 86400(24h) is advised
			String period = ServerConfiguration.getConf().getProperty("oc.authmgr.credential-refresh-period.seconds",
					"82800");
			periodsec = Long.valueOf(period);
			LOG.info("[oc.authmgr.credential-refresh-period.seconds] set to " + periodsec);
		} catch (Throwable e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}

	public static AuthenticatorManager getInstance() {
		if (instance == null) {
			synchronized (AuthenticatorManager.class) {
				if (instance == null) {
					instance = new AuthenticatorManager();
				}
			}
		}
		return instance;
	}

	private AuthenticatorManager() {
		map = Maps.newConcurrentMap();
		init();
	}

	private void init() {
		ServicesIni.getInstance().getAllProperties().forEach((k, v) -> {
			register(k);
		});
	}

	/**
	 * Get the authenticator of the specified service
	 * 
	 * @param serviceName
	 * @return
	 */
	public AuthenticatorInterface getAuthenticator(String serviceName) {
		return map.get(serviceName);
	}

	private void register(String serviceName) {
		try {
			AuthenticatorInterface authenticator = newAuthenticator(serviceName);
			map.put(serviceName, authenticator);
			LOG.info("Authenticator registered: [" + serviceName + "] -> " + authenticator.getDelegator());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			LOG.error("Exception while register authenticator: ", e);
			throw new RuntimeException("Exception while register authenticator: ", e);
		}
	}

	private AuthenticatorInterface newAuthenticator(String serviceName)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		String classname = ServicesIni.getInstance().getProperty(serviceName, ServiceClient.AUTH_CLASS);
		Preconditions.checkNotNull(classname,
				"[auth.class] parameter is null of service: " + serviceName + " in services.ini config file");
		try {
			Class<?> claz = Class.forName(classname);
			if (!BaseAuthenticator.class.isAssignableFrom(claz)) {
				LOG.error("Class [{}] should be sub-class of [{}]", claz.getName(), BaseAuthenticator.class.getName());
				throw new RuntimeException(
						"Class " + claz.getName() + " should be sub-class of " + BaseAuthenticator.class.getName());
			}
			Object obj = claz.getConstructor(Properties.class)
					.newInstance(ServicesIni.getInstance().getProperties(serviceName));
			AuthenticatorInterface auth = (AuthenticatorInterface) obj;
			return auth;
		} catch (ClassNotFoundException e) {
			LOG.error("Class not found: ", e);
			throw new RuntimeException("Class not found: ", e);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(periodsec * 1000l);// seconds to millseconds
				for (Entry<String, AuthenticatorInterface> en : map.entrySet()) {
					try {
						Delegator delegator = en.getValue().getDelegator();
						LOG.info("Refreshing credential for service [{}] of delegator [{}]", en.getKey(), delegator);
						delegator.refreshCredential();
						LOG.info("Successfuly Refreshed credential for service [{}] of delegator [{}]", en.getKey(),
								delegator);
					} catch (IOException e) {
						LOG.error("Exception while refreshing credential of [{}] for service [{}]: {}",
								en.getValue().getDelegator(), en.getKey(), e);
					}
				}
			} catch (Exception e) {
				LOG.error("Unexpected exception catched in AuthenticatorManager-Thread: ", e);
			}
		}
	}

	@SuppressWarnings("unused")
	private void unregister(String serviceName) {
		map.remove(serviceName);
		LOG.info("Authenticator unregistered: [" + serviceName + "]");
	}

}
