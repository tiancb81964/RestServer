package com.asiainfo.ocmanager.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.google.common.collect.Multimap;

/**
 * Sync up catalog with resources
 * @author Ethan
 *
 */
public class CatalogSynchronizer {
	private static final Logger LOG = LoggerFactory.getLogger(CatalogSynchronizer.class);
	private static Catalog catalog;
	static {
		try {
			catalog = Catalog.getInstance();
		} catch (Throwable e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}
	
	/**
	 * Synchronize catalog quotas with tenants in DB 
	 */
	public static void syncWithTenants() {
		catalog.listAllServices().values().forEach(s -> {
			List<String> quotas = catalog.getServiceQuotaKeys(s);
			updateTenants(s, quotas);
		});
	}
		

	private static void updateTenants(String s, List<String> quotas) {
		TenantPersistenceWrapper.getAllTenants().forEach(t -> {
			Multimap<String, String> tenantQuotas = QuotaParser.parse(t.getQuota());
			if (!tenantQuotas.containsKey(s)) {
				tenantQuotas.put(s, defaultQuota(quotas));
			}
			t.setQuota(QuotaParser.toString(tenantQuotas));
			TenantPersistenceWrapper.updateTenant(t);
		});
	}


	private static String defaultQuota(List<String> quotas) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
