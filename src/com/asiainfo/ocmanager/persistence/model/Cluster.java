package com.asiainfo.ocmanager.persistence.model;

public class Cluster {
	private String cluster_id;
	private String cluster_name;
	private String cluster_type;
	private String ambari_url;
	private String ambari_user;
	private String ambari_password;

	public Cluster(String cluster_id, String cluster_name, String cluster_type, String ambari_url, String ambari_user,
			String ambari_password) {
		this.cluster_id = cluster_id;
		this.cluster_name = cluster_name;
		this.cluster_type = cluster_type;
		this.ambari_url = ambari_url;
		this.ambari_user = ambari_user;
		this.ambari_password = ambari_password;
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

	public String getAmbari_url() {
		return ambari_url;
	}

	public void setAmbari_url(String ambari_url) {
		this.ambari_url = ambari_url;
	}

	public String getAmbari_user() {
		return ambari_user;
	}

	public void setAmbari_user(String ambari_user) {
		this.ambari_user = ambari_user;
	}

	public String getAmbari_password() {
		return ambari_password;
	}

	public void setAmbari_password(String ambari_password) {
		this.ambari_password = ambari_password;
	}

}
