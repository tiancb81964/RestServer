package com.asiainfo.ocmanager.auth;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gq on 17/7/17.
 */
@Path("/login")
public class UserLogin {
    private static Logger logger = Logger.getLogger(UserLogin.class);

    @POST
    @Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String requestBody) {
        try {
            JsonElement req = new JsonParser().parse(requestBody);
            JsonObject obj = req.getAsJsonObject();
            Authenticator authenticator = new Authenticator();
            String username = obj.get("username").getAsString();
            String password = obj.get("password").getAsString();
            if (authenticator.loginWithUsernamePassword(username,password, Constant.SHIROINIPATH)) {
                String token = authenticator.generateTokenWithTTL(username,password);
                logger.info("login success. Token: "+ token);
                return Response.ok().entity("login success. Token: " + token).build();
            } else {
                logger.info("login failed.");
                return Response.ok().entity("login failed. Invalid username or password.").build();
            }
        } catch (Exception e) {
            logger.warn("Invalid parameter format!" + e.getMessage());
            return Response.ok().entity("login failed. Invalid parameter format!").build();
        }
    }

}
