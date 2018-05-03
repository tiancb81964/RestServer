package com.asiainfo.ocmanager.rest.bean;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantQuotaUtils;
import com.asiainfo.ocmanager.utils.Catalog;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
/**
 * Tenant quota calculator, replacing old version of {@linkplain TenantQuotaBean}
 * @author Ethan
 *
 */
public class TenantQuotaBeanV2 {
	public String tenantId;
	public String tenantName;
	private Table<String, String, Long> svcquot = HashBasedTable.create();

	public TenantQuotaBeanV2(Tenant tenant) {
		this.tenantId = tenant.getId();
		this.tenantName = tenant.getName();
		this.tenantQuotaParser(tenant.getQuota());
	}

	
	public Table<String, String, Long> getQuotas() {
		return svcquot;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * parser the quota str to tenant quota bean
	 * 
	 * @param quotaStr
	 */
	public void tenantQuotaParser(String quotaStr) {
		Catalog.getInstance().listAllServices().values().forEach(e -> {
			TenantQuotaUtils.getTenantQuotaByService(e, quotaStr).forEach((k,v) -> {
				long value = Long.valueOf(v);
				svcquot.put(e, k, value);
			});
		});
	}

	public static void main(String[] args) {
		Tenant t = TenantPersistenceWrapper.getTenantById("admin-1523586795");
		TenantQuotaBeanV2 b = new TenantQuotaBeanV2(t);
		System.out.println(">>> bean: " + b);
		b.minusOtherTenantQuota(new TenantQuotaBeanV2(t));
		System.out.println(">>> end of main: " + b);
	}
	/**
	 * add the 2 tenants quota together
	 * 
	 * @param otherTenantQuota
	 */
	public void plusOtherTenantQuota(TenantQuotaBeanV2 otherTenantQuota) {
		otherTenantQuota.svcquot.cellSet().forEach(o -> {
			Long value = this.svcquot.get(o.getRowKey(), o.getColumnKey());
			if (value == null) {
				return;
			}
			this.svcquot.put(o.getRowKey(), o.getColumnKey(), value + o.getValue());
		});
	}

	/**
	 * minus other tenant quota
	 * 
	 * @param otherTenantQuota
	 */
	public void minusOtherTenantQuota(TenantQuotaBeanV2 otherTenantQuota) {
		otherTenantQuota.svcquot.cellSet().forEach(o -> {
			Long value = this.svcquot.get(o.getRowKey(), o.getColumnKey());
			if (value == null) {
				return;
			}
			this.svcquot.put(o.getRowKey(), o.getColumnKey(), value - o.getValue());
		});
	}

	@Override
	public String toString() {
		return "TenantQuotaBean -> " + svcquot;
	}

}
