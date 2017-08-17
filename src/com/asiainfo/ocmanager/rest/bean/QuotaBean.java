package com.asiainfo.ocmanager.rest.bean;

/**
 * Quota of certain resource.
 * 
 * @author EthanWang
 *
 */
public class QuotaBean {
	private String name;
	private String size;
	private String used;
	private String available;
	private String desc;

	public QuotaBean(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public String getSize() {
		return size;
	}

	public String getUsed() {
		return used;
	}

	public String getAvailable() {
		return available;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return "QuotaBean [name=" + name + ", size=" + size + ", used=" + used + ", available=" + available + ", desc="
				+ desc + "]";
	}

}
