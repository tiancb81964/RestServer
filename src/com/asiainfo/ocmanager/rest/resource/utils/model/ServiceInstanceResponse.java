package com.asiainfo.ocmanager.rest.resource.utils.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceResponse {

	private ServiceInstanceQuotaCheckerResponse checkerRes;
	private String resBody;

	public ServiceInstanceQuotaCheckerResponse getCheckerRes() {
		return checkerRes;
	}

	public void setCheckerRes(ServiceInstanceQuotaCheckerResponse checkerRes) {
		this.checkerRes = checkerRes;
	}

	public String getResBody() {
		return resBody;
	}

	public void setResBody(String resBody) {
		this.resBody = resBody;
	}

}
