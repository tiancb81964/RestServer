package com.asiainfo.ocmanager.rest.bean;

/**
 * 
 * @author zhaoyim
 *
 */
public class IsAdminBean {
	private String userName;
	private boolean isAdmin;

	public IsAdminBean() {

	}

	public IsAdminBean(boolean isAdmin, String userName) {
		this.userName = userName;
		this.isAdmin = isAdmin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "IsAdminBean [isAdmin: " + isAdmin + ", userName: " + userName + "]";
	}
}
