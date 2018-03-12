package com.asiainfo.ocmanager.service.client.v2;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.security.AuthenticatorManager;
import com.asiainfo.ocmanager.utils.ServicesIni;
import com.google.common.collect.Maps;

/**
 * Service clients, which are listed in file <code>../conf/sergices.ini</code>,
 * will all be registered into pool. Moreover, clients will be registered with a
 * {@link Delegator} which is ugi in kerberos environment and subject in insecure
 * environment.
 * 
 * @author Ethan
 * @param <T>
 */
public class ServiceClientPool implements ServiceClientPoolInterface<ServiceClientInterface> {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceClientPool.class);
	// serviceName, client
	private Map<String, ServiceClientInterface> pool;
	private static ServiceClientPool instance;

	public static ServiceClientPool getInstance() {
		if (instance == null) {
			synchronized (ServiceClientPool.class) {
				if (instance == null) {
					instance = new ServiceClientPool();
				}
			}
		}
		return instance;
	}

	private ServiceClientPool() {
		pool = Maps.newConcurrentMap();
		initServiceClients();
	}

	private void initServiceClients() {
		ServicesIni.getInstance().getAllProperties().forEach((k, v) -> {
			String clz = v.getProperty(ServiceClient.CLIENT_CLASS);
			if (clz == null || clz.isEmpty()) {
				LOG.error("Config 'client.class' not found for service: " + k);
				throw new RuntimeException("Config 'client.class' not found for service: " + k);
			}
			Object cliobj = null;
			try {
				Class<?> clzobj = Class.forName(clz);
				if (!ServiceClient.class.isAssignableFrom(clzobj)) {
					LOG.error("Client [{}] is not subclass of com.asiainfo.ocmanager.service.client.core.Client.",
							clzobj.getName());
					throw new RuntimeException(
							"Class is not subclass of com.asiainfo.ocmanager.service.client.core.Client: "
									+ clzobj.getName());
				}
				Delegator subject = AuthenticatorManager.getInstance().getAuthenticator(k).getDelegator();
				cliobj = clzobj.getDeclaredConstructor(String.class, Delegator.class).newInstance(k, subject);
			} catch (ClassNotFoundException e) {
				LOG.error("Class not found: " + clz, e);
				throw new RuntimeException("Class not found: " + clz, e);
			} catch (Exception e) {
				LOG.error("Exception when create client: " + clz, e);
				throw new RuntimeException("Exception when create client: " + clz, e);
			}
			ServiceClient client = (ServiceClient) cliobj;
			pool.put(k.trim(), client);
			LOG.info("Service client registered: [" + k + "] -> " + client.getClass().getName());
		});

	}

	public static void main(String[] args) {
		ServiceClientPool.getInstance();
		System.out.println(">>> end of main:" + ServiceClientPool.getInstance().pool.keySet());
	}

	@Override
	public void register(ServiceClientInterface t) {
		if (pool.get(t.getServiceName().trim()) != null) {
			LOG.warn("Client already registered: " + t.getServiceName());
			return;
		}
		pool.put(t.getServiceName().trim(), t);
		LOG.info("Service client registered: " + t.getServiceName() + " -> " + t.getClass().getName());
	}

	@Override
	public void unregister(ServiceClientInterface t) {
		pool.remove(t.getServiceName().trim());
		LOG.info("Service client unregistered: " + t.getServiceName() + " -> " + t.getClass().getName());
	}

	@Override
	public ServiceClientInterface getClient(String serviceName) throws ClientNotFoundException {
		ServiceClientInterface client = pool.get(serviceName.trim());
		if (client == null) {
			LOG.warn("Service client not exist: " + serviceName);
			throw new ClientNotFoundException("Service client not exist: " + serviceName);
		}
		return client;
	}
	
	public static class ClientNotFoundException extends IOException{
		private static final long serialVersionUID = -3618870753268383724L;

		public ClientNotFoundException(String message, Throwable cause) {
			super(message, cause);
		}

		public ClientNotFoundException(String message) {
			super(message);
		}

		public ClientNotFoundException(Throwable cause) {
			super(cause);
		}
	}
}
