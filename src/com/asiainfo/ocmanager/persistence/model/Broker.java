package com.asiainfo.ocmanager.persistence.model;

public class Broker {
	private String broker_id;
	private String broker_name;
	private String broker_image;
	private String broker_url;
	private String binded_cluster;
	private String dc_name;
	
	public Broker(String broker_id, String broker_name, String broker_image, String broker_url, String binded_cluster,
			String dc_name) {
		super();
		this.broker_id = broker_id;
		this.broker_name = broker_name;
		this.broker_image = broker_image;
		this.broker_url = broker_url;
		this.binded_cluster = binded_cluster;
		this.dc_name = dc_name;
	}
	public String getBroker_id() {
		return broker_id;
	}
	public void setBroker_id(String broker_id) {
		this.broker_id = broker_id;
	}
	public String getBroker_name() {
		return broker_name;
	}
	public void setBroker_name(String broker_name) {
		this.broker_name = broker_name;
	}
	public String getBroker_image() {
		return broker_image;
	}
	public void setBroker_image(String broker_image) {
		this.broker_image = broker_image;
	}
	public String getBroker_url() {
		return broker_url;
	}
	public void setBroker_url(String broker_url) {
		this.broker_url = broker_url;
	}
	public String getBinded_cluster() {
		return binded_cluster;
	}
	public void setBinded_cluster(String binded_cluster) {
		this.binded_cluster = binded_cluster;
	}
	public String getDc_name() {
		return dc_name;
	}
	public void setDc_name(String dc_name) {
		this.dc_name = dc_name;
	}
}
