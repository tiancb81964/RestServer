package com.asiainfo.ocmanager.rest.resource.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.AdapterResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.TenantResource;
import com.asiainfo.ocmanager.rest.resource.utils.ServiceRolePermissionWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.UserRoleViewPersistenceWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TenantResourceCreateInstanceBindingExecutor implements Runnable {

	private static Logger logger = Logger.getLogger(TenantResource.class);

	private String tenantId;
	private String serviceName;
	private String instanceName;

	public TenantResourceCreateInstanceBindingExecutor(String tenantId, String serviceName, String instanceName) {
		this.tenantId = tenantId;
		this.serviceName = serviceName;
		this.instanceName = instanceName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public void run() {

		try {
			List<UserRoleView> users = UserRoleViewPersistenceWrapper.getUsersInTenant(tenantId);

			for (UserRoleView u : users) {
				ServiceRolePermission permission = ServiceRolePermissionWrapper
						.getServicePermissionByRoleId(serviceName, u.getRoleId());
				// only the has service permission users
				// can be assign
				if (!(permission == null)) {
					String getInstanceResBody = TenantResource.getTenantServiceInstancesFromDf(tenantId, instanceName);
					JsonElement OCDPServiceInstanceJson = new JsonParser().parse(getInstanceResBody);
					JsonObject provisioning = OCDPServiceInstanceJson.getAsJsonObject().getAsJsonObject("spec")
							.getAsJsonObject("provisioning");

					provisioning.getAsJsonObject("parameters").addProperty("user_name", u.getUserName());
					logger.debug("createServiceInstanceInTenant -> permission.getServicePermission(): "
							+ permission.getServicePermission());
					provisioning.getAsJsonObject("parameters").addProperty("accesses",
							permission.getServicePermission());
					JsonObject status = OCDPServiceInstanceJson.getAsJsonObject().getAsJsonObject("status");
					status.addProperty("patch", Constant.UPDATE);

					logger.info("createServiceInstanceInTenant -> begin update service instance");
					AdapterResponseBean updateRes = TenantResource.updateTenantServiceInstanceInDf(tenantId,
							instanceName, OCDPServiceInstanceJson.toString());

					if (updateRes.getResCodel() == 200) {

						logger.info("createServiceInstanceInTenant -> wait update complete");
						TenantResource.watiInstanceUpdateComplete(updateRes, tenantId, instanceName);
						logger.info("createServiceInstanceInTenant -> update complete");

						logger.info("createServiceInstanceInTenant -> begin to binding");
						AdapterResponseBean bindingRes = TenantResource.generateOCDPServiceCredentials(tenantId,
								instanceName, u.getUserName());

						if (bindingRes.getResCodel() == 201) {
							logger.info("createServiceInstanceInTenant -> wait binding complete");
							TenantResource.watiInstanceBindingComplete(bindingRes, tenantId, instanceName);
							logger.info("createServiceInstanceInTenant -> binding complete");
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
