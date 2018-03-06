package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.bean.TenantQuotaBean;
import com.asiainfo.ocmanager.rest.resource.utils.model.TenantQuotaCheckerResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantQuotaUtils {

	private static Logger logger = LoggerFactory.getLogger(TenantQuotaUtils.class);

	/**
	 * get the tenant quota based on the service type and tenant quota str
	 * 
	 * @param service
	 * @param quotaStr
	 * @return
	 */
	public static Map<String, String> getTenantQuotaByService(String service, String quotaStr) {

		service = service.toLowerCase();

		Map<String, String> quotaMap = new HashMap<String, String>();

		if (!TenantJsonParserUtils.isValidJsonString(quotaStr)) {
			return quotaMap;
		}

		JsonElement quotaJson = new JsonParser().parse(quotaStr);
		JsonObject serviceQuota = quotaJson.getAsJsonObject().getAsJsonObject(service);

		if (serviceQuota == null || serviceQuota.isJsonNull()) {
			logger.debug("The tenant did NOT have the {} service quota, please check with admin.", service);
			return quotaMap;
		}

		quotaMap = QuotaCommonUtils.parserQuota(service, serviceQuota);

		return quotaMap;
	}

	/**
	 * check whether can create tenant based on the quota limitations
	 * 
	 * @param tenantQuota
	 * @return
	 */
	public static TenantQuotaCheckerResponse checkCanChangeTenant(TenantQuotaBean tenantQuota) {
		TenantQuotaCheckerResponse checkRes = new TenantQuotaCheckerResponse();
		StringBuilder resStr = new StringBuilder();
		boolean canChange = true;

		// hdfs
		if (tenantQuota.getNameSpaceQuotaHdfs() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getNameSpaceQuotaHdfs(), "nameSpaceQuota", "hdfs"));
			canChange = false;
		}
		if (tenantQuota.getStorageSpaceQuotaHdfs() < 0) {
			resStr.append(
					QuotaCommonUtils.logAndResStr(tenantQuota.getStorageSpaceQuotaHdfs(), "storageSpaceQuota", "hdfs"));
			canChange = false;
		}

		// hbase
		if (tenantQuota.getMaximumTablesQuotaHbase() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getMaximumTablesQuotaHbase(), "maximumTablesQuota",
					"hbase"));
			canChange = false;
		}
		if (tenantQuota.getMaximumRegionsQuotaHbase() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getMaximumRegionsQuotaHbase(),
					"maximumRegionsQuota", "hbase"));
			canChange = false;
		}

		// hive
		if (tenantQuota.getStorageSpaceQuotaHive() < 0) {
			resStr.append(
					QuotaCommonUtils.logAndResStr(tenantQuota.getStorageSpaceQuotaHive(), "storageSpaceQuota", "hive"));
			canChange = false;
		}
		if (tenantQuota.getYarnQueueQuotaHive() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getYarnQueueQuotaHive(), "yarnQueueQuota", "hive"));
			canChange = false;
		}

		// mapreduce
		if (tenantQuota.getYarnQueueQuotaMapreduce() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getYarnQueueQuotaMapreduce(), "yarnQueueQuota",
					"mapreduce"));
			canChange = false;
		}

		// spark
		if (tenantQuota.getYarnQueueQuotaSpark() < 0) {
			resStr.append(
					QuotaCommonUtils.logAndResStr(tenantQuota.getYarnQueueQuotaSpark(), "yarnQueueQuota", "spark"));
			canChange = false;
		}

		// kafka
		if (tenantQuota.getTopicQuotaKafka() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getTopicQuotaKafka(), "topicQuota", "kafka"));
			canChange = false;
		}
		if (tenantQuota.getPartitionSizeKafka() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getPartitionSizeKafka(), "partitionSize", "kafka"));
			canChange = false;
		}
		if (tenantQuota.getTopicTTLKafka() < 0) {
			resStr.append(QuotaCommonUtils.logAndResStr(tenantQuota.getTopicTTLKafka(), "topicTTL", "kafka"));
			canChange = false;
		}

		if (canChange) {
			resStr.append("can change the tenant!");
		}

		checkRes.setCanChange(canChange);
		checkRes.setMessages(resStr.toString());

		return checkRes;
	}

}
