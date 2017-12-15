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
public class HdfsServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	public final static String NAMESPACEQUOTA = "nameSpaceQuota";
	public final static String STORAGESPACEQUOTA = "storageSpaceQuota";

	private double nameSpaceQuota;
	private double storageSpaceQuota;

	/**
	 * 
	 */
	public HdfsServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public HdfsServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.nameSpaceQuota = hdfsQuotaMap.get(NAMESPACEQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(NAMESPACEQUOTA)).longValue();
		this.storageSpaceQuota = hdfsQuotaMap.get(STORAGESPACEQUOTA) == null ? 0
				: Long.valueOf(hdfsQuotaMap.get(STORAGESPACEQUOTA)).longValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public HdfsServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.nameSpaceQuota = quotaMap.get(NAMESPACEQUOTA) == null ? 0
				: Long.valueOf(quotaMap.get(NAMESPACEQUOTA)).longValue();
		this.storageSpaceQuota = quotaMap.get(STORAGESPACEQUOTA) == null ? 0
				: Long.valueOf(quotaMap.get(STORAGESPACEQUOTA)).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static HdfsServiceInstanceQuotaBean createDefaultServiceInstanceQuota(JsonObject parameters) {
		HdfsServiceInstanceQuotaBean defaultServiceInstanceQuota = new HdfsServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hdfs");

		// if passby the params use the params otherwise use the default
		if (parameters == null) {
			defaultServiceInstanceQuota.setNameSpaceQuota(
					ServicesDefaultQuotaConf.getInstance().get("hdfs").get(NAMESPACEQUOTA).getDefaultQuota());
			defaultServiceInstanceQuota.setStorageSpaceQuota(
					ServicesDefaultQuotaConf.getInstance().get("hdfs").get(STORAGESPACEQUOTA).getDefaultQuota());
		} else {
			if (parameters.get(NAMESPACEQUOTA) == null || parameters.get(NAMESPACEQUOTA).isJsonNull()
					|| parameters.get(NAMESPACEQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setNameSpaceQuota(
						ServicesDefaultQuotaConf.getInstance().get("hdfs").get(NAMESPACEQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setNameSpaceQuota(parameters.get(NAMESPACEQUOTA).getAsLong());
			}

			if (parameters.get(STORAGESPACEQUOTA) == null || parameters.get(STORAGESPACEQUOTA).isJsonNull()
					|| parameters.get(STORAGESPACEQUOTA).getAsString().isEmpty()) {
				defaultServiceInstanceQuota.setStorageSpaceQuota(
						ServicesDefaultQuotaConf.getInstance().get("hdfs").get(STORAGESPACEQUOTA).getDefaultQuota());
			} else {
				defaultServiceInstanceQuota.setStorageSpaceQuota(parameters.get(STORAGESPACEQUOTA).getAsLong());
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

		// get all hdfs children bsi quota
		HdfsServiceInstanceQuotaBean hdfsChildrenTotalQuota = new HdfsServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());
		for (ServiceInstance inst : serviceInstances) {
			HdfsServiceInstanceQuotaBean quota = new HdfsServiceInstanceQuotaBean(backingServiceName, inst.getQuota());
			hdfsChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		HdfsServiceInstanceQuotaBean hdfsParentTenantQuota = new HdfsServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		hdfsParentTenantQuota.minus(hdfsChildrenTotalQuota);

		// get request bsi quota
		HdfsServiceInstanceQuotaBean hdfsRequestServiceInstanceQuota = HdfsServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota(parameters);

		// left quota minus request quota
		hdfsParentTenantQuota.minus(hdfsRequestServiceInstanceQuota);

		return hdfsParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// hdfs
		if (this.nameSpaceQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.nameSpaceQuota, NAMESPACEQUOTA, "hdfs"));
			canChange = false;
		}
		if (this.storageSpaceQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.storageSpaceQuota, STORAGESPACEQUOTA, "hdfs"));
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
	public void plus(HdfsServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota + otherServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota + otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(HdfsServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota - otherServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota - otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	/**
	 * 
	 * @return
	 */
	public double getNameSpaceQuota() {
		return nameSpaceQuota;
	}

	/**
	 * 
	 * @param nameSpaceQuota
	 */
	public void setNameSpaceQuota(double nameSpaceQuota) {
		this.nameSpaceQuota = nameSpaceQuota;
	}

	/**
	 * 
	 * @return
	 */
	public double getStorageSpaceQuota() {
		return storageSpaceQuota;
	}

	/**
	 * 
	 * @param storageSpaceQuota
	 */
	public void setStorageSpaceQuota(double storageSpaceQuota) {
		this.storageSpaceQuota = storageSpaceQuota;
	}

	@Override
	public String toString() {
		return "HdfsServiceInstanceQuotaBean [nameSpaceQuota: " + nameSpaceQuota + ", storageSpaceQuota: "
				+ storageSpaceQuota + ", serviceType: " + serviceType + "]";
	}

}