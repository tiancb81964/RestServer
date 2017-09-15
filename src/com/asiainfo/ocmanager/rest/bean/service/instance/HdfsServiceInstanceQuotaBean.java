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
public class HdfsServiceInstanceQuotaBean extends ServiceInstanceQuotaBean {

	private long nameSpaceQuota;
	private long storageSpaceQuota;

	public HdfsServiceInstanceQuotaBean() {

	}

	public HdfsServiceInstanceQuotaBean(String serviceType, String quotaStr) {
		this.serviceType = serviceType;
		Map<String, String> hdfsQuotaMap = ServiceInstanceQuotaUtils.getServiceInstanceQuota(serviceType, quotaStr);
		this.nameSpaceQuota = hdfsQuotaMap.get("nameSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("nameSpaceQuota")).longValue();
		this.storageSpaceQuota = hdfsQuotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("storageSpaceQuota")).longValue();

	}

	public HdfsServiceInstanceQuotaBean(String serviceType, Map<String, String> quotaMap) {
		this.serviceType = serviceType;
		this.nameSpaceQuota = quotaMap.get("nameSpaceQuota") == null ? 0
				: Long.valueOf(quotaMap.get("nameSpaceQuota")).longValue();
		this.storageSpaceQuota = quotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(quotaMap.get("storageSpaceQuota")).longValue();

	}

	public static HdfsServiceInstanceQuotaBean createDefaultServiceInstanceQuota() {
		HdfsServiceInstanceQuotaBean defaultServiceInstanceQuota = new HdfsServiceInstanceQuotaBean();
		defaultServiceInstanceQuota.setServiceType("hdfs");
		defaultServiceInstanceQuota.setNameSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("nameSpaceQuota").getDefaultQuota());
		defaultServiceInstanceQuota.setStorageSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("storageSpaceQuota").getDefaultQuota());
		return defaultServiceInstanceQuota;
	}

	public ServiceInstanceQuotaCheckerResponse checkCanChangeInst() {
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

	public void plus(HdfsServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota + otherServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota + otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	public void minus(HdfsServiceInstanceQuotaBean otherServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota - otherServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota - otherServiceInstanceQuota.getStorageSpaceQuota();
	}

	public long getNameSpaceQuota() {
		return nameSpaceQuota;
	}

	public void setNameSpaceQuota(long nameSpaceQuota) {
		this.nameSpaceQuota = nameSpaceQuota;
	}

	public long getStorageSpaceQuota() {
		return storageSpaceQuota;
	}

	public void setStorageSpaceQuota(long storageSpaceQuota) {
		this.storageSpaceQuota = storageSpaceQuota;
	}

	@Override
	public String toString() {
		return "HdfsServiceInstanceQuotaBean [nameSpaceQuota: " + nameSpaceQuota + ", storageSpaceQuota: "
				+ storageSpaceQuota + ", serviceType: " + serviceType + "]";
	}

}