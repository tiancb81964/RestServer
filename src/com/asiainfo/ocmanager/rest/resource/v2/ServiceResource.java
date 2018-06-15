package com.asiainfo.ocmanager.rest.resource.v2;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.persistence.model.Service;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.ServicePersistenceWrapper;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.utils.OsClusterIni;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author zhaoyim
 *
 */
@Path("/v2/api/service")
public class ServiceResource {

	private static Logger logger = LoggerFactory.getLogger(ServiceResource.class);

	/**
	 * Get All OCManager services
	 *
	 * @return service list
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.SERVICES)
	public Response getServices() {

		try {
			// TODO should call df service api and compare with adapter db
			// service data, insert the data which is not in the adapter db
			// every time when call the get all services api it will symnc the
			// adapter db with df services data
			// this is not a good solution should be enhance in future
			List<Service> servicesInDB = ServicePersistenceWrapper.getAllServices();

			// get all the services in the adapter db
			List<String> dbServiceNameList = new ArrayList<String>();
			for (Service s : servicesInDB) {
				dbServiceNameList.add(s.getServicename().toLowerCase());
			}

			String servicesFromDf = ServiceResource.callDFToGetAllServices();
			JsonObject servicesFromDfJson = new JsonParser().parse(servicesFromDf).getAsJsonObject();
			JsonArray items = servicesFromDfJson.getAsJsonArray("items");

			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					String name = items.get(i).getAsJsonObject().getAsJsonObject("spec").get("name").getAsString();
					String id = items.get(i).getAsJsonObject().getAsJsonObject("spec").get("id").getAsString();
					String description = items.get(i).getAsJsonObject().getAsJsonObject("spec").get("description")
							.getAsString();
					String origin = items.get(i).getAsJsonObject().getAsJsonObject("metadata").getAsJsonObject("labels")
							.get("asiainfo.io/servicebroker").getAsString();

					JsonObject specMetadata = items.get(i).getAsJsonObject().getAsJsonObject("spec")
							.getAsJsonObject("metadata");

					if (servicesInDB.size() == 0) {
						ServicePersistenceWrapper.addService(
								new Service(id, name, description, origin, this.parseServiceType(specMetadata, name),
										this.parseServiceCategory(specMetadata, name)));
					} else {
						if (!dbServiceNameList.contains(name.toLowerCase())) {
							ServicePersistenceWrapper.addService(new Service(id, name, description, origin,
									this.parseServiceType(specMetadata, name),
									this.parseServiceCategory(specMetadata, name)));
						}
					}

				}
			}

			List<Service> services = ServicePersistenceWrapper.getAllServices();

			return Response.ok().entity(services).tag("all-services").build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getServices  hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag("all-services").build();
		}
	}

	private String parseServiceType(JsonObject specMetadata, String serviceName) {
		JsonElement typeJE = specMetadata.get("type");
		if (typeJE == null) {
			logger.debug("The service {} is not have spec:metadata:type, please check with admin.", serviceName);
			// if the service did not have the type return the service name
			return serviceName;
		} else {
			String serviceType = specMetadata.get("type").getAsString();
			return serviceType;
		}
	}

	private String parseServiceCategory(JsonObject specMetadata, String serviceName) {
		JsonElement categoryJE = specMetadata.get("category");
		if (categoryJE == null) {
			logger.debug("The service {} is not have category, please check with admin.", serviceName);
			// if the service did not have the category return the default value
			// service
			return Constant.SERVICE;
		} else {
			String serviceCategory = specMetadata.get("category").getAsString();
			return serviceCategory;
		}
	}

	/**
	 * Get service by id
	 *
	 * @return service
	 */
	@GET
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.SERVICE)
	public Response getServiceById(@PathParam("id") String serviceId) {
		try {
			Service service = ServicePersistenceWrapper.getServiceById(serviceId);

			return Response.ok().entity(service == null ? new Service() : service).tag(serviceId).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getServiceById hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(serviceId).build();
		}
	}

	/**
	 * Get all service from df
	 *
	 * @return services
	 */
	@GET
	@Path("/df")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.SERVICES)
	public Response getServiceFromDf() {
		try {
			return Response.ok().entity(ServiceResource.callDFToGetAllServices()).tag("all-services").build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getServiceFromDf hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag("all-services").build();
		}
	}

	/**
	 * call data foundry rest api
	 *
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String callDFToGetAllServices() throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, ClientProtocolException, IOException {

		String url = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_URL);
		String token = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_TOKEN);
		String dfRestUrl = url + "/oapi/v1/namespaces/openshift/backingservices";

		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpGet httpGet = new HttpGet(dfRestUrl);
			httpGet.addHeader("Content-type", "application/json;charset=utf-8");
			httpGet.addHeader("Authorization", "bearer " + token);

			CloseableHttpResponse response1 = httpclient.execute(httpGet);

			try {
				// int statusCode =
				// response1.getStatusLine().getStatusCode();

				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");

				return bodyStr;
			} finally {
				response1.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * Get all service instances
	 *
	 * @return service instance list
	 */
	@GET
	@Path("all/instances")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.INSTANCES)
	public Response getAllServiceInstances() {
		try {
			List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper.getAllServiceInstances();
			return Response.ok().entity(serviceInstances).tag("all-instances").build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getAllServiceInstances hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag("all-instances").build();
		}
	}

	/**
	 * 
	 * @param serviceName
	 * @return
	 */
	@GET
	@Path("{serviceName}/plan")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.SERVICE_PLAN)
	public Response getServicePlanInfo(@PathParam("serviceName") String serviceName) {
		try {
			String servicesStr = ServiceResource.callDFToGetAllServices();
			JsonObject servicesJson = new JsonParser().parse(servicesStr).getAsJsonObject();
			JsonArray items = servicesJson.getAsJsonArray("items");
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					JsonObject spec = items.get(i).getAsJsonObject().getAsJsonObject("spec");
					String name = spec.get("name").getAsString();
					String plan = spec.getAsJsonArray("plans").toString();
					if (serviceName.toLowerCase().equals(name.toLowerCase())) {
						return Response.ok().entity(plan).tag(serviceName).build();
					}
				}
			}

			return Response.status(Status.NOT_FOUND)
					.entity(new ResourceResponseBean("get service plan failed",
							"can NOT find the service plan, please make sure you input the correct service name"
									+ " or the service is added successfully in the OCManager.",
							ResponseCodeConstant.SERVICE_PLAN_NOT_FOUND))
					.tag(serviceName).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getServicePlanInfo hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(serviceName).build();
		}
	}

}
