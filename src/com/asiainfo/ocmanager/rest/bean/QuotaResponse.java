package com.asiainfo.ocmanager.rest.bean;

import java.util.List;

public class QuotaResponse {
	private List<QuotaBean> items;
 
 	public QuotaResponse(List<QuotaBean> items) {
 		this.items = items;
 	}
 
	public List<QuotaBean> getItems() {
		return items;
	}

	public void setItems(List<QuotaBean> items) {
		this.items = items;
	}
}
