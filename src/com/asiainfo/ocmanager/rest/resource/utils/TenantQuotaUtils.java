package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.bean.TenantQuotaBeanV2;
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
	public static TenantQuotaCheckerResponse checkCanChangeTenant(TenantQuotaBeanV2 tenantQuota) {
		TenantQuotaCheckerResponse checkRes = new TenantQuotaCheckerResponse();
		checkRes.setCanChange(true);
		StringBuilder resStr = new StringBuilder();
		tenantQuota.getQuotas().cellSet().forEach(c -> {
			if (c.getValue() < 0) {
				checkRes.setCanChange(false);
				resStr.append(QuotaCommonUtils.logAndResStr(c.getValue(), c.getColumnKey(), c.getRowKey()));
			}
		});
		if (checkRes.isCanChange()) {
			resStr.append("Tenant passed quota examination!");
		}
		checkRes.setMessages(resStr.toString());
		return checkRes;
	}

}
