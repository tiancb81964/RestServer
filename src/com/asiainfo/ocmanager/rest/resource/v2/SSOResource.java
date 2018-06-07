package com.asiainfo.ocmanager.rest.resource.v2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.bean.XForwardedUser;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/v2/api/sso")
public class SSOResource {

	private static final Logger logger = LoggerFactory.getLogger(SSOResource.class);

	/**
	 * 
	 * @param request
	 * @return
	 */
	@GET
	@Path("/proxy")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getProxyXForwardedUser(@Context HttpServletRequest request) {
		try {
			String XForwardedUser = request.getHeader("X-Forwarded-User");

			return Response.ok().entity(new XForwardedUser(XForwardedUser)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "SSOResource", "getProxyXForwardedUser");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}
	}
}
