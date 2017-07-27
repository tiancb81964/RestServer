package com.asiainfo.ocmanager.rest.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Response body of resource monitor requests.
 * 
 * @author EthanWang
 *
 */
public class QuotaResponse {
	private List<QuotaBean> items;

	public QuotaResponse() {
		this.items = new ArrayList<>();
	}

	public QuotaResponse addItem(QuotaBean bean) {
		this.items.add(bean);
		return this;
	}

}
