package com.asiainfo.ocmanager.service.broker.imp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.utils.Resource;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Implementing Broker interface and providing quota-relative operations. Any
 * other service resource monitor should extends this class to query for
 * <code>total quota</code> and <code> used quota</code>.
 * 
 * @author EthanWang
 *
 */
public abstract class BaseResourcePeeker implements ResourcePeeker {
	protected static Logger LOG = Logger.getLogger(BaseResourcePeeker.class);
	protected Resource resources;

	public BaseResourcePeeker() {
		setup();
	}

	public BaseResourcePeeker peekOn(List<String> resources) {
		if (resources == null || resources.isEmpty()) {
			LOG.error("Resources must be added into broker to do surveillance.");
			throw new RuntimeException("Resources must be added into broker to do surveillance.");
		}
		if (inited()) {
			LOG.error("Broker can only be inited for one time.");
			throw new RuntimeException("Broker can only be inited for one time.");
		}
		setupResources(resources);
		findUsage();
		cleanup();
		return this;
	}

	/**
	 * Will only be called once when first time create a instance. Heavy
	 * operations like opening and closing connections should be init and cached
	 * in this section.
	 */
	protected abstract void setup();

	/**
	 * Find the usage of current resources.
	 */
	private void findUsage() {
		fetchAction();
	}

	/**
	 * Will be called only once at the end of peeker lifetime to do cleanup
	 * work.
	 */
	protected abstract void cleanup();

	private void setupResources(List<String> resources) {
		Table<String, String, Long> targetResources = HashBasedTable.create();
		for (String type : resourceTypes()) {
			for (String resource : resources) {
				targetResources.put(type, resource, -1l);
			}
		}
		this.resources = new Resource(targetResources);
		LOG.info("Service broker peeking on: " + resources);
	}

	/**
	 * Return specific resource type names.
	 * 
	 * @return
	 */
	protected abstract List<String> resourceTypes();

	/**
	 * Init service broker with total quota.
	 */
	private void fetchAction() {
		for (Entry<String, Map<String, Long>> typeResources : this.resources.peekTotals().rowMap().entrySet()) {
			for (Entry<String, Long> resource : typeResources.getValue().entrySet()) {
				Long total = fetchTotalQuota(typeResources.getKey(), resource.getKey());
				Long used = fetchUsedQuota(typeResources.getKey(), resource.getKey());
				this.resources.updateTotal(typeResources.getKey(), resource.getKey(), total);
				this.resources.updateUsed(typeResources.getKey(), resource.getKey(), used);
			}
		}
	}

	/**
	 * Called repeatedly to fetch the total quota of each resource. Server
	 * connection should be cached to avoid frequent net IO.
	 * 
	 * @param type
	 * @return
	 */
	protected abstract Long fetchTotalQuota(String resourceType, String resourceName);

	/**
	 * Called repeatedly to fetch the used quota of each resource. Server
	 * connection should be cached to avoid frequent net IO.
	 * 
	 * @param type
	 * @return
	 */
	protected abstract Long fetchUsedQuota(String resourceType, String resourceName);

	public Long getTotalQuota(String key, String name) {
		if (inited()) {
			return this.resources.getTotal(key, name);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new RuntimeException("No resources for current broker to spy on!");
	}

	public Long getUsedQuota(String key, String name) {
		if (inited()) {
			return this.resources.getUsed(key, name);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new RuntimeException("No resources for current broker to spy on!");
	}

	private boolean inited() {
		return this.resources != null;
	}

	@Override
	public Long getFreeQuota(String key, String name) {
		if (inited()) {
			long t = this.resources.getTotal(key, name);
			if (t < 0) {
				return -1l; // free quota return -1 if total quota being
							// -1(unlimited).
			}
			long u = this.resources.getUsed(key, name);
			return (t - u);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new RuntimeException("No resources for current broker to spy on!");
	}
}
