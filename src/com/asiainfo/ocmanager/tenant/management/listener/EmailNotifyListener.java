package com.asiainfo.ocmanager.tenant.management.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TURAssignmentPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserPersistenceWrapper;
import com.asiainfo.ocmanager.service.client.MailClient;
import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.LifetimeFlag;
import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Sending email to tenant admin
 * 
 * @author Ethan
 *
 */
public class EmailNotifyListener implements Listener {
	private static final Logger LOG = LoggerFactory.getLogger(EmailNotifyListener.class);
	private static final String SUBJECT = "CM平台-系统通知-租户生命周期告警";

	@Override
	public void handleDue(TenantEvent e) {
		List<User> admins = getAdmins(e);
		if (admins == null || admins.isEmpty()) {
			LOG.warn("Can not send email due to no tenant-admin found in tenant: " + e.getTenant().getName());
			return;
		}
		admins.forEach(a -> {
			String toEmail = a.getEmail();
			if (Strings.isNullOrEmpty(toEmail)) {
				LOG.warn("Can not send email due to email address is null for user: " + a.getUsername());
				return;
			}
			List<ServiceInstance> instances = ServiceInstancePersistenceWrapper
					.getServiceInstancesInTenant(e.getTenant().getId());
			List<TenantUserRoleAssignment> userList = TURAssignmentPersistenceWrapper.getUsers(e.getTenant().getId());
			String textbody = getTextBody(e, a, instances, userList);
			MailClient.getInstance().sendMsgs(toEmail, SUBJECT, textbody);
			LOG.info("Mail been sent to: " + toEmail + ", notifying event: " + e);
		});
	}

	private String getTextBody(TenantEvent e, User admin, List<ServiceInstance> instances,
			List<TenantUserRoleAssignment> users) {
		List<String> bsiList = Lists.transform(instances, new Function<ServiceInstance, String>() {

			@Override
			public String apply(ServiceInstance input) {
				return input.getInstanceName();
			}
		});
		List<String> userList = Lists.transform(users, new Function<TenantUserRoleAssignment, String>() {

			@Override
			public String apply(TenantUserRoleAssignment input) {
				User user = UserPersistenceWrapper.getUserById(input.getUserId());
				return user.getUsername();
			}
		});
		BodyTemplateBuilder builder = new BodyTemplateBuilder(e.getFlag()).withAdmin(admin.getUsername())
				.withCreateTime(e.getTenant().getCreateTime()).withDueTime(e.getTenant().getDueTime())
				.withTenant(e.getTenant().getName())
				.withInstances(String.join(",", bsiList.toArray(new String[bsiList.size()])))
				.withUsers(String.join(",", userList.toArray(new String[userList.size()])));
		return builder.toString();
	}

	private List<User> getAdmins(TenantEvent e) {
		List<TenantUserRoleAssignment> turs = TURAssignmentPersistenceWrapper.getTenantAdmin(e.getTenant().getId());
		if (turs == null) {
			return null;
		}
		List<User> users = Lists.newArrayList();
		turs.forEach(t -> {
			User user = UserPersistenceWrapper.getUserById(t.getUserId());
			users.add(user);
		});
		return users;
	}

	/**
	 * Builders to build Tenant-Lifetime-Management notification text body.
	 * 
	 * @author Ethan
	 *
	 */
	private static class BodyTemplateBuilder {
		private String TEMP = "<font size=\"3\">${tenantAdmin}, 您好：</font>"
				+ "<font size=\"3\"><br>&nbsp;&nbsp;&nbsp;&nbsp;您的下列租户${event}，过期后CM平台将回收该租户下所有实例的使用权限，为了不影响您的正常使用，请及时联系CM平台系统管理员进行续期。</br></font>"
				+ "<p></p>" + "<table border=\"1\" cellspacing=\"0\">"
				+ "<tr><td width=\"100px\">租户名</td><td>${tenantName}</td></tr>"
				+ "<tr><td width=\"100px\">创建时间</td><td>${createTime}</td></tr>"
				+ "<tr><td width=\"100px\">过期时间</td><td>${dueTime}</td></tr>"
				+ "<tr><td width=\"100px\">实例列表</td><td>${instances}</td></tr>"
				+ "<tr><td width=\"100px\">用户列表</td><td>${users}</td></tr>" + "</table>" + "<p></p>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;<font size=\"3\" color=\"red\"><b>发件人不会阅读此邮件，请不要直接回复此邮件</b></font>";
		private static final String TAG_ABT_DUE = "<font color=\"blue\">即将过期</font>";
		private static final String TAG_DUE = "<font color=\"red\">已过期</font>";

		public BodyTemplateBuilder(LifetimeFlag flag) {
			switch (flag) {
			case DUE:
				this.TEMP = this.TEMP.replace("${event}", TAG_DUE);
				break;
			case ABT_DUE:
				this.TEMP = this.TEMP.replace("${event}", TAG_ABT_DUE);
				break;
			default:
				LOG.error("Unknown LifetimeFlag: " + flag);
				break;
			}
		}

		public BodyTemplateBuilder withAdmin(String adminName) {
			this.TEMP = this.TEMP.replace("${tenantAdmin}", adminName);
			return this;
		}

		public BodyTemplateBuilder withTenant(String tenantName) {
			this.TEMP = this.TEMP.replace("${tenantName}", tenantName);
			return this;
		}

		public BodyTemplateBuilder withCreateTime(String createTime) {
			this.TEMP = this.TEMP.replace("${createTime}", createTime);
			return this;
		}

		public BodyTemplateBuilder withDueTime(String dueTime) {
			this.TEMP = this.TEMP.replace("${dueTime}", dueTime);
			return this;
		}

		public BodyTemplateBuilder withInstances(String instances) {
			this.TEMP = this.TEMP.replace("${instances}", instances);
			return this;
		}

		public BodyTemplateBuilder withUsers(String users) {
			this.TEMP = this.TEMP.replace("${users}", users);
			return this;
		}

		public String toString() {
			return this.TEMP;
		}
	}
}
