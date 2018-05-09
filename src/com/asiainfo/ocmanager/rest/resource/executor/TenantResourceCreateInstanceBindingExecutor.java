package com.asiainfo.ocmanager.rest.resource.executor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceRolePermissionWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantJsonParserUtils;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.google.gson.JsonElement;

public class TenantResourceCreateInstanceBindingExecutor implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(TenantResourceCreateInstanceBindingExecutor.class);

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
						.getServicePermissionByRoleId(serviceName.toLowerCase(), u.getRoleId());
				String userName = u.getUserName();
				// only the has service permission users
				// can be assign
				if (!(permission == null)) {

					JsonElement patch = TenantJsonParserUtils.getPatchString(tenantId, instanceName);
					// only update success, then do binding
					if (patch == null) {
						logger.info(
								"TenantResourceCreateInstanceBindingExecutor -> begin to binding with user: {} on instance: {}",
								userName, instanceName);
						ResourceResponseBean bindingRes = TenantUtils.generateOCDPServiceCredentials(tenantId,
								instanceName, userName, permission.getServicePermission());

						if (bindingRes.getResCodel() == 201) {
							logger.info(
									"TenantResourceCreateInstanceBindingExecutor -> wait binding complete with user: {} on instance: {}",
									userName, instanceName);
							TenantUtils.watiInstanceBindingComplete(bindingRes, tenantId, instanceName);
							logger.info(
									"TenantResourceCreateInstanceBindingExecutor -> binding complete with user: {} on instance: {}",
									userName, instanceName);
						} else {
							logger.info(
									"TenantResourceCreateInstanceBindingExecutor -> binding falied with user: {} on instance: {}",
									userName, instanceName);
						}
					} else {
						logger.info(
								instanceName
										+ " TenantResourceCreateInstanceBindingExecutor -> The instance is updating or failed with user: {} on instance: {},"
										+ " please make sure there is NOT operations on the instance.",
								userName, instanceName);
					}
				}
				logger.warn("Privilege schema is null for service [{}] of role [{}] by user [{}]", serviceName.toLowerCase(), u.getRoleName(), u.getUserName());
			}

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("TenantResourceCreateInstanceBindingExecutor -> ", e);

		}
	}

}
