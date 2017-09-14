package com.asiainfo.ocmanager.rest.bean;

import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceDefaultQuotaBean {
	private long defaultQuota;
	private long max;
	private long price;
	private long step;
	private String desc;

	public ServiceInstanceDefaultQuotaBean() {
	}

	public ServiceInstanceDefaultQuotaBean(JsonObject jsonObj) {
		this.defaultQuota = jsonObj.get("default").getAsLong();
		this.max = jsonObj.get("max").getAsLong();
		this.price = jsonObj.get("price").getAsLong();
		this.step = jsonObj.get("step").getAsLong();
		this.desc = jsonObj.get("desc").getAsString();
	}

	public long getDefaultQuota() {
		return defaultQuota;
	}

	public void setDefaultQuota(long defaultQuota) {
		this.defaultQuota = defaultQuota;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
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
