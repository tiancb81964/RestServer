package com.asiainfo.ocmanager.persistence.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantUserRoleAssignment {
	private String tenantId;
	private String userId;
	private String roleId;

	public TenantUserRoleAssignment() {

	}

	public TenantUserRoleAssignment(String tenantId, String userId, String roleId) {
		this.tenantId = tenantId;
		this.userId = userId;
		this.roleId = roleId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "TenantUserRoleAssignment [tenantId=" + tenantId + ", userId=" + userId + ", roleId=" + roleId + "]";
	}

}
