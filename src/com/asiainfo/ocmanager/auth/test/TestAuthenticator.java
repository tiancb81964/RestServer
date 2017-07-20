package com.asiainfo.ocmanager.auth.test;

import com.asiainfo.ocmanager.auth.AESUtils;
import com.asiainfo.ocmanager.auth.Authenticator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by gq on 17/7/19.
 */
public class TestAuthenticator {
    Authenticator authenticator = new Authenticator();

    @Test
    public void testLoginWithUsernamePassword() {
        Authenticator authenticator = new Authenticator();
        Assert.assertTrue(authenticator.loginWithUsernamePassword("u1","passw0rd","shiroJdbc.ini"));
    }


    @Test
    public void testGenerateTokenWithTTL() {
        String token = authenticator.generateTokenWithTTL("u1","passw0rd").toString();
        System.out.println(token);
        try {
            authenticator.parseTokenWithTTl(token);
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
        String token = new Authenticator().generateToken(username,password);
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
