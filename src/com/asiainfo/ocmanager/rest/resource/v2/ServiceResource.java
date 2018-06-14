package com.asiainfo.ocmanager.rest.resource.v2;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.auth.utils.TokenPaserUtils;
import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.persistence.model.Service;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.CreateBrokerBean;
import com.asiainfo.ocmanager.rest.bean.CreateBrokerBean.Phase;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.persistence.ClusterPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.ServicePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.utils.DataFoundryConfiguration;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;
import com.asiainfo.ocmanager.service.broker.utils.BrokerUtils;
import com.asiainfo.ocmanager.utils.DFTemplate;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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
	 * Add a broker
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Path("/broker")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.CREATE, targetType = TargetType.BROKER)
	public Response provisionBroker(@Context HttpServletRequest request) {
		String clustername = request.getParameter("clustername");
		Preconditions.checkArgument(Strings.isNullOrEmpty(clustername));
		CreateBrokerBean rsp = new CreateBrokerBean();
		try {
			Cluster cluster = ClusterPersistenceWrapper.getClusterByName(clustername);
			BrokerInterface adapter = BrokerUtils.getAdapter(cluster);
			String dcreq = DFTemplate.Create_DC.assembleString(adapter);
			String dcName = createdc(dcreq);
			rsp.withPhase(new Phase("create-dc", 200, ""));
			String svcreq = DFTemplate.Create_SVC.assembleString(dcName);
			String scvName = createsvc(svcreq);
			rsp.withPhase(new Phase("create-svc", 200, ""));
			String routerreq = DFTemplate.Create_Router.assembleString(scvName);
			int routerstatus = createrouter(routerreq);
			rsp.withPhase(new Phase("create-router", routerstatus, ""));
			rsp.setStatus(200);
			rsp.setMessage("");
			rsp.setAddress("123456789.cm.southbase.prd.dataos.io");
			// TODO: check rsp status and roll back stategy
			// TODP: insert broker info into CM_BROKERS table
			return Response.ok().entity(rsp).tag(clustername).build();
		} catch (Exception e) {
			logger.error("provisionBroker hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(clustername).build();
		}
	}

	private String createsvc(String svcreq) {
		// TODO Auto-generated method stub
		return "svc_name_987654321";
	}

	private String createdc(String dcreq) {
		// TODO Auto-generated method stub
		return "dc_name_123456789";
	}

	private int createrouter(String reqBody) {
		// TODO Auto-generated method stub
		return 200;
	}
	
	/**
	 * get a broker dc
	 * @param request
	 * @return
	 */
	@GET
	@Path("/broker/{id}/dc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.BROKER_DC)
	public Response getBrokerDC(@PathParam("id") String brokerid) {
		//TODO:
		return Response.ok().entity(getDCConfig()).build();
	}
	
	private String getDCConfig() {
		return "{\\r\\n    \\\"apiVersion\\\": \\\"v1\\\",\\r\\n    \\\"kind\\\": \\\"DeploymentConfig\\\",\\r\\n    \\\"metadata\\\": {\\r\\n        \\\"annotations\\\": {\\r\\n            \\\"dadafoundry.io\\/create-by\\\": \\\"chaizs\\\",\\r\\n            \\\"openshift.io\\/generated-by\\\": \\\"OpenShiftWebConsole\\\"\\r\\n        },\\r\\n        \\\"creationTimestamp\\\": \\\"2018-06-05T02:33:13Z\\\",\\r\\n        \\\"generation\\\": 3,\\r\\n        \\\"labels\\\": {\\r\\n            \\\"app\\\": \\\"cm-console\\\"\\r\\n        },\\r\\n        \\\"name\\\": \\\"cm-console\\\",\\r\\n        \\\"namespace\\\": \\\"southbase\\\",\\r\\n        \\\"resourceVersion\\\": \\\"9200111\\\",\\r\\n        \\\"selfLink\\\": \\\"\\/oapi\\/v1\\/namespaces\\/southbase\\/deploymentconfigs\\/cm-console\\\",\\r\\n        \\\"uid\\\": \\\"cb8ea774-6868-11e8-ae4e-fa163ef134de\\\"\\r\\n    },\\r\\n    \\\"spec\\\": {\\r\\n        \\\"replicas\\\": 1,\\r\\n        \\\"selector\\\": {\\r\\n            \\\"app\\\": \\\"cm-console\\\",\\r\\n            \\\"deploymentconfig\\\": \\\"cm-console\\\"\\r\\n        },\\r\\n        \\\"strategy\\\": {\\r\\n            \\\"activeDeadlineSeconds\\\": 21600,\\r\\n            \\\"resources\\\": {},\\r\\n            \\\"rollingParams\\\": {\\r\\n                \\\"intervalSeconds\\\": 1,\\r\\n                \\\"maxSurge\\\": \\\"25%\\\",\\r\\n                \\\"maxUnavailable\\\": \\\"25%\\\",\\r\\n                \\\"timeoutSeconds\\\": 600,\\r\\n                \\\"updatePeriodSeconds\\\": 1\\r\\n            },\\r\\n            \\\"type\\\": \\\"Rolling\\\"\\r\\n        },\\r\\n        \\\"template\\\": {\\r\\n            \\\"metadata\\\": {\\r\\n                \\\"annotations\\\": {\\r\\n                    \\\"openshift.io\\/generated-by\\\": \\\"OpenShiftWebConsole\\\"\\r\\n                },\\r\\n                \\\"creationTimestamp\\\": null,\\r\\n                \\\"labels\\\": {\\r\\n                    \\\"app\\\": \\\"cm-console\\\",\\r\\n                    \\\"deploymentconfig\\\": \\\"cm-console\\\"\\r\\n                }\\r\\n            },\\r\\n            \\\"spec\\\": {\\r\\n                \\\"containers\\\": [\\r\\n                    {\\r\\n                        \\\"env\\\": [\\r\\n                            {\\r\\n                                \\\"name\\\": \\\"ADAPTER_API_SERVER\\\",\\r\\n                                \\\"value\\\": \\\"http:\\/\\/10.1.236.60:9090\\\"\\r\\n                            },\\r\\n                            {\\r\\n                                \\\"name\\\": \\\"SVCAMOUNT_API_SERVER\\\",\\r\\n                                \\\"value\\\": \\\"http:\\/\\/svc-amount2.cloud.prd.asiainfo.com\\\"\\r\\n                            }\\r\\n                        ],\\r\\n                        \\\"image\\\": \\\"docker-registry.default.svc:5000\\/southbase\\/cm-console@sha256:8f0b437a91bed1ab44cfdda6b989debc078dfba8a2013ef38e5a824dff42afd7\\\",\\r\\n                        \\\"imagePullPolicy\\\": \\\"IfNotPresent\\\",\\r\\n                        \\\"name\\\": \\\"cm-console\\\",\\r\\n                        \\\"ports\\\": [\\r\\n                            {\\r\\n                                \\\"containerPort\\\": 9000,\\r\\n                                \\\"protocol\\\": \\\"TCP\\\"\\r\\n                            }\\r\\n                        ],\\r\\n                        \\\"resources\\\": {},\\r\\n                        \\\"terminationMessagePath\\\": \\\"\\/dev\\/termination-log\\\",\\r\\n                        \\\"terminationMessagePolicy\\\": \\\"File\\\"\\r\\n                    }\\r\\n                ],\\r\\n                \\\"dnsPolicy\\\": \\\"ClusterFirst\\\",\\r\\n                \\\"restartPolicy\\\": \\\"Always\\\",\\r\\n                \\\"schedulerName\\\": \\\"default-scheduler\\\",\\r\\n                \\\"securityContext\\\": {},\\r\\n                \\\"terminationGracePeriodSeconds\\\": 30\\r\\n            }\\r\\n        },\\r\\n        \\\"test\\\": false,\\r\\n        \\\"triggers\\\": [\\r\\n            {\\r\\n                \\\"type\\\": \\\"ConfigChange\\\"\\r\\n            }\\r\\n        ]\\r\\n    },\\r\\n    \\\"status\\\": {\\r\\n        \\\"availableReplicas\\\": 1,\\r\\n        \\\"conditions\\\": [\\r\\n            {\\r\\n                \\\"lastTransitionTime\\\": \\\"2018-06-05T02:34:59Z\\\",\\r\\n                \\\"lastUpdateTime\\\": \\\"2018-06-05T02:34:59Z\\\",\\r\\n                \\\"message\\\": \\\"Deployment config has minimum availability.\\\",\\r\\n                \\\"status\\\": \\\"True\\\",\\r\\n                \\\"type\\\": \\\"Available\\\"\\r\\n            },\\r\\n            {\\r\\n                \\\"lastTransitionTime\\\": \\\"2018-06-05T03:28:27Z\\\",\\r\\n                \\\"lastUpdateTime\\\": \\\"2018-06-05T03:28:29Z\\\",\\r\\n                \\\"message\\\": \\\"replication controller \\\\\\\"cm-console-2\\\\\\\" successfully rolled out\\\",\\r\\n                \\\"reason\\\": \\\"NewReplicationControllerAvailable\\\",\\r\\n                \\\"status\\\": \\\"True\\\",\\r\\n                \\\"type\\\": \\\"Progressing\\\"\\r\\n            }\\r\\n        ],\\r\\n        \\\"details\\\": {\\r\\n            \\\"causes\\\": [\\r\\n                {\\r\\n                    \\\"type\\\": \\\"ConfigChange\\\"\\r\\n                }\\r\\n            ],\\r\\n            \\\"message\\\": \\\"config change\\\"\\r\\n        },\\r\\n        \\\"latestVersion\\\": 2,\\r\\n        \\\"observedGeneration\\\": 3,\\r\\n        \\\"readyReplicas\\\": 1,\\r\\n        \\\"replicas\\\": 1,\\r\\n        \\\"unavailableReplicas\\\": 0,\\r\\n        \\\"updatedReplicas\\\": 1\\r\\n    }\\r\\n}\\r\\n";
	}
	
	/**
	 * update a broker
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/broker/{id}/dc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.UPDATE, targetType = TargetType.BROKER_DC)
	public Response updateBrokerDC(@Context HttpServletRequest request) {
		//TODO:
		return Response.ok().entity(getDCConfig()).build();
	}
	
	/**
	 * instantiate a broker
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/broker/{id}/dc/instantiate")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.INSTANTIATE, targetType = TargetType.BROKER_DC)
	public Response instantiateBrokerDC(@Context HttpServletRequest request) {
		//TODO:
		return Response.ok().entity(getDCConfig()).build();
	}
	
	/**
	 * Register service broker
	 *
	 * @return service
	 */
	@PUT
	@Path("/broker/register")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.REGISTER, targetType = TargetType.BROKER)
	public Response registerServiceBroker(String reqBodyStr, @Context HttpServletRequest request) {
		String brokerIP = extractIP(reqBodyStr);
		try {

			String adToken = request.getHeader("token");
			if (adToken == null || adToken.isEmpty()) {
				return Response.status(Status.NOT_FOUND)
						.entity(new ResourceResponseBean("add service broker failed",
								"token is null or empty, please check the token in request header.",
								ResponseCodeConstant.EMPTY_TOKEN))
						.tag(brokerIP).build();
			}

			String loginUser = TokenPaserUtils.paserUserName(adToken);
			logger.debug("addServiceBroker -> add service broker with login user: " + loginUser);

			UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser,
					Constant.ROOTTENANTID);

			if (role == null || !(role.getRoleName().equals(Constant.SYSADMIN))) {
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ResourceResponseBean("add service broker failed",
								"the user is not system admin role, does NOT have the add service broker permission.",
								ResponseCodeConstant.NO_ADD_SERVICE_BROKER_PERMISSION))
						.tag(brokerIP).build();
			}

			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/servicebrokers";

			// parse the req body make sure it is json
			JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);
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

				CloseableHttpResponse response2 = httpclient.execute(httpPost);

				try {
					// int statusCode =
					// response2.getStatusLine().getStatusCode();
					String bodyStr = EntityUtils.toString(response2.getEntity());

					return Response.ok().entity(bodyStr).tag(brokerIP).build();
				} finally {
					response2.close();
				}
			} finally {
				httpclient.close();
			}
			// TODO: update binded_cluster colume of CM_BROKERS table
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("addServiceBroker hit exception-> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(brokerIP).build();
		}
	}

	private String extractIP(String reqBodyStr) {
		JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);
		JsonObject spec = reqBodyJson.getAsJsonObject().getAsJsonObject("spec");
		JsonObject url = spec.getAsJsonObject("url");
		return url.getAsString();
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
	 * delete service broker
	 *
	 * @return service
	 */
	@DELETE
	@Path("/broker/{name}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.DELETE, targetType = TargetType.BROKER)
	public Response deleteServiceBroker(@PathParam("name") String serviceBrokerName,
			@Context HttpServletRequest request) {
		try {

			String adToken = request.getHeader("token");
			if (adToken == null || adToken.isEmpty()) {
				return Response.status(Status.NOT_FOUND)
						.entity(new ResourceResponseBean("delete service broker failed",
								"token is null or empty, please check the token in request header.",
								ResponseCodeConstant.EMPTY_TOKEN))
						.tag(serviceBrokerName).build();
			}

			String loginUser = TokenPaserUtils.paserUserName(adToken);
			logger.debug("deleteServiceBroker -> delete service broker with login user: " + loginUser);

			UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser,
					Constant.ROOTTENANTID);

			if (role == null || !(role.getRoleName().equals(Constant.SYSADMIN))) {
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ResourceResponseBean("delete service broker failed",
								"the user is not system admin role, does NOT have the add service broker permission.",
								ResponseCodeConstant.NO_DELETE_SERVICE_BROKER_PERMISSION))
						.tag(serviceBrokerName).build();
			}

			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/servicebrokers/" + serviceBrokerName;

			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpDelete httpDelete = new HttpDelete(dfRestUrl);
				httpDelete.addHeader("Content-type", "application/json");
				httpDelete.addHeader("Authorization", "bearer " + token);

				CloseableHttpResponse response1 = httpclient.execute(httpDelete);

				try {
					// int statusCode =
					// response1.getStatusLine().getStatusCode();

					String bodyStr = EntityUtils.toString(response1.getEntity());

					return Response.ok().entity(bodyStr).tag(serviceBrokerName).build();
				} finally {
					response1.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("deleteServiceBroker hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(serviceBrokerName).build();
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

		String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
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
