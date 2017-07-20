package com.asiainfo.ocmanager.service.broker.imp;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.utils.Resource;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

/**
 * Implements Broker interface and provide quota-relative operations. 
 * Any other service quota broker should extends this class to query 
 * for <code>total quota</code> and <code> used quota</code>.
 * @author EthanWang
 *
 */
public abstract class BaseResourcePeeker implements ResourcePeeker{
	protected static Logger LOG = Logger.getLogger(BaseResourcePeeker.class);
	protected Resource resources;
	
	public BaseResourcePeeker(){
		init();
	}
	
	public BaseResourcePeeker peekOn(Multimap<String, String> resources) {
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
		return this;
	}
	
	/**
	 * Will only be called once when first time create a instance. 
	 * Heavy operations like opening and closing connections should be
	 * init and cached in this section.
	 */
	protected abstract void init();
	
	/**
	 * Find the usage of current resources.
	 */
	private void findUsage() {
		fetchTotal();
		fetchUsed();		
	}

	private void setupResources(Multimap<String, String> resources) {
		Table<String, String, Long> targetResources = HashBasedTable.create();
		for(Entry<String, String> resource : resources.entries()){
			targetResources.put(resource.getKey(), resource.getValue(), -1l);
		}
		this.resources = new Resource(targetResources);		
		LOG.info("Service broker spying on: " + resources);
	}

	/**
	 * Init service broker with total quota.
	 */
	private void fetchTotal(){
		for(Entry<String, Map<String, Long>> typeResources : this.resources.peekTotals().rowMap().entrySet()){
			for (Entry<String, Long> resource : typeResources.getValue().entrySet()) {
				Long total = fetchTotalQuota(typeResources.getKey(), resource.getKey());
				this.resources.updateTotal(typeResources.getKey(), resource.getKey(), total);
			}
		}
	}

	/**
	 * Update used quotas.
	 */
	private void fetchUsed(){
		for(Entry<String, Map<String, Long>> typeResources : this.resources.peekUsed().rowMap().entrySet()){
			for (Entry<String, Long> resource : typeResources.getValue().entrySet()) {
				Long total = fetchUsedQuota(typeResources.getKey(), resource.getKey());
				this.resources.updateUsed(typeResources.getKey(), resource.getKey(), total);
			}
		}
	}
	
	/**
	 * Called repeatedly to fetch the total quota of each 
	 * resource. Server connection should be cached to 
	 * avoid frequent net IO.
	 * @param type
	 * @return
	 */
	protected abstract Long fetchTotalQuota(String resourceType, String resourceName);
	
	/**
	 * Called repeatedly to fetch the used quota of each 
	 * resource. Server connection should be cached to 
	 * avoid frequent net IO.	 
	 * @param type
	 * @return
	 */
	protected abstract Long fetchUsedQuota(String resourceType, String resourceName);

	public Long getTotalQuota(String key, String name) throws Exception{
		if (inited()) {
			return this.resources.getTotal(key, name);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new Exception("No resources for current broker to spy on!");
	}
	
	public Long getUsedQuota(String key, String name) throws Exception{
		if (inited()) {
			return this.resources.getUsed(key, name);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new Exception("No resources for current broker to spy on!");	
	}
	
	private boolean inited(){
		return this.resources != null;
	}
	
	@Override
	public Long getFreeQuota(String key, String name) throws Exception {
		if (inited()) {
			long t = this.resources.getTotal(key, name);
			long u = this.resources.getUsed(key, name);
			return (t - u);
		}
		LOG.error("No resources for current broker to spy on!");
		throw new Exception("No resources for current broker to spy on!");	
	}
}
