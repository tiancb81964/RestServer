package com.asiainfo.ocmanager.persistence.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class User {
	private String id;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String description;
	private String createdUser;
	private String createTime;
	private int platformRoleId;

	// TODO should use builder designer
	public User() {

	}

	public User(String id, String username, String password, String email, String phone, String description) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.description = description;
	}

	public User(String id, String username, String password, String email, String phone, String description,
			String createdUser) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.description = description;
		this.createdUser = createdUser;
	}

	// update user
	public User(String id, String username, String email, String phone, String description, String createdUser,
			int platformRoleId) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.description = description;
		this.createdUser = createdUser;
		this.platformRoleId = platformRoleId;
	}

	// create user
	public User(String id, String username, String password, String email, String phone, String description,
			String createdUser, int platformRoleId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.description = description;
		this.createdUser = createdUser;
		this.platformRoleId = platformRoleId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getPlatformRoleId() {
		return platformRoleId;
	}

	public void setPlatformRoleId(int platformRoleId) {
		this.platformRoleId = platformRoleId;
	}

	public String toString() {
		return "id:" + id + " username:" + username + " password:" + password + " email:" + email + " phone:" + phone
				+ " description:" + description + " createdUser:" + createdUser + " createTime:" + createTime
				+ " platformRoleId:" + platformRoleId;
	}

}
