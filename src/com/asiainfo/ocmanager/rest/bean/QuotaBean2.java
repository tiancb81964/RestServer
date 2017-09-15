package com.asiainfo.ocmanager.rest.bean;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;

public class QuotaBean2 {
	private ServiceType type;
	private Map<String, Long> quotas = new HashMap<>();
	
	public QuotaBean2(ServiceType type) {
		this.type = type;
		for (String key : type.quotaKeys()) {
			quotas.put(key, 0l);
		}
	}
	
	public QuotaBean2(ServiceType type, Map<String, Long> quotas) {
		this.type = type;
		for (String key : type.quotaKeys()) {
			this.quotas.put(key, 0l);
		}
		this.quotas.putAll(quotas);
	}

	public ServiceType getType() {
		return type;
	}

	public Map<String, Long> getQuotas() {
		return quotas;
	}

	public QuotaBean2 plus(QuotaBean2 bean) {
		bean.getQuotas().forEach((k, v) -> {
			long newValue = this.quotas.get(k) + v;
			this.quotas.put(k, newValue);
		});
		return this;
	}
	
	public QuotaBean2 minus(QuotaBean2 bean) {
		bean.getQuotas().forEach((k, v) -> {
			long newValue = this.quotas.get(k) - v;
			this.quotas.put(k, newValue);
		});
		return this;
	}
}
