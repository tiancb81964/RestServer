package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.bean.TenantQuotaBean;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
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
	 * Get tenant quota of specified service type. Only those quota 
	 * of the specified service type will be returned.
	 * @param tenantID
	 * @param type
	 * @return
	 */
	public static Map<String, String> getTenantQuotaByService(String tenantID, ServiceType type){
		Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantID);
		Map<String, String> tenantQuotas = QuotaParser.parseServiceQuotaFromTenant(tenant.getQuota(), type);
		return tenantQuotas;
	}
	
	/**
	 * Get all allocated quotas of a tenant. Only the specified 
	 * service type quota will be returned.
	 * @param tenantID
	 * @param type
	 * @return
	 */
	public static Map<String, String> getAllocatedQuotaByService(String tenantID, ServiceType type){
		Map<String, String> map = newMap(type);
		List<ServiceInstance> instances = ServiceInstancePersistenceWrapper.getServiceInstanceByServiceType(tenantID, type.serviceType());
		for (ServiceInstance bsi : instances) {
			Map<String, String> allocated = QuotaParser.parseBSIQuota(bsi.getQuota(), type);
			map.forEach((k, v) -> {
				String newValue = String.valueOf(Long.valueOf(v) + Long.valueOf(allocated.get(k)));
				map.replace(k, v, newValue);
			});
		}
		return map;
	}
	
	private static Map<String, String> newMap(ServiceType type) {
		HashMap<String, String> map = new HashMap<>();
		for (String key : type.quotaKeys()) {
			map.put(key, "0"); // init quota assinged to zero
		}
		return map;
	}
	
	/**
	 * Parse tenant quota in json format
	 * @author EthanWang
	 *
	 */
	private static class QuotaParser {
		private static Map<String, String> parseServiceQuotaFromTenant(String tenantQuotaJson, ServiceType serviceType){
			Map<String, String> map = new HashMap<>();
			JsonObject json = new JsonParser().parse(tenantQuotaJson).getAsJsonObject().getAsJsonObject(serviceType.serviceType());
			if (json == null) {
				logger.error("No service quota({}) found in tenant_quota [{}] ", serviceType.serviceType(), tenantQuotaJson);
				throw new RuntimeException("No service quota been found in tenant");
			}
			for (Entry<String, JsonElement> kv : json.entrySet()) {
				map.put(kv.getKey(), kv.getValue().getAsString());
			}
			return map;
		}
		
		private static Map<String, String> parseBSIQuota(String bsiQuotaJson, ServiceType serviceType){
			Map<String, String> map = new HashMap<>();
			JsonObject json = new JsonParser().parse(bsiQuotaJson).getAsJsonObject();
			json.entrySet().forEach(e -> {
				if (Arrays.asList(serviceType.quotaKeys()).contains(e.getKey())) {
					map.put(e.getKey(), e.getValue().getAsString());
				}
			});
			return map;
		}
	}
	
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
			logger.error("TenantQuotaUtils -> Invalid json string, return empty map");
			return quotaMap;
		}

		JsonElement quotaJson = new JsonParser().parse(quotaStr);
		JsonObject serviceQuota = quotaJson.getAsJsonObject().getAsJsonObject(service);

		if (serviceQuota == null || serviceQuota.isJsonNull()) {
			logger.error("The tenant did NOT have the {} service quota, please check with admin.", service);
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
