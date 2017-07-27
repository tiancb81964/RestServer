package com.asiainfo.ocmanager.rest.bean;

/**
 * Quota of certain resource.
 * 
 * @author EthanWang
 *
 */
public class QuotaBean {
	private String name;
	private long size;
	private long used;
	private long available;
	private String desc;

	public QuotaBean(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public void setAvailable(long available) {
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public long getUsed() {
		return used;
	}

	public long getAvailable() {
		return available;
	}

	public String getDesc() {
		return desc;
	}

}
