package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceQuotaUtils {

	private static Logger logger = LoggerFactory.getLogger(ServiceInstanceQuotaUtils.class);

	public static Map<String, String> getServiceInstanceQuota(String service, String quotaStr) {

		service = service.toLowerCase();
		
		Map<String, String> quotaMap = new HashMap<String, String>();

		if (!TenantJsonParserUtils.isValidJsonString(quotaStr)) {
			logger.error("ServiceInstanceQuotaUtils -> Invalid json string, return empty map");
			return quotaMap;
		}

		JsonElement quotaJson = new JsonParser().parse(quotaStr);
		JsonObject serviceQuota = quotaJson.getAsJsonObject();

		if (serviceQuota == null || serviceQuota.isJsonNull()) {
			logger.error("The tenant did NOT have the {} service quota, please check with admin.", service);
		}

		quotaMap = QuotaCommonUtils.parserQuota(service, serviceQuota);

		return quotaMap;

	}

}
