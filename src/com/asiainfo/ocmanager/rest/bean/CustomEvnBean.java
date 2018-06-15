package com.asiainfo.ocmanager.rest.bean;

/**
 * Custom environment bean for service broker
 * 
 * @author Ethan
 *
 */
public class CustomEvnBean {
	private String key;
	private String value = "";
	private String description;

	public CustomEvnBean(String key, String value, String description) {
		this.key = key;
		this.value = value;
		this.description = description;
	}

	public CustomEvnBean() {
	}

	public CustomEvnBean(String key, String description) {
		this.key = key;
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
