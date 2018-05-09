package com.asiainfo.ocmanager.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.google.common.collect.Table;

/**
 * Sync up catalog with resources
 * 
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
	 * Synchronize catalog with cm resources
	 */
	public static void syncup() {
		syncupWithTenantQuotas();
	}

	private static void syncupWithTenantQuotas() {
		catalog.listAllServices().values().forEach(s -> {
			syncToTenants(s);
		});		
	}

	private static void syncToTenants(String s) {
		List<String> quotas = catalog.getServiceQuotaKeys(s);
		TenantPersistenceWrapper.getAllTenants().forEach(t -> {
			Table<String, String, Number> tenantQuotas = QuotaParser.parse(t.getQuota());
			if (!tenantQuotas.containsRow(s)) {
				appendNewService(tenantQuotas, s, quotas);
				t.setQuota(QuotaParser.toString(tenantQuotas));
				LOG.info("New service [{}] appended to tenant [{}]'s quota: [{}]", s, t.getName(), tenantQuotas);
				TenantPersistenceWrapper.updateTenant(t);
			}
		});
	}

	private static void appendNewService(Table<String, String, Number> tenantQuotas, String s, List<String> quotas) {
		quotas.forEach(q -> {
			tenantQuotas.put(s, q, 0);
		});
	}
}
