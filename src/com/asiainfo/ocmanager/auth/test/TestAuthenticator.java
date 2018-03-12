package com.asiainfo.ocmanager.auth.test;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.auth.Authenticator;
import com.asiainfo.ocmanager.auth.utils.AESUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by gq on 17/7/19.
 */
public class TestAuthenticator {
    Authenticator authenticator = new Authenticator();
    static Logger logger = LoggerFactory.getLogger(TestAuthenticator.class.getName());

    @Test
    public void testLoginWithUsernamePassword() {
        authenticator.loginWithUsernamePassword("admin","admin");
        authenticator.loginWithUsernamePassword("admin","admin1");
        authenticator.loginWithUsernamePassword("admin","admin");

    }

    @Test
    public void testLoginWithToken() {
        String username = "admin";
        String password = "admin";
        if (authenticator.loginWithUsernamePassword(username,password)) {
            String token = authenticator.generateToken(username, password);
            logger.info("login success. Token: " + token);
            logger.info(authenticator.loginWithToken(token)?"success":"failed");
        }
        else {
            logger.info("Invalid password.");
        }
    }

    @Test
    public void testGenerateTokenWithTTL() {
        String token = authenticator.generateTokenWithTTL("u1","passw0rd").toString();
        System.out.println(token);
        UsernamePasswordToken uptoken;
        try {
            uptoken = authenticator.parseTokenWithTTl(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginWithTokenWithTTL() {
        String token = authenticator.generateTokenWithTTL("u1","passw0rd").toString();
        System.out.println(token);
        try {
            authenticator.loginWithToken(token);
            authenticator.loginWithToken(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerateToken() {
        String requestBody = "{'username':'u1','password':'passw0rd'}";
        JsonElement req = new JsonParser().parse(requestBody);
        JsonObject obj = req.getAsJsonObject();

        String username = obj.get("username").getAsString();
        String password = obj.get("password").getAsString();
        System.out.println(username + password);
        String token = authenticator.generateToken(username,password);
        System.out.println(token);
        String[] tokenArgs = token.split("_");
        username = tokenArgs[0];
        String encryptedContent = tokenArgs[1];
        try {
           String content = AESUtils.decrypt(encryptedContent,username);
            System.out.println(content);
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
    }

}
