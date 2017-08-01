package com.asiainfo.ocmanager.rest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.AdapterResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceAssignRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceCreateInstanceBindingExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceUnAssignRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceUpdateRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TURAssignmentPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.asiainfo.ocmanager.rest.utils.DFPropertiesFoundry;
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

	private static Logger logger = Logger.getLogger(TenantResource.class);

	/**
	 * Get All OCManager tenants
	 *
	 * @return tenant list
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getAllTenants() {
		try {
			List<Tenant> tenants = TenantPersistenceWrapper.getAllTenants();
			return Response.ok().entity(tenants).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getAllTenants -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantById(@PathParam("id") String tenantId) {
		try {
			Tenant tenant = TenantPersistenceWrapper.getTenantById(tenantId);
			return Response.ok().entity(tenant).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantById -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getChildrenTenants(@PathParam("id") String parentTenantId) {
		try {
			List<Tenant> tenants = TenantPersistenceWrapper.getChildrenTenants(parentTenantId);
			return Response.ok().entity(tenants).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getChildrenTenants -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantUsers(@PathParam("id") String tenantId) {
		try {
			List<UserRoleView> usersRoles = UserRoleViewPersistenceWrapper.getUsersInTenant(tenantId);
			return Response.ok().entity(usersRoles).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantUsers -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
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
			logger.info("getRoleByTenantUserName -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantServiceInstances(@PathParam("id") String tenantId) {
		try {
			List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
					.getServiceInstancesInTenant(tenantId);
			return Response.ok().entity(serviceInstances).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantServiceInstances -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantServiceInstanceAccessInfo(@PathParam("tenantId") String tenantId,
			@PathParam("InstanceName") String InstanceName) {
		try {
			return Response.ok().entity(TenantUtils.getTenantServiceInstancesFromDf(tenantId, InstanceName)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantServiceInstanceAccessInfo -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTenant(Tenant tenant, @Context HttpServletRequest request) {

		if (tenant.getName() == null || tenant.getId() == null) {
			return Response.status(Status.BAD_REQUEST).entity("input format is not correct").build();
		}

		try {
			List<Tenant> allRootTenants = TenantPersistenceWrapper.getAllRootTenants();
			List<String> allRootTenantsId = new ArrayList<String>();
			for (Tenant t : allRootTenants) {
				allRootTenantsId.add(t.getId());
			}

			String url = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
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
				httpPost.setEntity(se);

				logger.info("createTenant -> start create");
				CloseableHttpResponse response2 = httpclient.execute(httpPost);

				try {
					int statusCode = response2.getStatusLine().getStatusCode();

					if (statusCode == 201) {
						logger.info("createTenant -> start successfully");
						// very ugly code here hard code 3 level
						// only for citic should enhance
						if (tenant.getParentId() == null) {
							tenant.setLevel(1);
						} else if (allRootTenantsId.contains(tenant.getParentId())) {
							tenant.setLevel(2);
						} else {
							tenant.setLevel(3);
						}
						TenantPersistenceWrapper.createTenant(tenant);
					}
					String bodyStr = EntityUtils.toString(response2.getEntity());

					return Response.ok().entity(bodyStr).build();
				} finally {
					response2.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("createTenant -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Create a service instance in specific tenant
	 *
	 * @param
	 * @return
	 */
	@POST
	@Path("{id}/service/instance")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createServiceInstanceInTenant(@PathParam("id") String tenantId, String reqBodyStr) {

		try {
			String url = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/namespaces/" + tenantId + "/backingserviceinstances";

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
			logger.info("createServiceInstanceInTenant -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateServiceInstanceInTenant(@PathParam("id") String tenantId,
			@PathParam("instanceName") String instanceName, String parametersStr) {

		try {

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
				for (Map.Entry<String, JsonElement> entry : parameterObj.entrySet()) {
					String key = entry.getKey();
					JsonElement value = entry.getValue();
					// only check quota
					if (Constant.serviceQuotaParam.contains(key)) {
						// if value is not int, will throw Exception
						value.getAsInt();
						logger.info("parameters" + key + ":" + value.toString());
					}
				}
			} catch (Exception e) {
				logger.info("The parameter format check error:" + e.getMessage());
				return Response.status(Status.BAD_REQUEST)
						.entity("BadRequest: the parameter value format is illegal! Error:" + e.toString()).build();
			}

			// add into the update json
			provisioning.add("parameters", parameterObj);

			// add the patch Updating into the request body
			JsonObject status = serviceInstanceJson.getAsJsonObject().getAsJsonObject("status");
			status.addProperty("patch", Constant.UPDATE);

			logger.info("updateServiceInstanceInTenant -> update start");
			AdapterResponseBean responseBean = TenantUtils.updateTenantServiceInstanceInDf(tenantId, instanceName,
					serviceInstanceJson.toString());

			String quota = null;
			if (responseBean.getResCodel() == 200) {
				logger.info("updateServiceInstanceInTenant -> update successfully");
				JsonElement resBodyJson = new JsonParser().parse(responseBean.getMessage());
				JsonObject resBodyJsonObj = resBodyJson.getAsJsonObject();
				if ((resBodyJsonObj.getAsJsonObject("spec").getAsJsonObject("provisioning").get("parameters")
						.isJsonNull())) {
					// TODO should get df service quota
				} else {
					quota = serviceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
							.getAsJsonObject("provisioning").get("parameters").toString();
				}

				ServiceInstancePersistenceWrapper.updateServiceInstanceQuota(tenantId, instanceName, quota);
			}

			return Response.ok().entity(responseBean.getMessage()).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("updateServiceInstanceInTenant -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServiceInstanceInTenant(@PathParam("id") String tenantId,
			@PathParam("instanceName") String instanceName) {

		try {

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
							AdapterResponseBean unBindingRes = TenantUtils.removeOCDPServiceCredentials(tenantId,
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
			String url = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
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
			logger.info("deleteServiceInstanceInTenant -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTenant(@PathParam("id") String tenantId) {
		try {
			// if have instances can not be deleted
			if (TenantResource.hasInstances(tenantId)){
				return Response.status(Status.FORBIDDEN)
						.entity("The tenant can not be deleted, because it have service instances on it.")
						.build();
			}
			// if have users can not be deleted
			if (TenantResource.hasUsers(tenantId)){
				return Response.status(Status.FORBIDDEN)
						.entity("The tenant can not be deleted, because it have users binding with it.")
						.build();
			}
			
			String url = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DFPropertiesFoundry.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
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
					int statusCode = response1.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						TenantPersistenceWrapper.deleteTenant(tenantId);
						logger.info("deleteTenant -> delete successfully");
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
			logger.info("deleteTenant -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignRoleToUserInTenant(@PathParam("id") String tenantId, TenantUserRoleAssignment assignment) {

		try {
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
			logger.info("assignRoleToUserInTenant -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRoleToUserInTenant(@PathParam("id") String tenantId, TenantUserRoleAssignment assignment) {

		try {
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
			logger.info("updateRoleToUserInTenant -> " + e.getMessage());
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
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response unassignRoleFromUserInTenant(@PathParam("id") String tenantId, @PathParam("userId") String userId) {

		try {
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

			return Response.ok().entity(new AdapterResponseBean("delete success", userId, 200)).build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("unassignRoleFromUserInTenant -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}

	}

}
