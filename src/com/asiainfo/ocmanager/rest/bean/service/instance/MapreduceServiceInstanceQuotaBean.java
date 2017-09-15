package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.QuotaCommonUtils;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceInstanceQuotaUtils;
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

	public static MapreduceServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		MapreduceServiceInstanceQuotaBean defaultServiceInstanceQuota = new MapreduceServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("mapreduce");
		defaultServiceInstanceQuota.setYarnQueueQuota(
				ServicesDefaultQuotaConf.getInstance().get("mapreduce").get("yarnQueueQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst() {
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

	public void plus(MapreduceServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.yarnQueueQuota = this.yarnQueueQuota + otherServiceInstanceQuota.getYarnQueueQuota();

	}

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
