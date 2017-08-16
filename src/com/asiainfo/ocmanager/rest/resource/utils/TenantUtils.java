package com.asiainfo.ocmanager.rest.resource.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.utils.DataFoundryConfiguration;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TenantUtils {

	private static Logger logger = Logger.getLogger(TenantUtils.class);

	/**
	 * 
	 * @param unBindingRes
	 * @param tenantId
	 * @param instanceName
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void watiInstanceUnBindingComplete(ResourceResponseBean unBindingRes, String tenantId,
			String instanceName) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			IOException, InterruptedException {

		String unBindingResStr = unBindingRes.getMessage();
		JsonElement unBindingResJson = new JsonParser().parse(unBindingResStr);
		int bound = unBindingResJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();

		String instStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
		JsonElement instJson = new JsonParser().parse(instStr);

		int currentBound = instJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();

		int count = 0;
		while (currentBound == bound) {
			// if the wait 3600s, think it is dead loop, break
			if (count > 3600) {
				logger.debug("watiInstanceUnBindingComplete -> hit dead loop, break");
				break;
			}
			logger.debug("watiInstanceUnBindingComplete -> waiting");
			Thread.sleep(1000);
			instStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
			instJson = new JsonParser().parse(instStr);
			currentBound = instJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();
			count++;
		}

	}

	/**
	 * 
	 * @param bindingRes
	 * @param tenantId
	 * @param instanceName
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void watiInstanceBindingComplete(ResourceResponseBean bindingRes, String tenantId,
			String instanceName) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			IOException, InterruptedException {

		String bindingResStr = bindingRes.getMessage();
		JsonElement bindingResJson = new JsonParser().parse(bindingResStr);
		int bound = bindingResJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();

		String instStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
		JsonElement instJson = new JsonParser().parse(instStr);

		int currentBound = instJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();

		int count = 0;
		while (currentBound == bound) {
			// if the wait 3600s, think it is dead loop, break
			if (count > 3600) {
				logger.debug("watiInstanceBindingComplete -> hit dead loop, break");
				break;
			}
			logger.debug("watiInstanceBindingComplete -> waiting");
			Thread.sleep(1000);
			instStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
			instJson = new JsonParser().parse(instStr);
			currentBound = instJson.getAsJsonObject().getAsJsonObject("spec").get("bound").getAsInt();
			count++;
		}

	}

	/**
	 * 
	 * @param updateRes
	 * @param tenantId
	 * @param instanceName
	 * @throws InterruptedException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 */
	public static void watiInstanceUpdateComplete(ResourceResponseBean updateRes, String tenantId, String instanceName)
			throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			IOException {

		String updateInstStr = updateRes.getMessage();
		JsonElement updateInstJson = new JsonParser().parse(updateInstStr);

		JsonElement patch = updateInstJson.getAsJsonObject().getAsJsonObject("status").get("patch");

		while (patch != null) {
			logger.debug("watiInstanceUpdateComplete -> waiting");
			Thread.sleep(1000);
			updateInstStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
			updateInstJson = new JsonParser().parse(updateInstStr);

			patch = updateInstJson.getAsJsonObject().getAsJsonObject("status").get("patch");
		}

	}

	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param userName
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static ResourceResponseBean removeOCDPServiceCredentials(String tenantId, String instanceName,
			String userName) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances/" + instanceName
				+ "/binding";

		JsonObject reqBody = new JsonObject();
		reqBody.addProperty("apiVersion", "v1");
		reqBody.addProperty("kind", "BindingRequestOptions");
		reqBody.addProperty("bindKind", "HadoopUser");
		reqBody.addProperty("resourceName", userName);

		JsonObject metadata = new JsonObject();
		metadata.addProperty("name", instanceName);
		reqBody.add("metadata", metadata);
		String reqBodyStr = reqBody.toString();

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPut httpPut = new HttpPut(dfRestUrl);
			httpPut.addHeader("Content-type", "application/json");
			httpPut.addHeader("Authorization", "bearer " + token);

			StringEntity se = new StringEntity(reqBodyStr);
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPut.setEntity(se);

			CloseableHttpResponse response2 = httpclient.execute(httpPut);

			try {
				int statusCode = response2.getStatusLine().getStatusCode();

				String bodyStr = EntityUtils.toString(response2.getEntity());

				return new ResourceResponseBean("", bodyStr, statusCode);
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}

	}

	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param userName
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static ResourceResponseBean generateOCDPServiceCredentials(String tenantId, String instanceName,
			String userName, String accesses) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances/" + instanceName
				+ "/binding";

		JsonObject reqBody = new JsonObject();
		reqBody.addProperty("apiVersion", "v1");
		reqBody.addProperty("kind", "BindingRequestOptions");
		reqBody.addProperty("bindKind", "HadoopUser");
		reqBody.addProperty("resourceName", userName);

		JsonObject metadata = new JsonObject();
		metadata.addProperty("name", instanceName);
		reqBody.add("metadata", metadata);
		
		JsonObject parameters = new JsonObject();
		parameters.addProperty("user_name", userName);
		parameters.addProperty("accesses", accesses);
		reqBody.add("parameters", parameters);
		
		String reqBodyStr = reqBody.toString();

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(dfRestUrl);
			httpPost.addHeader("Content-type", "application/json");
			httpPost.addHeader("Authorization", "bearer " + token);

			StringEntity se = new StringEntity(reqBodyStr);
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPost.setEntity(se);

			CloseableHttpResponse response2 = httpclient.execute(httpPost);

			try {
				int statusCode = response2.getStatusLine().getStatusCode();

				String bodyStr = EntityUtils.toString(response2.getEntity());

				return new ResourceResponseBean("", bodyStr, statusCode);
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}

	}

	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static String getTenantServiceInstancesFromDf(String tenantId, String instanceName)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances/" + instanceName;

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpGet httpGet = new HttpGet(dfRestUrl);
			httpGet.addHeader("Content-type", "application/json");
			httpGet.addHeader("Authorization", "bearer " + token);

			CloseableHttpResponse response1 = httpclient.execute(httpGet);

			try {
				// int statusCode =
				// response1.getStatusLine().getStatusCode();

				String bodyStr = EntityUtils.toString(response1.getEntity());

				return bodyStr;
			} finally {
				response1.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * 
	 * @param tenantId
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static String getTenantAllServiceInstancesFromDf(String tenantId)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances";

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpGet httpGet = new HttpGet(dfRestUrl);
			httpGet.addHeader("Content-type", "application/json");
			httpGet.addHeader("Authorization", "bearer " + token);

			CloseableHttpResponse response1 = httpclient.execute(httpGet);

			try {
				int statusCode = response1.getStatusLine().getStatusCode();

				String bodyStr = EntityUtils.toString(response1.getEntity());

				// filter the _ToDelete instances
				if (statusCode == 200) {
					JsonElement jsonE = new JsonParser().parse(bodyStr);
					JsonObject jsonO = jsonE.getAsJsonObject();

					JsonArray items = jsonO.getAsJsonArray(("items"));

					Iterator<JsonElement> it = items.iterator();
					while (it.hasNext()) {
						JsonElement je = it.next();
						JsonObject status = je.getAsJsonObject().getAsJsonObject("status");
						JsonElement action = status.get("action");

						if (action != null) {
							if (action.getAsString().equals(Constant._TODELETE)) {
								it.remove();
							}
						}
					}
					bodyStr = jsonO.toString();
				}
				logger.debug("getTenantAllServiceInstancesFromDf -> " + bodyStr);
				return bodyStr;
			} finally {
				response1.close();
			}
		} finally {
			httpclient.close();
		}

	}

	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param reqBodyStr
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static ResourceResponseBean updateTenantServiceInstanceInDf(String tenantId, String instanceName,
			String reqBodyStr) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances/" + instanceName;

		// parse the req body make sure it is json
		JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);
		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPut httpPut = new HttpPut(dfRestUrl);
			httpPut.addHeader("Content-type", "application/json");
			httpPut.addHeader("Authorization", "bearer " + token);

			StringEntity se = new StringEntity(reqBodyJson.toString());
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPut.setEntity(se);

			CloseableHttpResponse response2 = httpclient.execute(httpPut);

			try {
				int statusCode = response2.getStatusLine().getStatusCode();
				String bodyStr = EntityUtils.toString(response2.getEntity());

				return new ResourceResponseBean("", bodyStr, statusCode);
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * remove the duplicate tenants in a tenant list
	 * 
	 * @param list
	 * @return
	 */
	public static List<Tenant> removeListDup(List<Tenant> list) {

		HashSet<Tenant> hs = new HashSet<Tenant>(list);
		List<Tenant> newList = new ArrayList<Tenant>();
		newList.addAll(hs);
		return newList;
	}

}
