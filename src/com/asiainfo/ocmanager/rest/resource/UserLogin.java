package com.asiainfo.ocmanager.rest.resource;

import com.asiainfo.ocmanager.auth.Authenticator;
import com.asiainfo.ocmanager.rest.bean.LoginResponseBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gq on 17/7/17.
 */
@Path("/login")
public class UserLogin {
    private static Logger logger = LoggerFactory.getLogger(UserLogin.class);

    @POST
    @Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String requestBody) {
        try {
            JsonElement req = new JsonParser().parse(requestBody);
            JsonObject obj = req.getAsJsonObject();
            String username = obj.get("username").getAsString();
            String password = obj.get("password").getAsString();
            Authenticator authenticator = Authenticator.getInstance();
            if (authenticator.loginWithUsernamePassword(username,password)) {
                String token = Authenticator.generateToken(username,password);
                logger.info("login success. Token: "+ token);
                return Response.ok().entity(new LoginResponseBean("Login successful!", "Please add token in header of other requests.", 200, token)).build();
            } else {
                logger.info("login failed.");
                return Response.ok().entity(new LoginResponseBean("Login failed!", "Invalid username or password.", 200, null)).build();
            }
        } catch (Exception e) {
            logger.warn("Exception during login: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
        }
    }

    @DELETE
    @Path("{username}")
    @Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@PathParam("username") String username, @Context HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            if (!username.equals(token.split("_")[0])) {
                throw new Exception("Username and token doesn't match!");
            }
            Authenticator.logout(username);
            logger.info("User [{}] logout successfully!",username);
            return Response.ok().entity(new LoginResponseBean("Logout successful!", null, 200, null)).build();
        }
        catch (Exception e) {
            logger.info("User [{}] logout failed : {}", username, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
        }
    }

}
