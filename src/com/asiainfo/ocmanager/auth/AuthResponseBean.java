package com.asiainfo.ocmanager.auth;

/**
 * Created by gq on 17/7/27.
 */
public class AuthResponseBean {
    private String status;
    private String message;
    private String token;
    private int resCode;

    public AuthResponseBean(String status, String message, int resCode, String token) {
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

}
