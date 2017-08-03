package com.asiainfo.ocmanager.rest.resource.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantJsonParserUtils {

	
	public static JsonElement getPatchString(String tenantId, String instanceName) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException{
		String updateInstStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
		JsonElement updateInstJson = new JsonParser().parse(updateInstStr);
		JsonElement patch = updateInstJson.getAsJsonObject().getAsJsonObject("status").get("patch");
		
		return patch;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
