package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.utils.Catalog;
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class QuotaCommonUtils {

	private static Logger logger = LoggerFactory.getLogger(QuotaCommonUtils.class);

	public static Map<String, String> parserQuota(String service, JsonObject serviceQuota) {
		Map<String, String> quotaMap = new HashMap<String, String>();

		switch (Catalog.getInstance().getServiceType(service).toLowerCase()) {
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

			quotaMap.put("storageSpaceQuota", storageSpaceQuotaHive);
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
		case "redis":

			String memory = serviceQuota.get("memory") == null ? null : serviceQuota.get("memory").getAsString();
			String nodes = serviceQuota.get("nodes") == null ? null : serviceQuota.get("nodes").getAsString();
			String volumeSize = serviceQuota.get("volumeSize") == null ? null
					: serviceQuota.get("volumeSize").getAsString();

			quotaMap.put("memory", memory);
			quotaMap.put("nodes", nodes);
			quotaMap.put("volumeSize", volumeSize);
			break;

		case "storm":

			String stromMemory = serviceQuota.get("memory") == null ? null : serviceQuota.get("memory").getAsString();
			String supervisors = serviceQuota.get("supervisors") == null ? null
					: serviceQuota.get("supervisors").getAsString();
			String workers = serviceQuota.get("workers") == null ? null : serviceQuota.get("workers").getAsString();

			quotaMap.put("memory", stromMemory);
			quotaMap.put("supervisors", supervisors);
			quotaMap.put("workers", workers);
			break;

		case "elasticsearch":

			String elasticsearchReplicas = serviceQuota.get("replicas") == null ? null
					: serviceQuota.get("replicas").getAsString();
			String elasticsearchVolume = serviceQuota.get("volume") == null ? null
					: serviceQuota.get("volume").getAsString();
			String elasticsearchCpu = serviceQuota.get("cpu") == null ? null
					: serviceQuota.get("cpu").getAsString();
			String elasticsearchMemory = serviceQuota.get("mem") == null ? null
					: serviceQuota.get("mem").getAsString();

			quotaMap.put("replicas", elasticsearchReplicas);
			quotaMap.put("volume", elasticsearchVolume);
			quotaMap.put("cpu", elasticsearchCpu);
			quotaMap.put("mem", elasticsearchMemory);

			break;
		case "zeppelin":

			String zeppelinCpu = serviceQuota.get("cpu") == null ? null
					: serviceQuota.get("cpu").getAsString();
			String zeppelinMemory = serviceQuota.get("memory") == null ? null
					: serviceQuota.get("memory").getAsString();

			quotaMap.put("cpu", zeppelinCpu);
			quotaMap.put("memory", zeppelinMemory);

			break;
		case "anaconda":

			String anacondaCpu = serviceQuota.get("cpu") == null ? null
					: serviceQuota.get("cpu").getAsString();
			String anacondaMemory = serviceQuota.get("memory") == null ? null
					: serviceQuota.get("memory").getAsString();

			quotaMap.put("cpu", anacondaCpu);
			quotaMap.put("memory", anacondaMemory);

			break;
		case "dataiku":

			String dataikuCpu = serviceQuota.get("cpu") == null ? null
					: serviceQuota.get("cpu").getAsString();
			String dataikuMemory = serviceQuota.get("memory") == null ? null
					: serviceQuota.get("memory").getAsString();

			quotaMap.put("cpu", dataikuCpu);
			quotaMap.put("memory", dataikuMemory);

			break;

		default:
			logger.error("The {} service did NOT support the set quota in tenant, please check with admin.", service);
		}

		return quotaMap;
	}

	public static String logAndResStr(double quota, String param, String service) {
		logger.info("NOT enough " + service + " " + param + ", it need more: {}\n", -quota);
		return "NOT enough " + service + " " + param + " to create the tenant, it need more quota: " + (-quota) + "; ";
	}
}
