package com.asiainfo.ocmanager.rest.resource.executor;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceRolePermissionWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantResourceUpdateRoleExecutor implements Runnable {

	private static Logger logger = Logger.getLogger(TenantResourceUpdateRoleExecutor.class);

	private String tenantId;
	private int instnaceNum;
	private JsonArray allServiceInstancesArray;
	private TenantUserRoleAssignment assignment;

	public TenantResourceUpdateRoleExecutor(String tenantId, JsonArray allServiceInstancesArray,
			TenantUserRoleAssignment assignment, int instnaceNum) {
		this.tenantId = tenantId;
		this.allServiceInstancesArray = allServiceInstancesArray;
		this.assignment = assignment;
		this.instnaceNum = instnaceNum;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public int getInstnaceNum() {
		return instnaceNum;
	}

	public void setInstnaceNum(int instnaceNum) {
		this.instnaceNum = instnaceNum;
	}

	public JsonArray getAllServiceInstancesArray() {
		return allServiceInstancesArray;
	}

	public void setAllServiceInstancesArray(JsonArray allServiceInstancesArray) {
		this.allServiceInstancesArray = allServiceInstancesArray;
	}

	public TenantUserRoleAssignment getAssignment() {
		return assignment;
	}

	public void setAssignment(TenantUserRoleAssignment assignment) {
		this.assignment = assignment;
	}

	@Override
	public void run() {
		try {
			JsonObject instance = allServiceInstancesArray.get(instnaceNum).getAsJsonObject();
			// get service name
			String serviceName = instance.getAsJsonObject("spec").getAsJsonObject("provisioning")
					.get("backingservice_name").getAsString();
			String phase = instance.getAsJsonObject("status").get("phase").getAsString();

			if (!phase.equals(Constant.PROVISIONING) && !phase.equals(Constant.FAILURE)) {
				if (Constant.list.contains(serviceName.toLowerCase())) {

					// get service instance name
					String instanceName = instance.getAsJsonObject("metadata").get("name").getAsString();
					String userName = UserPersistenceWrapper.getUserById(assignment.getUserId()).getUsername();

					logger.info("updateRoleToUserInTenant -> begin to unbinding");
					ResourceResponseBean unBindingRes = TenantUtils.removeOCDPServiceCredentials(tenantId, instanceName,
							userName);

					if (unBindingRes.getResCodel() == 201) {
						logger.info("updateRoleToUserInTenant -> wait unbinding compelte");
						TenantUtils.watiInstanceUnBindingComplete(unBindingRes, tenantId, instanceName);
						logger.info("updateRoleToUserInTenant -> unbinding compelte");

						String OCDPServiceInstanceStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId,
								instanceName);

						// get the service permission
						ServiceRolePermission permission = ServiceRolePermissionWrapper
								.getServicePermissionByRoleId(serviceName.toLowerCase(), assignment.getRoleId());

						// only the has service permission users
						// can be assign
						if (permission != null) {
							// parse the update request body
							JsonElement OCDPServiceInstanceJson = new JsonParser().parse(OCDPServiceInstanceStr);
							// get the provisioning json
							JsonObject provisioning = OCDPServiceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
									.getAsJsonObject("provisioning");

							provisioning.getAsJsonObject("parameters").addProperty("user_name", userName);

							// add the accesses fields into the request body
							provisioning.getAsJsonObject("parameters").addProperty("accesses",
									permission.getServicePermission());

							// add the patch Updating into the request body
							JsonObject status = OCDPServiceInstanceJson.getAsJsonObject().getAsJsonObject("status");
							status.addProperty("patch", Constant.UPDATE);

							logger.info("updateRoleToUserInTenant -> begin to update");
							ResourceResponseBean updateRes = TenantUtils.updateTenantServiceInstanceInDf(tenantId,
									instanceName, OCDPServiceInstanceJson.toString());

							if (updateRes.getResCodel() == 200) {

								logger.info("updateRoleToUserInTenant -> wait update compete");
								TenantUtils.watiInstanceUpdateComplete(updateRes, tenantId, instanceName);
								logger.info("updateRoleToUserInTenant -> update compete");

								logger.info("updateRoleToUserInTenant -> begin to binding");
								ResourceResponseBean bindingRes = TenantUtils.generateOCDPServiceCredentials(tenantId,
										instanceName, userName);
								if (bindingRes.getResCodel() == 201) {
									logger.info("updateRoleToUserInTenant -> binding successfully");
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("assignRoleToUserInTenant -> " + e.getMessage());

		}
	}

}
