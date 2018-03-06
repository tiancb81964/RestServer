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
import com.asiainfo.ocmanager.rest.bean.QuotaBean2;
import com.asiainfo.ocmanager.rest.bean.TenantQuotaBean;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.model.TenantQuotaCheckerResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	 * transform the passed service quota to {@linkplain QuotaBean2}
	 * @param service
	 * @param serviceQuotaJson
	 * @return
	 */
	public static QuotaBean2 toQuotaBean2(ServiceType service, String serviceQuotaJson) {
		return QuotaParser.parseBSIQuota(serviceQuotaJson, service);
	}
	
	/**
	 * Get tenant quota of specified service type. Only those quota 
	 * of the specified service type will be returned.
	 * @param tenantID
	 * @param type
	 * @return
	 */
	public static QuotaBean2 getTenantQuotaByService(String tenantID, ServiceType type){
		Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantID);
		QuotaBean2 tenantQuotas = QuotaParser.parseServiceQuotaFromTenant(tenant.getQuota(), type);
		return tenantQuotas;
	}
	
	/**
	 * Get tenant quota and parse into a list of quotabeans of all kinds
	 * of {@linkplain ServiceType}
	 * @param tenantID
	 * @return
	 */
	public static List<QuotaBean2> getTenantQuotas(String tenantID){
		Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantID);
		List<QuotaBean2> services = QuotaParser.parseTenantQuotas(tenant.getQuota());
		return services;

	}
	
	/**
	 * Get all allocated quotas of a tenant, except the specified BSI. Only the specified 
	 * service type quota will be returned.
	 * @param tenantID
	 * @param string 
	 * @param instanceName 
	 * @param type
	 * @return
	 */
	public static QuotaBean2 getMinimumAllocatedQuotaByService(String tenantID, String instanceID, ServiceType type){
		List<ServiceInstance> instances = ServiceInstancePersistenceWrapper.getServiceInstanceByServiceName(tenantID, type.serviceType());
		QuotaBean2 bean = new QuotaBean2(type);
		for (ServiceInstance bsi : instances) {
			if (bsi.getId().equals(instanceID)) {
				continue;
			}
			QuotaBean2 allocated = QuotaParser.parseBSIQuota(bsi.getQuota(), type);
			bean.plus(allocated);
		}
		return bean;
	}
	
	/**
	 * Parse tenant quota in json format
	 * @author EthanWang
	 *
	 */
	private static class QuotaParser {
		private static QuotaBean2 parseServiceQuotaFromTenant(String tenantQuotaJson, ServiceType serviceType){
			Map<String, Long> map = new HashMap<>();
			JsonObject json = new JsonParser().parse(tenantQuotaJson).getAsJsonObject().getAsJsonObject(serviceType.serviceType());
			if (json == null) {
				logger.error("No service quota({}) found in tenant_quota [{}] ", serviceType.serviceType(), tenantQuotaJson);
				throw new RuntimeException("No service quota(" + serviceType.serviceType() + ") been found in tenant " + tenantQuotaJson);
			}
			for (Entry<String, JsonElement> kv : json.entrySet()) {
				map.put(kv.getKey(), kv.getValue().getAsLong());
			}
			return new QuotaBean2(serviceType, map);
		}
		
		private static QuotaBean2 parseBSIQuota(String bsiQuotaJson, ServiceType serviceType){
			Map<String, Long> map = new HashMap<>();
			JsonObject json = new JsonParser().parse(bsiQuotaJson).getAsJsonObject();
			json.entrySet().forEach(e -> {
				if (Arrays.asList(serviceType.quotaKeys()).contains(e.getKey())) {
					map.put(e.getKey(), e.getValue().getAsLong());
				}
			});
			return new QuotaBean2(serviceType, map);
		}
		
		/**
		 * Parse tenant quota string into quota list, with each element corresponding to
		 * one type of service {@linkplain ServiceTypes}
		 * @param tenantQuotaJson
		 * @return
		 */
		private static List<QuotaBean2> parseTenantQuotas(String tenantQuotaJson){
			JsonObject json = new JsonParser().parse(tenantQuotaJson).getAsJsonObject();
			if (json == null) {
				logger.error("No quota found in tenant [{}] ",  tenantQuotaJson);
				throw new RuntimeException("No quota found in tenant " + tenantQuotaJson);
			}
			List<QuotaBean2> list = Lists.newArrayList();
			json.entrySet().forEach(s -> {
				HashMap<String, Long> map = Maps.newHashMap();
				s.getValue().getAsJsonObject().entrySet().forEach(p -> {
					map.put(p.getKey(), p.getValue().getAsLong());
				});
				list.add(new QuotaBean2(ServiceType.valueOf(s.getKey()), map));
			});
			return list;
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
