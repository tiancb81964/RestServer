package com.asiainfo.ocmanager.auth.test;

import com.asiainfo.ocmanager.auth.AESUtils;
import com.asiainfo.ocmanager.auth.Authenticator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by gq on 17/7/19.
 */
public class TestAuthenticator {
    Authenticator authenticator = Authenticator.getInstance();

    @Test
    public void testLoginWithUsernamePassword() {
        Assert.assertTrue(authenticator.loginWithUsernamePassword("u1","passw0rd"));
    }
//    @Test
//    public void testCacheLogin() {
//        UsernamePasswordToken token = new UsernamePasswordToken("u3", "passw0rd");
//        try {
//            System.out.println(authenticator.cacheLogin(token));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }


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
