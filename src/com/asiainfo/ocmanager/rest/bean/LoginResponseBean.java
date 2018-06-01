package com.asiainfo.ocmanager.rest.bean;

/**
 * Created by gq on 17/7/27.
 */
public class LoginResponseBean {
    private String status;
    private String message;
    private String token;
    private int resCode;

    public LoginResponseBean(String status, String message, int resCode, String token) {
        this.status = status;
        this.message = message;
        this.resCode = resCode;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {return token;}

    public int getResCode() {return resCode;}

	@Override
	public String toString() {
		return this.message;
	}
}
