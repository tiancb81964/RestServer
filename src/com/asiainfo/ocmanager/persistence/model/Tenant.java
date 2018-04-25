package com.asiainfo.ocmanager.persistence.model;

import java.util.Objects;

/**
 * 
 * @author zhaoyim
 *
 */
public class Tenant {

	private String id;
	private String name;
	private String description;
	private String parentId;
	private int level;
	private String quota;
	private String dueTime;
	private String status;
	private String createTime;

	public Tenant() {

	}

	public Tenant(String id, String name, String description, String parentId, int level, String quota, String dueTime) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.level = level;
		this.quota = quota;
		this.dueTime = dueTime;
	}

	public Tenant(String id, String name, String description, String parentId, int level) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.level = level;
	}

	public Tenant(String id, String name, String description, String parentId, int level, String quota) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentId = parentId;
		this.level = level;
		this.quota = quota;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDueTime() {
		return dueTime;
	}

	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	@Override
	public String toString() {
		return "Tenant [id=" + id + ", name=" + name + ", description=" + description + ", parentId=" + parentId
				+ ", level=" + level + ", quota=" + quota + ", dueTime=" + dueTime + ", status=" + status
				+ ", createTime=" + createTime + "]";
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Tenant other = (Tenant) obj;

		return Objects.equals(name, other.name) && Objects.equals(id, other.id)
				&& Objects.equals(description, other.description) && Objects.equals(parentId, other.parentId)
				&& level == other.level && Objects.equals(quota, other.quota);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, id, description, parentId, level, quota);
	}

}
