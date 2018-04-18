package com.asiainfo.ocmanager.tenant.management;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;
import com.asiainfo.ocmanager.tenant.management.listener.Listener;
import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * To do tenant lifetime management
 * 
 * @author Ethan
 *
 */
public class LifetimeManager {
	private static final Logger LOG = LoggerFactory.getLogger(LifetimeManager.class);
	private static LifetimeManager instance;
	private BlockingQueue<TenantEvent> dueTenants;
	private List<Listener> listeners = Lists.newArrayList();
	public static final String DEACTIVE = "deactive";
	public static final String ACTIVE = "active";

	public static LifetimeManager getInstance() {
		if (instance == null) {
			synchronized (LifetimeManager.class) {
				if (instance == null) {
					instance = new LifetimeManager();
				}
			}
		}
		return instance;
	}

	private LifetimeManager() {
		this.dueTenants = LifetimeDetector.getInstance().getDueTenants();
		this.listeners.addAll(configuredListeners());
		LOG.debug("Loaded listeners: " + this.listeners);
	}

	private Collection<? extends Listener> configuredListeners() {
		String[] listeners = ServerConfiguration.getConf().getProperty(Constant.LIFETIME_LISTENERS).split(",");
		Set<Listener> set = Sets.newHashSet();
		Lists.newArrayList(listeners).forEach(p -> {
			try {
				Class<?> listenerClz = Class.forName(p);
				if (!Listener.class.isAssignableFrom(listenerClz)) {
					LOG.error("Class [{}] should implements interface [{}]", listenerClz.getName(),
							Listener.class.getName());
					throw new RuntimeException(
							"Class should implements interface com.asiainfo.ocmanager.tenant.listener.Listener: "
									+ listenerClz.getName());
				}
				Object obj = listenerClz.newInstance();
				Listener listener = (Listener) obj;
				set.add(listener);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.error("Class init failed: ", e);
				throw new RuntimeException("Class init failed: ", e);
			}
		});
		return set;
	}

	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				TenantEvent tenant = null;
				while (true) {
					try {
						tenant = dueTenants.take();
						dealActions(tenant);
					}
					catch (InterruptedException e) {
						LOG.error("Taking tenant from BlockingQueue exception: ", e);
						continue;
					}catch (Exception e) {
						LOG.error("Exception during running Tenant-LifetimeManager-Thread: ", e);
						continue;
					}
				}
			}
		}, "Tenant-LifetimeManager-Thread").start();
		LOG.info("LifetimeManager started with listeners: " + this.listeners);
	}

	private void dealActions(TenantEvent tenant) {
		if (tenant.getTenant().getStatus().equals(DEACTIVE)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Tenant status is 'deactive' already, no need to deal.");
			}
			// no need to deal
			return;
		}
		LOG.info("Dealing TenantEvent: " + tenant);
		this.listeners.forEach(p -> {
			p.handleDue(tenant);
		});
	}

	public static void main(String[] args) {
		LifetimeManager.getInstance().start();
		System.out.println(">>> end of main.");
	}
}
