package com.asiainfo.ocmanager.rest.resource.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceRolePermissionWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantJsonParserUtils;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantResourceAssignRoleExecutor implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(TenantResourceAssignRoleExecutor.class);

	private String tenantId;
	private int instnaceNum;
	private JsonArray allServiceInstancesArray;
	private TenantUserRoleAssignment assignment;

	public TenantResourceAssignRoleExecutor(String tenantId, JsonArray allServiceInstancesArray,
			TenantUserRoleAssignment assignment, int instnaceNum) {
		this.tenantId = tenantId;
		this.allServiceInstancesArray = allServiceInstancesArray;
		this.assignment = assignment;
		this.instnaceNum = instnaceNum;
	}

	public int getInstnaceNum() {
		return instnaceNum;
	}

	public void setInstnaceNum(int instnaceNum) {
		this.instnaceNum = instnaceNum;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
			// TODO should consider the resource version changed need to
			// call get instance by id
			JsonObject instance = allServiceInstancesArray.get(instnaceNum).getAsJsonObject();
			// get service name
			String serviceName = instance.getAsJsonObject("spec").getAsJsonObject("provisioning")
					.get("backingservice_name").getAsString();

			String phase = instance.getAsJsonObject("status").get("phase").getAsString();
			if (!phase.equals(Constant.PROVISIONING) && !phase.equals(Constant.FAILURE)) {
				// Because the Provisioning will make the update failed
				// TODO toLowerCase() it the hard code, should change when add
				// add service permission
				if (Constant.list.contains(serviceName.toLowerCase())) {
					// get service instance name
					String instanceName = instance.getAsJsonObject("metadata").get("name").getAsString();

					// get the service permission based on the service name
					// and role
					// TODO toLowerCase() it the hard code, should change when
					// add add service permission
					ServiceRolePermission permission = ServiceRolePermissionWrapper
							.getServicePermissionByRoleId(serviceName.toLowerCase(), assignment.getRoleId());

					// only the has service permission users
					// can be assign
					if (permission != null) {
						// add the user name to the parameters for update
						String userName = UserPersistenceWrapper.getUserById(assignment.getUserId()).getUsername();
						JsonElement patch = TenantJsonParserUtils.getPatchString(tenantId, instanceName);
						// only update success, then do binding
						if (patch == null) {
							logger.info(
									"TenantResourceAssignRoleExecutor -> begin to binding with user: {} on instance: {}",
									userName, instanceName);
							ResourceResponseBean bindingRes = TenantUtils.generateOCDPServiceCredentials(tenantId,
									instanceName, userName, permission.getServicePermission());
							if (bindingRes.getResCodel() == 201) {
								logger.info(
										"TenantResourceAssignRoleExecutor -> binding successfully with user: {} on instance: {}",
										userName, instanceName);
							} else {
								logger.info(
										"TenantResourceAssignRoleExecutor -> binding falied with user: {} on instance: {}",
										userName, instanceName);
							}
						} else {
							logger.info(
									instanceName
											+ " TenantResourceAssignRoleExecutor -> The instance is updating or failed with user: {} on instance: {}"
											+ " please make sure there is NOT operations on the instance.",
									userName, instanceName);
						}
					}
					else {
						logger.warn("Privilege schema is null for service [{}] of role [{}] by user [{}]", serviceName.toLowerCase(), assignment.getRoleId(),assignment.getUserId());
					}
				}
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("TenantResourceAssignRoleExecutor -> ", e);

		}
	}

}
