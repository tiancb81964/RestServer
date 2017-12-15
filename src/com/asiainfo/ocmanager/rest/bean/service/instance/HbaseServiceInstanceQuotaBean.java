package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.QuotaCommonUtils;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.TenantQuotaUtils;
import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.asiainfo.ocmanager.utils.ServicesDefaultQuotaConf;
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class HbaseServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String MAXIMUMTABLESQUOTA = "maximumTablesQuota";
	public final static String MAXIMUMREGIONSQUOTA = "maximumRegionsQuota";

	private double maximumTablesQuota;
	private double maximumRegionsQuota;

	public HbaseServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public HbaseServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.maximumTablesQuota = hdfsQuotaMap.get(MAXIMUMTABLESQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(MAXIMUMTABLESQUOTA)).longValue();
		this.maximumRegionsQuota = hdfsQuotaMap.get(MAXIMUMREGIONSQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(MAXIMUMREGIONSQUOTA)).longValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public HbaseServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.maximumTablesQuota = quotaMap.get(MAXIMUMTABLESQUOTA) == null ? 0
				: Long.valueOf(quotaMap.get(MAXIMUMTABLESQUOTA)).longValue();
		this.maximumRegionsQuota = quotaMap.get(MAXIMUMREGIONSQUOTA) == null ? 0
				: Long.valueOf(quotaMap.get(MAXIMUMREGIONSQUOTA)).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static HbaseServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		HbaseServiceInstanceQuotaBean defaultServiceInstanceQuota = new HbaseServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hbase");

		// if passby the params use the params otherwise use the default
		if (parameters == null) {
			defaultServiceInstanceQuota.setMaximumTablesQuota(
					ServicesDefaultQuotaConf.getInstance().get("hbase").get(MAXIMUMTABLESQUOTA).getDefaultQuota());
			defaultServiceInstanceQuota.setMaximumRegionsQuota(
					ServicesDefaultQuotaConf.getInstance().get("hbase").get(MAXIMUMREGIONSQUOTA).getDefaultQuota());
		} else {
			if (parameters.get(MAXIMUMTABLESQUOTA) == null || parameters.get(MAXIMUMTABLESQUOTA).isJsonNull()
					|| parameters.get(MAXIMUMTABLESQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setMaximumTablesQuota(
						ServicesDefaultQuotaConf.getInstance().get("hbase").get(MAXIMUMTABLESQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setMaximumTablesQuota(parameters.get(MAXIMUMTABLESQUOTA).getAsLong());
			}

			if (parameters.get(MAXIMUMREGIONSQUOTA) == null || parameters.get(MAXIMUMREGIONSQUOTA).isJsonNull()
					|| parameters.get(MAXIMUMREGIONSQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setMaximumRegionsQuota(
						ServicesDefaultQuotaConf.getInstance().get("hbase").get(MAXIMUMREGIONSQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setMaximumRegionsQuota(parameters.get(MAXIMUMREGIONSQUOTA).getAsLong());
			}
		}

		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId,
			JsonObject parameters) {

		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceType(tenantId, backingServiceName);
		Tenant parentTenant = TenantPersistenceWrapper.getTenantById(tenantId);

		// get all hbase children bsi quota
		HbaseServiceInstanceQuotaBean hbaseChildrenTotalQuota = new HbaseServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			HbaseServiceInstanceQuotaBean quota = new HbaseServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			hbaseChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		HbaseServiceInstanceQuotaBean hbaseParentTenantQuota = new HbaseServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		hbaseParentTenantQuota.minus(hbaseChildrenTotalQuota);

		// get request bsi quota
		HbaseServiceInstanceQuotaBean hbaseRequestServiceInstanceQuota = HbaseServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		hbaseParentTenantQuota.minus(hbaseRequestServiceInstanceQuota);

		return hbaseParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// hbase
		if (this.maximumTablesQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.maximumTablesQuota, MAXIMUMTABLESQUOTA, "hbase"));
			canChange = false;
		}
		if (this.maximumRegionsQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.maximumRegionsQuota, MAXIMUMREGIONSQUOTA, "hbase"));
			canChange = false;
		}

		if (canChange) {
			resStr.append("can change the bsi!");
		}

		checkRes.setCanChange(canChange);
		checkRes.setMessages(resStr.toString());

		return checkRes;
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void plus(HbaseServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.maximumTablesQuota = this.maximumTablesQuota + otherServiceInstanceQuota.getMaximumTablesQuota();
		this.maximumRegionsQuota = this.maximumRegionsQuota + otherServiceInstanceQuota.getMaximumRegionsQuota();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(HbaseServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.maximumTablesQuota = this.maximumTablesQuota - otherServiceInstanceQuota.getMaximumTablesQuota();
		this.maximumRegionsQuota = this.maximumRegionsQuota - otherServiceInstanceQuota.getMaximumRegionsQuota();
	}

	public double getMaximumTablesQuota() {
		return maximumTablesQuota;
	}

	public void setMaximumTablesQuota(double maximumTablesQuota) {
		this.maximumTablesQuota = maximumTablesQuota;
	}

	public double getMaximumRegionsQuota() {
		return maximumRegionsQuota;
	}

	public void setMaximumRegionsQuota(double maximumRegionsQuota) {
		this.maximumRegionsQuota = maximumRegionsQuota;
	}

	@Override
	public String toString() {
		return "HbaseServiceInstanceQuotaBean [maximumTablesQuota: " + maximumTablesQuota + ", maximumRegionsQuota: "
				+ maximumRegionsQuota + ", serviceType: " + serviceType + "]";
	}
}
