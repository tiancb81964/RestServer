package com.asiainfo.ocmanager.tenant.management.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.resource.executor.TenantResourceUnAssignRoleExecutor;
import com.asiainfo.ocmanager.rest.resource.persistence.TURAssignmentPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.asiainfo.ocmanager.tenant.management.LifetimeManager;
import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Default policy for tenant lifetime management, which unbind user-list of
 * out-date tenant and set tenant.status to <code>deactive</code>.
 * 
 * @author Ethan
 *
 */
public class DefaultListener implements Listener {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultListener.class);

	@Override
	public void handleDue(TenantEvent e) {
		switch (e.getFlag()) {
		case DUE:
			dealDue(e);
			break;
		case ABT_DUE:
			dealABTDue(e);
			break;
		default:
			LOG.error("Unknown LifetimeFlag: " + e.getFlag());
			break;
		}
	}

	private void dealABTDue(TenantEvent e) {
		// doing nothing
	}

	private void dealDue(TenantEvent e) {
		unbindUserList(e);
		updateStatus(e);
	}

	private void unbindUserList(TenantEvent tenant) {
		try {
			List<UserRoleView> userList = UserRoleViewPersistenceWrapper.getUsersInTenant(tenant.getTenant().getId());
			String allServiceInstances = TenantUtils.getTenantAllServiceInstancesFromDf(tenant.getTenant().getId());
			JsonElement allServiceInstancesJson = new JsonParser().parse(allServiceInstances);
			JsonArray allServiceInstancesArray = allServiceInstancesJson.getAsJsonObject().getAsJsonArray("items");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Users that will be unbound from tenant [{}]: [{}]", tenant.getTenant().getName(), userList);
				LOG.debug("Instances that will be unbound with userList: [{}]", allServiceInstancesArray);
			}
			userList.forEach(u -> {
				for (int i = 0; i < allServiceInstancesArray.size(); i++) {
					JsonObject instance = allServiceInstancesArray.get(i).getAsJsonObject();
					if (LOG.isDebugEnabled()) {
						LOG.debug("Unbinding user [{}] with instance [{}] of tenant [{}]", u.getUserName(),
								serviceName(instance), tenant.getTenant().getName());
					}
					TenantResourceUnAssignRoleExecutor runnable = new TenantResourceUnAssignRoleExecutor(
							tenant.getTenant().getId(), instance, u.getUserId());
					Thread thread = new Thread(runnable, "Unbinding-Thread-" + tenant.getTenant().getName() + "-"
							+ serviceName(instance) + "-" + u.getUserName());
					thread.start();
				}
				// tenant admin should not be removed from userlist, so it can be notified by
				// Email
				if (!u.getRoleName().equalsIgnoreCase("tenant.admin")) {
					TURAssignmentPersistenceWrapper.unassignRoleFromUserInTenant(tenant.getTenant().getId(),
							u.getUserId());
				}
			});
		} catch (Exception e) {
			LOG.error("Exception while unbinding users from tenant: " + tenant.getTenant().getName(), e);
			throw new RuntimeException("Exception while unbinding users from tenant: " + tenant.getTenant().getName(),
					e);
		}
	}

	private String serviceName(JsonObject instance) {
		String serviceName = instance.getAsJsonObject("spec").getAsJsonObject("provisioning").get("backingservice_name")
				.getAsString();
		return serviceName;
	}

	private void updateStatus(TenantEvent e) {
		Tenant t = e.getTenant();
		t.setStatus(LifetimeManager.DEACTIVE);
		TenantPersistenceWrapper.updateStatus(t);
		LOG.info("Tenant status been set to deactive: " + t.getName());
	}

}
