package com.asiainfo.ocmanager.rest.resource.utils.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class OsResponse {

	private String responseBody;
	private int statusCode;

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode2) {
		this.statusCode = statusCode2;
	}

	@Override
	public String toString() {
		return "OsResponse [responseBody: " + responseBody + ", statusCode: " + statusCode + "]";
	}
}
