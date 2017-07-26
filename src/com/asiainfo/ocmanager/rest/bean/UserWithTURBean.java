package com.asiainfo.ocmanager.rest.bean;

import java.util.List;

import com.asiainfo.ocmanager.persistence.model.User;

public class UserWithTURBean extends User {

	private List<UserRoleViewBean> urv;

	public UserWithTURBean() {

	}

	public UserWithTURBean(User user) {
		super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(),
				user.getDescription(), user.getCreatedUser());

	}

	public List<UserRoleViewBean> getUrv() {
		return urv;
	}

	public void setUrv(List<UserRoleViewBean> urv) {
		this.urv = urv;
	}

}
