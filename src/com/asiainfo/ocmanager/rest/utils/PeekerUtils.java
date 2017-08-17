package com.asiainfo.ocmanager.rest.utils;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.ocmanager.rest.bean.QuotaBean;
import com.asiainfo.ocmanager.service.broker.ResourcePeeker;

public class PeekerUtils {

	/**
	 * Transform resources usage into response entity. All resources would be
	 * extracted from peeker and be appended to response.
	 * 
	 * @param peeker
	 * @return
	 */
	public static List<QuotaBean> transform(ResourcePeeker peeker) {
		List<QuotaBean> list = new ArrayList<>();
		for (String type : peeker.resourceTypes()) {
			for (String resource : peeker.getResourcesByType(type)) {
				QuotaBean bean = new QuotaBean(type, resource);
				bean.setSize(peeker.getTotalQuota(type, resource));
				bean.setUsed(peeker.getUsedQuota(type, resource));
				bean.setAvailable(peeker.getFreeQuota(type, resource));
				list.add(bean);
			}
		}
		return list;
	}

}
