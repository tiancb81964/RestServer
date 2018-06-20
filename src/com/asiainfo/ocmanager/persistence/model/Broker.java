package com.asiainfo.ocmanager.persistence.model;

public class Broker {
	private long broker_id;
	private String broker_name;
	private String broker_image;
	private String broker_url;
	private String binded_cluster;
	private String dc_name;
	private String latest_status;

	public Broker(long broker_id, String broker_name, String broker_image, String broker_url, String binded_cluster,
			String dc_name, BrokerStatus status) {
		this.broker_id = broker_id;
		this.broker_name = broker_name;
		this.broker_image = broker_image;
		this.broker_url = broker_url;
		this.binded_cluster = binded_cluster;
		this.dc_name = dc_name;
		this.latest_status = status.name();
	}

	public Broker(String broker_name, String broker_image, String binded_cluster, String dc_name, BrokerStatus status) {
		this.broker_name = broker_name;
		this.broker_image = broker_image;
		this.binded_cluster = binded_cluster;
		this.dc_name = dc_name;
		this.latest_status = status.name();
	}

	public long getBroker_id() {
		return broker_id;
	}

	public void setBroker_id(long broker_id) {
		this.broker_id = broker_id;
	}

	public String getBroker_name() {
		return broker_name;
	}

	public void setBroker_name(String broker_name) {
		this.broker_name = broker_name;
	}

	public String getLatest_status() {
		return latest_status;
	}

	public void setLatest_status(String latest_status) {
		this.latest_status = latest_status;
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
	
	public enum BrokerStatus{
		CATALOG_INITIALIZED,
		DC_CREATED,
		SVC_CREATED,
		ROUTER_CREATED,
		DC_INSTANTIATED,
		REGISTERED
	}
}
