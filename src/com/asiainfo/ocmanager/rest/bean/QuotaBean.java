package com.asiainfo.ocmanager.rest.bean;

/**
 * Quota usage of certain resource.
 * 
 * @author EthanWang
 *
 */
public class QuotaBean {
	private String name;
	private String size;
	private String used;
	private String available;
	private String resource;

	public QuotaBean(String name, String resourcePath) {
		this.name = name;
		this.resource = resourcePath;
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

	public String getResource() {
		return resource;
	}

	@Override
	public String toString() {
		return "QuotaBean [name=" + name + ", size=" + size + ", used=" + used + ", available=" + available + ", resource="
				+ resource + "]";
	}

}
