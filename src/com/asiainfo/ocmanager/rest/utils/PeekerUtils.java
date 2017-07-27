package com.asiainfo.ocmanager.rest.utils;

import com.asiainfo.ocmanager.rest.bean.QuotaBean;
import com.asiainfo.ocmanager.rest.bean.QuotaResponse;
import com.asiainfo.ocmanager.service.broker.ResourcePeeker;

public class PeekerUtils {

	/**
	 * Transform resources usage into response entity. All resources would be
	 * extracted from peeker and be appended to response.
	 * 
	 * @param peeker
	 * @return
	 */
	public static QuotaResponse transform(ResourcePeeker peeker) {
		QuotaResponse response = new QuotaResponse();
		for (String type : peeker.resourceTypes()) {
			for (String resource : peeker.getResourcesByType(type)) {
				QuotaBean bean = new QuotaBean(resource, type);
				bean.setSize(peeker.getTotalQuota(type, resource));
				bean.setUsed(peeker.getUsedQuota(type, resource));
				bean.setAvailable(peeker.getFreeQuota(type, resource));
				response.addItem(bean);
			}
		}
		return response;
	}

}
