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

/**
 * 
 * @author zhaoyim
 *
 */
public class HdfsServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	private long nameSpaceQuota;
	private long storageSpaceQuota;

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
		this.nameSpaceQuota = hdfsQuotaMap.get("nameSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("nameSpaceQuota")).longValue();
		this.storageSpaceQuota = hdfsQuotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("storageSpaceQuota")).longValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public HdfsServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.nameSpaceQuota = quotaMap.get("nameSpaceQuota") == null ? 0
				: Long.valueOf(quotaMap.get("nameSpaceQuota")).longValue();
		this.storageSpaceQuota = quotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(quotaMap.get("storageSpaceQuota")).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static HdfsServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		HdfsServiceInstanceQuotaBean defaultServiceInstanceQuota = new HdfsServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hdfs");
		defaultServiceInstanceQuota.setNameSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("nameSpaceQuota").getDefaultQuota());
		defaultServiceInstanceQuota.setStorageSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("storageSpaceQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId) {

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
				.createDefaultServiceInstanceQuota();

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
			resStr.append(QuotaCommonUtils.logAndResStr(this.nameSpaceQuota, "nameSpaceQuota", "hdfs"));
			canChange = false;
		}
		if (this.storageSpaceQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.storageSpaceQuota, "storageSpaceQuota", "hdfs"));
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
	public long getNameSpaceQuota() {
		return nameSpaceQuota;
	}

	/**
	 * 
	 * @param nameSpaceQuota
	 */
	public void setNameSpaceQuota(long nameSpaceQuota) {
		this.nameSpaceQuota = nameSpaceQuota;
	}

	/**
	 * 
	 * @return
	 */
	public long getStorageSpaceQuota() {
		return storageSpaceQuota;
	}

	/**
	 * 
	 * @param storageSpaceQuota
	 */
	public void setStorageSpaceQuota(long storageSpaceQuota) {
		this.storageSpaceQuota = storageSpaceQuota;
	}

	@Override
	public String toString() {
		return "HdfsServiceInstanceQuotaBean [nameSpaceQuota: " + nameSpaceQuota + ", storageSpaceQuota: "
				+ storageSpaceQuota + ", serviceType: " + serviceType + "]";
	}

}