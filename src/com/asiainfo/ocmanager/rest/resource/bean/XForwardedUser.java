package com.asiainfo.ocmanager.rest.resource.bean;

import java.io.Serializable;

/**
 * 
 * @author zhaoyim
 *
 */
public class XForwardedUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743567164596523521L;

	private String token;

	public XForwardedUser() {

	}

	public XForwardedUser(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "XForwardedUser: {token: " + token + "}";
	}

}
