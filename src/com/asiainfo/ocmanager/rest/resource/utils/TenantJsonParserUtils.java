package com.asiainfo.ocmanager.rest.resource.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.TenantResource;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantJsonParserUtils {

	private static Logger logger = LoggerFactory.getLogger(TenantJsonParserUtils.class);
	
	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 */
	public static JsonElement getPatchString(String tenantId, String instanceName)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		String updateInstStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
		JsonElement updateInstJson = new JsonParser().parse(updateInstStr);
		JsonElement patch = updateInstJson.getAsJsonObject().getAsJsonObject("status").get("patch");

		return patch;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidJsonString(String str) {

		if (str == null) {
			return false;
		}

		if (str.indexOf("{") == -1 || str.indexOf("}") == -1) {
			return false;
		}

		try {
			new JsonParser().parse(str);
			return true;
		} catch (JsonParseException e) {
			logger.error("isValidJsonString hit exception-> ", e);
			return false;
		}
	}

}
