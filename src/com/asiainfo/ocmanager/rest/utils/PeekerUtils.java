package com.asiainfo.ocmanager.rest.utils;

import java.util.ArrayList;
import java.util.List;

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
		List<QuotaBean> list = new ArrayList<>();
		for (String type : peeker.resourceTypes()) {
			for (String resource : peeker.getResourcesByType(type)) {
				QuotaBean bean = new QuotaBean(type, resource);
				bean.setSize(String.valueOf(peeker.getTotalQuota(type, resource)));
				bean.setUsed(String.valueOf(peeker.getUsedQuota(type, resource)));
				bean.setAvailable(String.valueOf(peeker.getFreeQuota(type, resource)));
				list.add(bean);
			}
		}
		QuotaResponse res = new QuotaResponse(list);
		return res;
	}

}
