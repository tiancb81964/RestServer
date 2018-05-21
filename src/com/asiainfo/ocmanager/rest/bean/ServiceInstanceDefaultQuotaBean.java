package com.asiainfo.ocmanager.rest.bean;

import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceDefaultQuotaBean {
	private double defaultQuota;
	private double max;
	private double price;
	private double step;
	private String desc;

	public ServiceInstanceDefaultQuotaBean() {
	}

	public ServiceInstanceDefaultQuotaBean(JsonObject jsonObj) {
		// if null set the default value
		this.defaultQuota = jsonObj.get("default") == null ? 1.0d : jsonObj.get("default").getAsDouble();
		this.max = jsonObj.get("max") == null ? 1.0d : jsonObj.get("max").getAsDouble();
		this.price = jsonObj.get("price") == null ? 1.0d : jsonObj.get("price").getAsDouble();
		this.step = jsonObj.get("step") == null ? 1.0d : jsonObj.get("step").getAsDouble();
		this.desc = jsonObj.get("desc") == null ? "CatalogDoNotHaveDesc" : jsonObj.get("desc").getAsString();
	}

	public double getDefaultQuota() {
		return defaultQuota;
	}

	public void setDefaultQuota(double defaultQuota) {
		this.defaultQuota = defaultQuota;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "ServiceQuotaDetailsBean [defaultQuota: " + defaultQuota + ", max: " + max + ", price: " + price
				+ ", step: " + step + ", desc: " + desc + "]";
	}
}
