package com.asiainfo.ocmanager.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.auth.Authenticator.AuthType;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * 
 * @author EthanWang
 *
 */
@Path("/auth")
public class AuthResource {
    private static Logger logger = LoggerFactory.getLogger(AuthResource.class);

    @GET
	@Path("/type")
    @Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
    public Response getType() {
        try {
            return Response.ok().entity(getAuthType()).build();
        } catch (Exception e) {
            logger.error("Exception while get auth type: ", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

	private int getAuthType() {
		AuthType type = AuthType.valueOf(ServerConfiguration.getConf().getProperty(Constant.AUTHTYPE));
		return type.code();
	}
	
}
