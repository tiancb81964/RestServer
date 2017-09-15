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
public class SparkServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	private long yarnQueueQuota;

	public SparkServiceInstanceQuotaBean() {

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaStr
	 */
	public SparkServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.yarnQueueQuota = hdfsQuotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("yarnQueueQuota")).longValue();

	}

	/**
	 * 
	 * @param serviceType
	 * @param quotaMap
	 */
	public SparkServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.yarnQueueQuota = quotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(quotaMap.get("yarnQueueQuota")).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static SparkServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		SparkServiceInstanceQuotaBean defaultServiceInstanceQuota = new SparkServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("spark");
		defaultServiceInstanceQuota.setYarnQueueQuota(
				ServicesDefaultQuotaConf.getInstance().get("spark").get("yarnQueueQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId) {

		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceType(tenantId, backingServiceName);
		Tenant parentTenant = TenantPersistenceWrapper.getTenantById(tenantId);

		// get all mapreduce children bsi quota
		SparkServiceInstanceQuotaBean sparkChildrenTotalQuota = new SparkServiceInstanceQuotaBean(backingServiceName,
				new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			SparkServiceInstanceQuotaBean quota = new SparkServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			sparkChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		SparkServiceInstanceQuotaBean sparkParentTenantQuota = new SparkServiceInstanceQuotaBean(backingServiceName,
				parentTenantQuotaMap);

		// calculate the left quota
		sparkParentTenantQuota.minus(sparkChildrenTotalQuota);

		// get request bsi quota
		SparkServiceInstanceQuotaBean sparkRequestServiceInstanceQuota = SparkServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota();

		// left quota minus request quota
		sparkParentTenantQuota.minus(sparkRequestServiceInstanceQuota);

		return sparkParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// spark
		if (this.yarnQueueQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.yarnQueueQuota, "yarnQueueQuota", "spark"));
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
	public void plus(SparkServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota + otherServiceInstanceQuota.getYarnQueueQuota();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(SparkServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota - otherServiceInstanceQuota.getYarnQueueQuota();

	}

	public long getYarnQueueQuota() {
		return yarnQueueQuota;
	}

	public void setYarnQueueQuota(long yarnQueueQuota) {
		this.yarnQueueQuota = yarnQueueQuota;
	}

	@Override
	public String toString() {
		return "SparkServiceInstanceQuotaBean [yarnQueueQuota: " + yarnQueueQuota + ", serviceType: " + serviceType
				+ "]";
	}
}
