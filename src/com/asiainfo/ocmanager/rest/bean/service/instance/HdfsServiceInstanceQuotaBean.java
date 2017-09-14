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
public class HdfsServiceInstanceQuotaBean {

	private long nameSpaceQuota;
	private long storageSpaceQuota;
	private String serviceType;

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

	public HdfsServiceInstanceQuotaBean(String serviceType, Map<String, String> hdfsQuotaMap) {
		this.serviceType = serviceType;
		this.nameSpaceQuota = hdfsQuotaMap.get("nameSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("nameSpaceQuota")).longValue();
		this.storageSpaceQuota = hdfsQuotaMap.get("storageSpaceQuota") == null ? 0
				: Long.valueOf(hdfsQuotaMap.get("storageSpaceQuota")).longValue();

	}

	public static HdfsServiceInstanceQuotaBean createDefaultHdfsServiceInstanceQuota() {
		HdfsServiceInstanceQuotaBean defaultHdfsServiceInstanceQuota = new HdfsServiceInstanceQuotaBean();
		defaultHdfsServiceInstanceQuota.setServiceType("hdfs");
		defaultHdfsServiceInstanceQuota.setNameSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("nameSpaceQuota").getDefaultQuota());
		defaultHdfsServiceInstanceQuota.setStorageSpaceQuota(
				ServicesDefaultQuotaConf.getInstance().get("hdfs").get("storageSpaceQuota").getDefaultQuota());
		return defaultHdfsServiceInstanceQuota;
	}

	public ServiceInstanceQuotaCheckerResponse checkCanChangeHdfsInst() {
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
			resStr.append("can change the tenant!");
		}

		checkRes.setCanChange(canChange);
		checkRes.setMessages(resStr.toString());

		return checkRes;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void plus(HdfsServiceInstanceQuotaBean otherHdfsServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota + otherHdfsServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota + otherHdfsServiceInstanceQuota.getStorageSpaceQuota();
	}

	public void minus(HdfsServiceInstanceQuotaBean otherHdfsServiceInstanceQuota) {
		this.nameSpaceQuota = this.nameSpaceQuota - otherHdfsServiceInstanceQuota.getNameSpaceQuota();
		this.storageSpaceQuota = this.storageSpaceQuota - otherHdfsServiceInstanceQuota.getStorageSpaceQuota();
	}

	@Override
	public String toString() {
		return "HdfsServiceInstanceQuotaBean [nameSpaceQuota: " + nameSpaceQuota + ", storageSpaceQuota: "
				+ storageSpaceQuota + ", serviceType: " + serviceType + "]";
	}

}