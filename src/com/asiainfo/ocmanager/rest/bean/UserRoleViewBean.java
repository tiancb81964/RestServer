package com.asiainfo.ocmanager.rest.bean;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;

public class UserRoleViewBean extends UserRoleView {

	private String parentTenantName;

	public UserRoleViewBean(UserRoleView urv) {
		super.setUserId(urv.getUserId());
		super.setUserName(urv.getUserName());
		super.setUserDescription(urv.getUserDescription());
		super.setUserEmail(urv.getUserEmail());
		super.setUserPhone(urv.getUserPhone());
		super.setUserPassword(urv.getUserPassword());
		super.setRoleId(urv.getRoleId());
		super.setRoleName(urv.getRoleName());
		super.setTenantId(urv.getTenantId());
		super.setTenantName(urv.getTenantName());
		super.setPermission(urv.getPermission());
	}

	public String getParentTenantName() {
		return parentTenantName;
	}

	public void setParentTenantName(String parentTenantName) {
		this.parentTenantName = parentTenantName;
	}

}
