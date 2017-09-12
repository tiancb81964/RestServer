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

		Map<String, String> quotaMap = new HashMap<String, String>();

		if (!TenantJsonParserUtils.isValidJsonString(quotaStr)) {
			return quotaMap;
		}

		JsonElement quotaJson = new JsonParser().parse(quotaStr);
		JsonObject serviceQuota = quotaJson.getAsJsonObject().getAsJsonObject(service);

		if (serviceQuota == null || serviceQuota.isJsonNull()) {
			logger.error("The tenant did NOT have the {} service quota, please check with admin.", service);
		}

		switch (service.toLowerCase()) {
		case "hdfs":

			String nameSpaceQuota = serviceQuota.get("nameSpaceQuota") == null ? null
					: serviceQuota.get("nameSpaceQuota").getAsString();
			String storageSpaceQuota = serviceQuota.get("storageSpaceQuota") == null ? null
					: serviceQuota.get("storageSpaceQuota").getAsString();

			quotaMap.put("nameSpaceQuota", nameSpaceQuota);
			quotaMap.put("storageSpaceQuota", storageSpaceQuota);
			break;
		case "hbase":

			String maximumTablesQuota = serviceQuota.get("maximumTablesQuota") == null ? null
					: serviceQuota.get("maximumTablesQuota").getAsString();
			String maximumRegionsQuota = serviceQuota.get("maximumRegionsQuota") == null ? null
					: serviceQuota.get("maximumRegionsQuota").getAsString();

			quotaMap.put("maximumTablesQuota", maximumTablesQuota);
			quotaMap.put("maximumRegionsQuota", maximumRegionsQuota);
			break;
		case "hive":

			String storageSpaceQuotaHive = serviceQuota.get("storageSpaceQuota") == null ? null
					: serviceQuota.get("storageSpaceQuota").getAsString();
			String yarnQueueQuotaHive = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("storageSpaceQuota", storageSpaceQuotaHive);
			quotaMap.put("yarnQueueQuota", yarnQueueQuotaHive);
			break;
		case "mapreduce":

			String yarnQueueQuotaMapreduce = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("yarnQueueQuota", yarnQueueQuotaMapreduce);
			break;
		case "spark":

			String yarnQueueQuotaSpark = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("yarnQueueQuota", yarnQueueQuotaSpark);
			break;
		case "kafka":

			String topicTTL = serviceQuota.get("topicTTL") == null ? null : serviceQuota.get("topicTTL").getAsString();
			String topicQuota = serviceQuota.get("topicQuota") == null ? null
					: serviceQuota.get("topicQuota").getAsString();
			String partitionSize = serviceQuota.get("partitionSize") == null ? null
					: serviceQuota.get("partitionSize").getAsString();

			quotaMap.put("topicTTL", topicTTL);
			quotaMap.put("topicQuota", topicQuota);
			quotaMap.put("partitionSize", partitionSize);
			break;
		default:
			logger.error("The {} service did NOT support the set quota in tenant, please check with admin.", service);
		}

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
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getNameSpaceQuotaHdfs(), "nameSpaceQuota", "hdfs"));
			canChange = false;
		}
		if (tenantQuota.getStorageSpaceQuotaHdfs() < 0) {
			resStr.append(
					TenantQuotaUtils.logAndResStr(tenantQuota.getStorageSpaceQuotaHdfs(), "storageSpaceQuota", "hdfs"));
			canChange = false;
		}

		// hbase
		if (tenantQuota.getMaximumTablesQuotaHbase() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getMaximumTablesQuotaHbase(), "maximumTablesQuota",
					"hbase"));
			canChange = false;
		}
		if (tenantQuota.getMaximumRegionsQuotaHbase() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getMaximumRegionsQuotaHbase(),
					"maximumRegionsQuota", "hbase"));
			canChange = false;
		}

		// hive
		if (tenantQuota.getStorageSpaceQuotaHive() < 0) {
			resStr.append(
					TenantQuotaUtils.logAndResStr(tenantQuota.getStorageSpaceQuotaHive(), "storageSpaceQuota", "hive"));
			canChange = false;
		}
		if (tenantQuota.getYarnQueueQuotaHive() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getYarnQueueQuotaHive(), "yarnQueueQuota", "hive"));
			canChange = false;
		}

		// mapreduce
		if (tenantQuota.getYarnQueueQuotaMapreduce() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getYarnQueueQuotaMapreduce(), "yarnQueueQuota",
					"mapreduce"));
			canChange = false;
		}

		// spark
		if (tenantQuota.getYarnQueueQuotaSpark() < 0) {
			resStr.append(
					TenantQuotaUtils.logAndResStr(tenantQuota.getYarnQueueQuotaSpark(), "yarnQueueQuota", "spark"));
			canChange = false;
		}

		// kafka
		if (tenantQuota.getTopicQuotaKafka() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getTopicQuotaKafka(), "topicQuota", "kafka"));
			canChange = false;
		}
		if (tenantQuota.getPartitionSizeKafka() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getPartitionSizeKafka(), "partitionSize", "kafka"));
			canChange = false;
		}
		if (tenantQuota.getTopicTTLKafka() < 0) {
			resStr.append(TenantQuotaUtils.logAndResStr(tenantQuota.getTopicTTLKafka(), "topicTTL", "kafka"));
			canChange = false;
		}

		if (canChange) {
			resStr.append("can change the tenant!");
		}

		checkRes.setCanChange(canChange);
		checkRes.setMessages(resStr.toString());

		return checkRes;
	}

	private static String logAndResStr(long quota, String param, String service) {
		logger.info("NOT enough " + service + " " + param + ", it need more: {}", quota);
		return "NOT enough " + service + " " + param + " to crteate the tenant, it need more quota: " + quota;
	}

}
