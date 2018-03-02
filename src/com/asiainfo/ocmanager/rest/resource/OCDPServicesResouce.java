package com.asiainfo.ocmanager.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.constant.Constant;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/ocdp/services")
public class OCDPServicesResouce {

	private static Logger logger = Logger.getLogger(OCDPServicesResouce.class);

	/**
	 * get all defined OCDP services in the server.properties parameters:
	 * oc.ocdp.services
	 * 
	 * @return
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getOCDPServices() {
		try {
			return Response.ok().entity(Constant.list).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getOCDPServices hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
}
