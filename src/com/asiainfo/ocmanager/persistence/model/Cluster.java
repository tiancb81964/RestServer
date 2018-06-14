package com.asiainfo.ocmanager.persistence.model;

public class Cluster {
	private String cluster_id;
	private String cluster_name;
	private String cluster_type;
	private String cluster_url;
	private String cluster_admin;
	private String cluster_password;

	public Cluster() {
	}

	public Cluster(String cluster_id, String cluster_name, String cluster_type, String cluster_url, String cluster_admin,
			String cluster_password) {
		this.cluster_id = cluster_id;
		this.cluster_name = cluster_name;
		this.cluster_type = cluster_type;
		this.cluster_url = cluster_url;
		this.cluster_admin = cluster_admin;
		this.cluster_password = cluster_password;
	}

	public String getCluster_id() {
		return cluster_id;
	}

	public void setCluster_id(String cluster_id) {
		this.cluster_id = cluster_id;
	}

	public String getCluster_name() {
		return cluster_name;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

	public String getCluster_type() {
		return cluster_type;
	}

	public void setCluster_type(String cluster_type) {
		this.cluster_type = cluster_type;
	}

	public String getCluster_url() {
		return cluster_url;
	}

	public void setCluster_url(String cluster_url) {
		this.cluster_url = cluster_url;
	}

	public String getCluster_admin() {
		return cluster_admin;
	}

	public void setCluster_admin(String cluster_admin) {
		this.cluster_admin = cluster_admin;
	}

	public String getCluster_password() {
		return cluster_password;
	}

	public void setCluster_password(String cluster_password) {
		this.cluster_password = cluster_password;
	}
}
