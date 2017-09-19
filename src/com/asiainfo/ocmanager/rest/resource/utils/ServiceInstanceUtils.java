package com.asiainfo.ocmanager.rest.resource.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.rest.bean.service.instance.ServiceInstanceQuotaBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceCreateInstanceBindingExecutor;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.asiainfo.ocmanager.rest.utils.DataFoundryConfiguration;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.rest.utils.UUIDFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceUtils {

	private static Logger logger = LoggerFactory.getLogger(ServiceInstanceUtils.class);

	/**
	 * 
	 * @param backingServiceName
	 * @param serviceInstances
	 * @param parentTenant
	 * @return
	 */
	public static ServiceInstanceQuotaCheckerResponse canCreateBsi(String backingServiceName, String tenantId,
			JsonObject parameters) {

		ServiceInstanceQuotaCheckerResponse checkRes = new ServiceInstanceQuotaCheckerResponse();
		ServiceInstanceQuotaBean serviceInst = ServiceInstanceQuotaBean.createServiceInstance(backingServiceName);

		if (serviceInst == null) {
			checkRes.setCanChange(false);
			checkRes.setMessages("Can NOT find the service in the OCM, please check with the admin.");
		} else {
			checkRes = serviceInst.checkCanChangeInst(backingServiceName, tenantId, parameters);
		}

		return checkRes;
	}

	/**
	 * 
	 * @param tenantId
	 * @param reqBodyJson
	 * @return
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws InterruptedException
	 */
	public static String createBsi(String tenantId, JsonElement reqBodyJson) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, InterruptedException {

		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances";

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(dfRestUrl);
			httpPost.addHeader("Content-type", "application/json");
			httpPost.addHeader("Authorization", "bearer " + token);

			StringEntity se = new StringEntity(reqBodyJson.toString());
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPost.setEntity(se);

			logger.info("createBsi -> begin to create service instance");
			CloseableHttpResponse response2 = httpclient.execute(httpPost);

			try {
				int statusCode = response2.getStatusLine().getStatusCode();
				String bodyStr = EntityUtils.toString(response2.getEntity());
				ServiceInstance serviceInstance = new ServiceInstance();
				if (statusCode == 201) {
					logger.info("createBsi -> create service instances successfully");

					JsonElement resBodyJson = new JsonParser().parse(bodyStr);
					JsonObject resBodyJsonObj = resBodyJson.getAsJsonObject();
					serviceInstance
							.setInstanceName(resBodyJsonObj.getAsJsonObject("metadata").get("name").getAsString());
					serviceInstance.setTenantId(tenantId);
					serviceInstance.setServiceTypeId(resBodyJsonObj.getAsJsonObject("spec")
							.getAsJsonObject("provisioning").get("backingservice_spec_id").getAsString());
					serviceInstance.setServiceTypeName(resBodyJsonObj.getAsJsonObject("spec")
							.getAsJsonObject("provisioning").get("backingservice_name").getAsString());

					JsonElement resCuzBsiNameJE = resBodyJsonObj.getAsJsonObject("spec").getAsJsonObject("provisioning")
							.getAsJsonObject("parameters").get("cuzBsiName");
					if (resCuzBsiNameJE != null) {
						serviceInstance.setCuzBsiName(resCuzBsiNameJE.getAsString());
					}

					// get the just now created instance info
					String getInstanceResBody = TenantUtils.getTenantServiceInstancesFromDf(tenantId,
							serviceInstance.getInstanceName());

					JsonElement serviceInstanceJson = new JsonParser().parse(getInstanceResBody);
					// get the service type
					String serviceName = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
							.getAsJsonObject("provisioning").get("backingservice_name").getAsString();
					// get status phase
					String phase = serviceInstanceJson.getAsJsonObject().getAsJsonObject("status").get("phase")
							.getAsString();
					// get the service instance name
					String instanceName = serviceInstanceJson.getAsJsonObject().getAsJsonObject("metadata").get("name")
							.getAsString();

					// loop to wait the instance status.phase change to
					// Unbound if the status.phase is Provisioning the
					// update will failed, so need to wait
					logger.info("createBsi -> waiting Provisioning to Unbound");
					while (phase.equals(Constant.PROVISIONING)) {
						// wait for 3 secs
						Thread.sleep(3000);
						// get the instance info again
						getInstanceResBody = TenantUtils.getTenantServiceInstancesFromDf(tenantId,
								serviceInstance.getInstanceName());
						serviceInstanceJson = new JsonParser().parse(getInstanceResBody);
						serviceName = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
								.getAsJsonObject("provisioning").get("backingservice_name").getAsString();
						phase = serviceInstanceJson.getAsJsonObject().getAsJsonObject("status").get("phase")
								.getAsString();
					}
					logger.info("createBsi -> waiting Provisioning to Unbound successfully");

					// sync here after the create successfully
					// get the latest status and insert into DB
					if ((resBodyJsonObj.getAsJsonObject("spec").getAsJsonObject("provisioning").get("parameters")
							.isJsonNull())) {
						// TODO should get df service quota
					} else {
						// parameters are a json format should use to string
						serviceInstance.setQuota(serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
								.getAsJsonObject("provisioning").get("parameters").toString());
					}
					serviceInstance.setStatus(phase);
					// set instance id, the id generated after Provisioning
					JsonElement instanceId = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
							.get("instance_id");
					if (instanceId == null || instanceId.isJsonNull() || instanceId.getAsString().isEmpty()) {
						// just make sure if the create failed and not
						// return id in df, generate the uid by
						// adapter self, in this way the data will
						// be sync with df
						serviceInstance.setId(UUIDFactory.getUUID());
					} else {
						serviceInstance.setId(instanceId.getAsString());
					}

					// insert the service instance into the adapter DB
					ServiceInstancePersistenceWrapper.createServiceInstance(serviceInstance);

					// if the phase is failed, it means the create failed
					if (phase.equals(Constant.FAILURE)) {
						logger.info("createBsi -> phase is failure, throw directly");
						// return
						// Response.ok().entity(getInstanceResBody).build();
						return getInstanceResBody;
					}

					// only the OCDP services need to wait to assign the
					// permission
					if (Constant.list.contains(serviceName.toLowerCase())) {

						TenantResourceCreateInstanceBindingExecutor runnable = new TenantResourceCreateInstanceBindingExecutor(
								tenantId, serviceName, instanceName);
						Thread thread = new Thread(runnable);
						thread.start();
					}
				}

				// return Response.ok().entity(bodyStr).build();
				return bodyStr;
			} finally {
				response2.close();
			}
		} finally {
			httpclient.close();
		}

	}

}
