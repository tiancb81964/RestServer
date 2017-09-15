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
public class MapreduceServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {
	private long yarnQueueQuota;

	public MapreduceServiceInstanceQuotaBean() {

	}

	public MapreduceServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.yarnQueueQuota = hdfsQuotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("yarnQueueQuota")).longValue();

	}

	public MapreduceServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.yarnQueueQuota = quotaMap.get("yarnQueueQuota") == null ? 0
				: Long.valueOf(quotaMap.get("yarnQueueQuota")).longValue();

	}

	/**
	 * 
	 * @return
	 */
	public static MapreduceServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		MapreduceServiceInstanceQuotaBean defaultServiceInstanceQuota = new MapreduceServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("mapreduce");
		defaultServiceInstanceQuota.setYarnQueueQuota(
				ServicesDefaultQuotaConf.getInstance().get("mapreduce").get("yarnQueueQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	@Override
	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId) {

		List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
				.getServiceInstanceByServiceType(tenantId, backingServiceName);
		Tenant parentTenant = TenantPersistenceWrapper.getTenantById(tenantId);

		// get all mapreduce children bsi quota
		MapreduceServiceInstanceQuotaBean mapreduceChildrenTotalQuota = new MapreduceServiceInstanceQuotaBean(
				backingServiceName, new HashMap<String, String>());

		for (ServiceInstance inst : serviceInstances) {
			MapreduceServiceInstanceQuotaBean quota = new MapreduceServiceInstanceQuotaBean(backingServiceName,
					inst.getQuota());
			mapreduceChildrenTotalQuota.plus(quota);
		}

		// get parent tenant quota
		Map<String, String> parentTenantQuotaMap = TenantQuotaUtils.getTenantQuotaByService(backingServiceName,
				parentTenant.getQuota());
		MapreduceServiceInstanceQuotaBean mapreduceParentTenantQuota = new MapreduceServiceInstanceQuotaBean(
				backingServiceName, parentTenantQuotaMap);

		// calculate the left quota
		mapreduceParentTenantQuota.minus(mapreduceChildrenTotalQuota);

		// get request bsi quota
		MapreduceServiceInstanceQuotaBean mapreduceRequestServiceInstanceQuota = MapreduceServiceInstanceQuotaBean
				.createDefaultServiceInstanceQuota();

		// left quota minus request quota
		mapreduceParentTenantQuota.minus(mapreduceRequestServiceInstanceQuota);

		return mapreduceParentTenantQuota.checker();
	}

	public ServiceInstanceQuotaCheckerResponse checker() {
		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// mr
		if (this.yarnQueueQuota < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(this.yarnQueueQuota, "yarnQueueQuota", "mapreduce"));
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
	public void plus(MapreduceServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota + otherServiceInstanceQuota.getYarnQueueQuota();

	}

	/**
	 * 
	 * @param otherServiceInstanceQuota
	 */
	public void minus(MapreduceServiceInstanceQuotaBean otherServiceInstanceQuota) {
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
		return "MapreduceServiceInstanceQuotaBean [yarnQueueQuota: " + yarnQueueQuota + ", serviceType: " + serviceType
				+ "]";
	}
}
