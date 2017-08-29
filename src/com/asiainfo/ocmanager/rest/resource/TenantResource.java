package com.asiainfo.ocmanager.rest.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.auth.utils.TokenPaserUtils;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.bean.TenantBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceAssignRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceCreateInstanceBindingExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceUnAssignRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceUpdateRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TURAssignmentPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.asiainfo.ocmanager.rest.utils.DataFoundryConfiguration;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.rest.utils.UUIDFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author zhaoyim
 *
 */

@Path("/tenant")
public class TenantResource {

	private static Logger logger = LoggerFactory.getLogger(TenantResource.class);

	/**
	 * Get All OCManager tenants
	 *
	 * @return tenant list
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getAllTenants() {
		try {
			List<Tenant> tenants = TenantPersistenceWrapper.getAllTenants();
			return Response.ok().entity(tenants).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getAllTenants hit exception-> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the specific tenant by id
	 *
	 * @param tenantId
	 *            tenant id
	 * @return tenant
	 */
	@GET
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getTenantById(@PathParam("id") String tenantId) {
		try {
			Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantId);
			return Response.ok().entity(tenant).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getTenantById hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the child tenants
	 *
	 * @param tenantId
	 *            tenant id
	 * @return tenant list
	 */
	@GET
	@Path("{id}/children")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getChildrenTenants(@PathParam("id") String parentTenantId) {
		try {
			List<Tenant> tenants = TenantPersistenceWrapper.getChildrenTenants(parentTenantId);
			return Response.ok().entity(tenants).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getChildrenTenants hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the users list in the specific tenant
	 *
	 * @param tenantId
	 *            tenant id
	 * @return user list
	 */
	@GET
	@Path("{id}/users")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getTenantUsers(@PathParam("id") String tenantId) {
		try {
			List<UserRoleView> usersRoles = UserRoleViewPersistenceWrapper.getUsersInTenant(tenantId);
			return Response.ok().entity(usersRoles).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getTenantUsers hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private static UserRoleView getRole(String tenantId, String userName) {

		UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(userName, tenantId);
		if (role == null) {
			logger.debug("getRole -> start get tenant");
			Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantId);
			logger.debug("getRole -> finish get tenant");
			if (tenant == null) {
				return null;
			}
			if (tenant.getParentId() == null) {
				return null;
			} else {
				role = TenantResource.getRole(tenant.getParentId(), userName);
			}
		}
		return role;
	}

	/**
	 * Get the role based on the tenant and user
	 *
	 * @param tenantId
	 * @param userName
	 * @return
	 */
	@GET
	@Path("{id}/user/{userName}/role")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getRoleByTenantUserName(@PathParam("id") String tenantId, @PathParam("userName") String userName) {
		try {
			UserRoleView role = TenantResource.getRole(tenantId, userName);
			if (role != null) {
				// set the tenant id to the passed tenant id
				role.setTenantId(tenantId);
			}
			return Response.ok().entity(role).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getRoleByTenantUserName hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the service instance list in the specific tenant
	 *
	 * @param tenantId
	 *            tenant id
	 * @return service instance list
	 */
	@GET
	@Path("{id}/service/instances")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getTenantServiceInstances(@PathParam("id") String tenantId) {
		try {
			List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
					.getServiceInstancesInTenant(tenantId);
			return Response.ok().entity(serviceInstances).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getTenantServiceInstances hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * get the tenant service instance info which include the access info
	 *
	 * @param tenantId
	 * @param InstanceName
	 * @return
	 */
	@GET
	@Path("{tenantId}/service/instance/{InstanceName}/access/info")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getTenantServiceInstanceAccessInfo(@PathParam("tenantId") String tenantId,
			@PathParam("InstanceName") String InstanceName) {
		try {
			return Response.ok().entity(TenantUtils.getTenantServiceInstancesFromDf(tenantId, InstanceName)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getTenantServiceInstanceAccessInfo hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

	/**
	 * Create a new tenant
	 *
	 * @param tenant
	 *            tenant obj json
	 * @return new tenant info
	 */
	@POST
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTenant(Tenant tenant, @Context HttpServletRequest request) {

		if (tenant.getName() == null || tenant.getId() == null) {
			return Response.status(Status.BAD_REQUEST).entity("input format is not correct").build();
		}

		// if the tenant have the instances
		// can NOT create chlid tenant
		if (tenant.getParentId() != null && TenantResource.hasInstances(tenant.getParentId())) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("The parent tenant have service instances, can NOT create child tenant.").build();
		}

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				logger.error("Only sysadmin can do this operation. User: " + loginUser);
				return Response.status(Status.UNAUTHORIZED).entity(new ResourceResponseBean("operation failed",
						"Current user has no privilege to do the operations.", ResponseCodeConstant.NOT_SYSTEM_ADMIN))
						.build();
			}

			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/projectrequests";

			JsonObject jsonObj1 = new JsonObject();
			jsonObj1.addProperty("apiVersion", "v1");
			jsonObj1.addProperty("kind", "ProjectRequest");
			// mapping DF tenant display name with adapter tenant name
			jsonObj1.addProperty("displayName", tenant.getName());
			if (tenant.getDescription() != null) {
				jsonObj1.addProperty("description", tenant.getDescription());
			}

			JsonObject jsonObj2 = new JsonObject();
			jsonObj2.addProperty("name", tenant.getId());
			jsonObj1.add("metadata", jsonObj2);
			String reqBody = jsonObj1.toString();

			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost(dfRestUrl);
				httpPost.addHeader("Content-type", "application/json");
				httpPost.addHeader("Authorization", "bearer " + token);

				StringEntity se = new StringEntity(reqBody);
				se.setContentType("application/json");
				se.setContentEncoding("utf-8");
				httpPost.setEntity(se);

				logger.info("createTenant -> start create");
				CloseableHttpResponse response2 = httpclient.execute(httpPost);

				try {
					int statusCode = response2.getStatusLine().getStatusCode();

					if (statusCode == 201) {
						logger.info("createTenant -> start successfully");
						TenantPersistenceWrapper.createTenant(tenant);
						logger.info("createTenant -> insert into DB successfully");
					}
					String bodyStr = EntityUtils.toString(response2.getEntity());

					return Response.ok().entity(new TenantBean(tenant, bodyStr)).build();
				} finally {
					response2.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("createTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Whether the role has tenant.admin privilege
	 *
	 * @param role
	 * @return
	 */
	private boolean privileged(UserRoleView role) {
		if (role == null || role.getRoleName().isEmpty()) {
			return false;
		}
		return role.getRoleName().equals(Constant.TENANTADMIN);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (token == null || token.isEmpty()) {
			logger.error("Token is null in request: " + request);
			throw new RuntimeException("Token is null in request");
		}
		return token;
	}

	private boolean isSysadmin(String user) {
		UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(user, Constant.ROOTTENANTID);
		if (role == null || role.getRoleName().isEmpty()) {
			return false;
		}
		return role.getRoleName().equals(Constant.SYSADMIN);
	}

	/**
	 * Create a service instance in specific tenant
	 *
	 * @param
	 * @return
	 */
	@POST
	@Path("{id}/service/instance")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createServiceInstanceInTenant(@PathParam("id") String tenantId, String reqBodyStr,
			@Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			// parse the req body make sure it is json
			JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);

			// when custom the bsi name shoud check
			// wethere it existing
			String metadataName = reqBodyJson.getAsJsonObject().getAsJsonObject("metadata").get("name").getAsString();
			String backingServiceName = reqBodyJson.getAsJsonObject().getAsJsonObject("spec")
					.getAsJsonObject("provisioning").get("backingservice_name").getAsString();
			JsonElement cuzBsiNameJE = reqBodyJson.getAsJsonObject().getAsJsonObject("spec")
					.getAsJsonObject("provisioning").getAsJsonObject("parameters").get("cuzBsiName");

			// metadata.name and spec.provisioning.parameters.cuzBsiName
			// should be the same
			if (cuzBsiNameJE != null && Constant.list.contains(backingServiceName.toLowerCase())) {
				String cuzBsiName = cuzBsiNameJE.getAsString();
				if (!metadataName.equals(cuzBsiName)) {
					logger.error("The service instance name are not match, metadata.name is {}; "
							+ "and spec.provisioning.parameters.cuzBsiName is {}. "
							+ "please make sure they are the same", metadataName, cuzBsiName);
					return Response.status(Status.BAD_REQUEST)
							.entity(new ResourceResponseBean("operation failed",
									"The service instance name are not match.", ResponseCodeConstant.BAD_REQUEST))
							.build();
				}

				ServiceInstance serInst = ServiceInstancePersistenceWrapper.getServiceInstanceByName(cuzBsiName);
				if (serInst != null) {
					logger.error("The service instance {} is already existing in OCDP cluster. "
							+ "please try another name.", cuzBsiName);
					return Response.status(Status.CONFLICT).entity(new ResourceResponseBean("operation failed",
							"The service instance is already existing in OCDP cluster.", ResponseCodeConstant.CONFLICT))
							.build();
				}
			}

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

				logger.info("createServiceInstanceInTenant -> begin to create service instance");
				CloseableHttpResponse response2 = httpclient.execute(httpPost);

				try {
					int statusCode = response2.getStatusLine().getStatusCode();
					String bodyStr = EntityUtils.toString(response2.getEntity());
					ServiceInstance serviceInstance = new ServiceInstance();
					if (statusCode == 201) {
						logger.info("createServiceInstanceInTenant -> create service instances successfully");

						JsonElement resBodyJson = new JsonParser().parse(bodyStr);
						JsonObject resBodyJsonObj = resBodyJson.getAsJsonObject();
						serviceInstance
								.setInstanceName(resBodyJsonObj.getAsJsonObject("metadata").get("name").getAsString());
						serviceInstance.setTenantId(tenantId);
						serviceInstance.setServiceTypeId(resBodyJsonObj.getAsJsonObject("spec")
								.getAsJsonObject("provisioning").get("backingservice_spec_id").getAsString());
						serviceInstance.setServiceTypeName(resBodyJsonObj.getAsJsonObject("spec")
								.getAsJsonObject("provisioning").get("backingservice_name").getAsString());

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
						String instanceName = serviceInstanceJson.getAsJsonObject().getAsJsonObject("metadata")
								.get("name").getAsString();

						// loop to wait the instance status.phase change to
						// Unbound if the status.phase is Provisioning the
						// update will failed, so need to wait
						logger.info("createServiceInstanceInTenant -> waiting Provisioning to Unbound");
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
						logger.info("createServiceInstanceInTenant -> waiting Provisioning to Unbound successfully");

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
							logger.info("createServiceInstanceInTenant -> phase is failure, throw directly");
							return Response.ok().entity(getInstanceResBody).build();
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

					return Response.ok().entity(bodyStr).build();
				} finally {
					response2.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("createServiceInstanceInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

	/**
	 * Update a service instance in specific tenant
	 *
	 * @param tenantId
	 * @param instanceName
	 * @param reqBodyStr
	 * @return
	 */
	@PUT
	@Path("{id}/service/instance/{instanceName}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateServiceInstanceInTenant(@PathParam("id") String tenantId,
			@PathParam("instanceName") String instanceName, String parametersStr, @Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			// get the just now created instance info
			String getInstanceResBody = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);

			JsonElement serviceInstanceJson = new JsonParser().parse(getInstanceResBody);
			// get status phase
			String phase = serviceInstanceJson.getAsJsonObject().getAsJsonObject("status").get("phase").getAsString();

			if (phase.equals(Constant.PROVISIONING)) {
				logger.info(
						"updateServiceInstanceInTenant -> The instance can not be updated when it is Provisioning!");
				return Response.status(Status.BAD_REQUEST)
						.entity("The instance can not be updated when it is Provisioning!").build();
			}

			if (phase.equals(Constant.FAILURE)) {
				logger.info("updateServiceInstanceInTenant -> The instance can not be updated when it is Failure!");
				return Response.status(Status.BAD_REQUEST).entity("The instance can not be updated when it is Failure!")
						.build();
			}

			// get the provisioning json
			JsonObject provisioning = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
					.getAsJsonObject("provisioning");

			// parse the input parameters json
			JsonElement parameterJon = new JsonParser().parse(parametersStr);
			JsonObject parameterObj = parameterJon.getAsJsonObject().getAsJsonObject("parameters");

			// check whether parameters format is legal
			try {
				Iterator<Entry<String, JsonElement>> iterator = parameterObj.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, JsonElement> entry = iterator.next();
					if (!isChanged(tenantId, instanceName, entry)) {
						// remove unchanged parameters
						logger.warn("Removing request parameter [" + entry.getKey()
								+ "], coz it's equivalent to current value: " + entry.getValue());
						iterator.remove();
					}

					// only check quota
					if (Constant.serviceQuotaParam.contains(entry.getKey())) {
						// if value is not int, will throw Exception
						entry.getValue().getAsLong();
						logger.info("parameters" + entry.getKey() + ":" + entry.getValue());
					}
				}
			} catch (Exception e) {
				logger.error("The parameter format check error:", e);
				return Response.status(Status.BAD_REQUEST)
						.entity("BadRequest: the parameter value format is illegal! Error:" + e.toString()).build();
			}

			// add into the update json
			provisioning.add("parameters", parameterObj);

			// add the patch Updating into the request body
			JsonObject status = serviceInstanceJson.getAsJsonObject().getAsJsonObject("status");
			status.addProperty("patch", Constant.UPDATE);

			logger.info("updateServiceInstanceInTenant -> update start");
			ResourceResponseBean responseBean = TenantUtils.updateTenantServiceInstanceInDf(tenantId, instanceName,
					serviceInstanceJson.toString());

			String quota = null;
			if (responseBean.getResCodel() == 200) {
				logger.info("updateServiceInstanceInTenant -> update successfully");
				JsonElement resBodyJson = new JsonParser().parse(responseBean.getMessage());
				JsonObject resBodyJsonObj = resBodyJson.getAsJsonObject();
				if ((resBodyJsonObj.getAsJsonObject("spec").getAsJsonObject("provisioning").get("parameters")
						.isJsonNull())) {
					logger.error(
							"Abnormal response from DF, parameters returned by DF is null! UpdateRquest: instanceName "
									+ instanceName + ", parameters " + parametersStr);
					throw new RuntimeException("parameters returned by DF is null!");
				} else {
					quota = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
							.getAsJsonObject("provisioning").get("parameters").toString();
				}
				ServiceInstancePersistenceWrapper.updateServiceInstanceQuota(tenantId, instanceName,
						quotaString(parametersStr));
			}

			return Response.ok().entity(responseBean.getMessage()).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("updateServiceInstanceInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private String quotaString(String parametersStr) {
		JsonElement parameterJon = new JsonParser().parse(parametersStr);
		JsonObject parameterObj = parameterJon.getAsJsonObject().getAsJsonObject("parameters");
		return parameterObj.toString();
	}

	/**
	 * Filter request parameters, remove unchanged parameters for better
	 * performance of backend services.
	 * 
	 * @param instanceName
	 * @param instanceName2
	 * @param entry
	 */
	private boolean isChanged(String tenantID, String instanceName, Entry<String, JsonElement> entry) {
		if (isKafkaTopicQuota(entry)) {
			long quotaInDB = getQuotaInDB(tenantID, instanceName);
			long newQuota = entry.getValue().getAsLong();
			if (newQuota < quotaInDB) {
				logger.error("Kafka topicQuota parameter [" + newQuota + "] must NOT be smaller than current value: "
						+ quotaInDB);
				throw new RuntimeException("Kafka topicQuota must not be smaller than current value: " + quotaInDB);
			} else if (newQuota == quotaInDB) {
				logger.debug("Kafka topicQuota parameter is equivalent to current value: " + quotaInDB);
				return false;
			} else {
				return true;
			}
		}
		// pass all other services except Kafka.
		return true;
	}

	private boolean isKafkaTopicQuota(Entry<String, JsonElement> entry) {
		return "topicQuota".equals(entry.getKey().trim());
	}

	private long getQuotaInDB(String tenantID, String instanceName) {
		String json = ServiceInstancePersistenceWrapper.getServiceInstance(tenantID, instanceName).getQuota();
		JsonObject jobj = new JsonParser().parse(json).getAsJsonObject();
		long topicSize = jobj.get("topicQuota").getAsLong();
		return topicSize;
	}

	/**
	 * Delete a service instance in specific tenant
	 *
	 * @param tenantId
	 * @param instanceName
	 * @return
	 */
	@DELETE
	@Path("{id}/service/instance/{instanceName}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServiceInstanceInTenant(@PathParam("id") String tenantId,
			@PathParam("instanceName") String instanceName, @Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			String getInstanceResBody = TenantUtils.getTenantServiceInstancesFromDf(tenantId, instanceName);
			JsonElement resBodyJson = new JsonParser().parse(getInstanceResBody);
			JsonObject instance = resBodyJson.getAsJsonObject();
			String serviceName = instance.getAsJsonObject("spec").getAsJsonObject("provisioning")
					.get("backingservice_name").getAsString();
			// get status phase
			String phase = instance.getAsJsonObject("status").get("phase").getAsString();

			if (phase.equals(Constant.PROVISIONING)) {
				logger.info(
						"deleteServiceInstanceInTenant -> The instance can not be deleted when it is Provisioning!");
				return Response.status(Status.BAD_REQUEST)
						.entity("The instance can not be deleted when it is Provisioning!").build();
			}

			// get binding info
			JsonObject spec = instance.getAsJsonObject("spec");
			JsonElement binding = spec.get("binding");

			// if the instance is Failure do not need to unbound
			if (!phase.equals(Constant.FAILURE)) {
				if (Constant.list.contains(serviceName.toLowerCase())) {
					if (!binding.isJsonNull()) {
						JsonArray bindingArray = spec.getAsJsonArray("binding");
						for (JsonElement je : bindingArray) {
							String userName = je.getAsJsonObject().get("bind_hadoop_user").getAsString();
							logger.debug("deleteServiceInstanceInTenant -> userName" + userName);
							logger.info("deleteServiceInstanceInTenant -> begin to unbinding");
							ResourceResponseBean unBindingRes = TenantUtils.removeOCDPServiceCredentials(tenantId,
									instanceName, userName);

							if (unBindingRes.getResCodel() == 201) {
								logger.info("deleteServiceInstanceInTenant -> wait unbinding complete");
								TenantUtils.watiInstanceUnBindingComplete(unBindingRes, tenantId, instanceName);
								logger.info("deleteServiceInstanceInTenant -> unbinding complete");
							}
						}
					}
				}
			}

			// begin to delete the instance
			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances/" + instanceName;

			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpDelete httpDelete = new HttpDelete(dfRestUrl);
				httpDelete.addHeader("Content-type", "application/json");
				httpDelete.addHeader("Authorization", "bearer " + token);

				logger.info("deleteServiceInstanceInTenant -> start delete");
				CloseableHttpResponse response1 = httpclient.execute(httpDelete);

				try {
					int statusCode = response1.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						ServiceInstancePersistenceWrapper.deleteServiceInstance(tenantId, instanceName);
						logger.info("deleteServiceInstanceInTenant -> delete successfully");
					}
					String bodyStr = EntityUtils.toString(response1.getEntity());

					return Response.ok().entity(bodyStr).build();
				} finally {
					response1.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("deleteServiceInstanceInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

	/**
	 * Update the existing tenant info
	 *
	 * @param tenant
	 *            tenant obj json
	 * @return updated tenant info
	 */
	@Deprecated
	@PUT
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Tenant updateTenant(Tenant tenant) {
		return tenant;
	}

	/**
	 * Delete a tenant
	 *
	 * @param tenantId
	 *            tenant id
	 */
	@DELETE
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTenant(@PathParam("id") String tenantId, @Context HttpServletRequest request) {
		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				logger.error("Only sysadmin can do this operation. User: " + loginUser);
				return Response.status(Status.UNAUTHORIZED).entity(new ResourceResponseBean("operation failed",
						"Current user has no privilege to do the operations.", ResponseCodeConstant.NOT_SYSTEM_ADMIN))
						.build();
			}

			// if have instances can not be deleted
			if (TenantResource.hasInstances(tenantId)) {
				return Response.status(Status.NOT_ACCEPTABLE)
						.entity("The tenant can not be deleted, because it have service instances on it.").build();
			}
			// if have users can not be deleted
			if (TenantResource.hasUsers(tenantId)) {
				return Response.status(Status.NOT_ACCEPTABLE)
						.entity("The tenant can not be deleted, because it have users binding with it.").build();
			}

			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/projects/" + tenantId;

			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpDelete httpDelete = new HttpDelete(dfRestUrl);
				httpDelete.addHeader("Content-type", "application/json");
				httpDelete.addHeader("Authorization", "bearer " + token);

				logger.info("deleteTenant -> delete start");
				CloseableHttpResponse response1 = httpclient.execute(httpDelete);

				try {
					Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantId);

					int statusCode = response1.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						TenantPersistenceWrapper.deleteTenant(tenantId);
						logger.info("deleteTenant -> delete successfully");
					}
					String bodyStr = EntityUtils.toString(response1.getEntity());

					return Response.ok().entity(new TenantBean(tenant, bodyStr)).build();
				} finally {
					response1.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("deleteTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private static boolean hasInstances(String tenantId) {
		List<ServiceInstance> instances = ServiceInstancePersistenceWrapper.getServiceInstancesInTenant(tenantId);
		if (instances.size() > 0) {
			return true;
		}
		return false;
	}

	private static boolean hasUsers(String tenantId) {
		List<UserRoleView> users = UserRoleViewPersistenceWrapper.getUsersInTenant(tenantId);
		// if the user list == 1 and it is admin
		// the tenant can be deleted
		if (users.size() == 1 && users.get(0).getUserName().equals(Constant.ADMIN)) {
			return false;
		} else {
			// if the size >= 1 the tenant have user
			if (users.size() >= 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Assign role to user in tenant
	 *
	 * @param tenantId
	 * @param assignment
	 * @return
	 */
	@POST
	@Path("{id}/user/role/assignment")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignRoleToUserInTenant(@PathParam("id") String tenantId, TenantUserRoleAssignment assignment,
			@Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			// assgin to the input tenant
			assignment.setTenantId(tenantId);

			// get all service instances from df
			String allServiceInstances = TenantUtils.getTenantAllServiceInstancesFromDf(tenantId);
			JsonElement allServiceInstancesJson = new JsonParser().parse(allServiceInstances);

			JsonArray allServiceInstancesArray = allServiceInstancesJson.getAsJsonObject().getAsJsonArray("items");
			for (int i = 0; i < allServiceInstancesArray.size(); i++) {
				TenantResourceAssignRoleExecutor runnable = new TenantResourceAssignRoleExecutor(tenantId,
						allServiceInstancesArray, assignment, i);
				Thread thread = new Thread(runnable);
				thread.start();

			}

			assignment = TURAssignmentPersistenceWrapper.assignRoleToUserInTenant(assignment);

			return Response.ok().entity(assignment).build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("assignRoleToUserInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

	/**
	 * Update user role in tenant
	 *
	 * @param tenantId
	 * @param assignment
	 * @return
	 */
	@PUT
	@Path("{id}/user/role/assignment")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRoleToUserInTenant(@PathParam("id") String tenantId, TenantUserRoleAssignment assignment,
			@Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			// assgin to the input tenant
			assignment.setTenantId(tenantId);

			// get all service instances from df
			String allServiceInstances = TenantUtils.getTenantAllServiceInstancesFromDf(tenantId);
			JsonElement allServiceInstancesJson = new JsonParser().parse(allServiceInstances);

			JsonArray allServiceInstancesArray = allServiceInstancesJson.getAsJsonObject().getAsJsonArray("items");
			for (int i = 0; i < allServiceInstancesArray.size(); i++) {
				TenantResourceUpdateRoleExecutor runnable = new TenantResourceUpdateRoleExecutor(tenantId,
						allServiceInstancesArray, assignment, i);
				Thread thread = new Thread(runnable);
				thread.start();
			}

			assignment = TURAssignmentPersistenceWrapper.updateRoleToUserInTenant(assignment);

			return Response.ok().entity(assignment).build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("updateRoleToUserInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

	/**
	 * Unassign role to user in tenant
	 *
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@DELETE
	@Path("{id}/user/{userId}/role/assignment")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response unassignRoleFromUserInTenant(@PathParam("id") String tenantId, @PathParam("userId") String userId,
			@Context HttpServletRequest request) {

		try {
			String loginUser = TokenPaserUtils.paserUserName(getToken(request));
			if (!isSysadmin(loginUser)) {
				UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser, tenantId);
				if (!privileged(role)) {
					logger.error("Current user " + loginUser + " has no privilege on tenant " + tenantId
							+ ", coz of role: " + (role == null ? "Null" : role.getRoleName()));
					return Response.status(Status.UNAUTHORIZED)
							.entity(new ResourceResponseBean("operation failed",
									"Current user has no privilege to do the operations.",
									ResponseCodeConstant.NO_PERMISSION_ON_TENANT))
							.build();
				}
			}

			// get all service instances from df
			String allServiceInstances = TenantUtils.getTenantAllServiceInstancesFromDf(tenantId);
			JsonElement allServiceInstancesJson = new JsonParser().parse(allServiceInstances);

			JsonArray allServiceInstancesArray = allServiceInstancesJson.getAsJsonObject().getAsJsonArray("items");
			for (int i = 0; i < allServiceInstancesArray.size(); i++) {
				TenantResourceUnAssignRoleExecutor runnable = new TenantResourceUnAssignRoleExecutor(tenantId,
						allServiceInstancesArray, userId, i);
				Thread thread = new Thread(runnable);
				thread.start();
			}

			TURAssignmentPersistenceWrapper.unassignRoleFromUserInTenant(tenantId, userId);

			return Response.ok().entity(new ResourceResponseBean("delete success", userId, 200)).build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("unassignRoleFromUserInTenant hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

}
