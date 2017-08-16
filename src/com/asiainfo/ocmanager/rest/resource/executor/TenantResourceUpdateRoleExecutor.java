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
public class TenantResourceUpdateRoleExecutor implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(TenantResourceUpdateRoleExecutor.class);

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

					logger.info("TenantResourceUpdateRoleExecutor -> begin to unbinding with user: {} on instance: {}",
							userName, instanceName);
					ResourceResponseBean unBindingRes = TenantUtils.removeOCDPServiceCredentials(tenantId, instanceName,
							userName);

					if (unBindingRes.getResCodel() == 201) {
						logger.info(
								"TenantResourceUpdateRoleExecutor -> wait unbinding compelte with user: {} on instance: {}",
								userName, instanceName);
						TenantUtils.watiInstanceUnBindingComplete(unBindingRes, tenantId, instanceName);
						logger.info(
								"TenantResourceUpdateRoleExecutor -> unbinding compelte with user: {} on instance: {}",
								userName, instanceName);

						// get the service permission
						ServiceRolePermission permission = ServiceRolePermissionWrapper
								.getServicePermissionByRoleId(serviceName.toLowerCase(), assignment.getRoleId());

						// only the has service permission users
						// can be assign
						if (permission != null) {

							JsonElement patch = TenantJsonParserUtils.getPatchString(tenantId, instanceName);
							// only update success, then do binding
							if (patch == null) {
								logger.info(
										"TenantResourceUpdateRoleExecutor -> begin to binding with user: {} on instance: {}",
										userName, instanceName);
								ResourceResponseBean bindingRes = TenantUtils.generateOCDPServiceCredentials(tenantId,
										instanceName, userName, permission.getServicePermission());
								if (bindingRes.getResCodel() == 201) {
									logger.info(
											"TenantResourceUpdateRoleExecutor -> binding successfully with user: {} on instance: {}",
											userName, instanceName);
								} else {
									logger.info(
											"TenantResourceUpdateRoleExecutor -> binding falied with user: {} on instance: {}",
											userName, instanceName);
								}
							} else {
								logger.info(
										instanceName
												+ " TenantResourceUpdateRoleExecutor -> The instance is updating or failed with user: {} on instance: {}"
												+ " please make sure there is NOT operations on the instance.",
										userName, instanceName);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("TenantResourceUpdateRoleExecutor -> ", e);

		}
	}

}
