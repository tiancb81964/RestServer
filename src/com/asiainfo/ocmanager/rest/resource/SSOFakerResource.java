package com.asiainfo.ocmanager.rest.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.bean.SSOFakerUserBean;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/sso")
public class SSOFakerResource {

	private static Logger logger = Logger.getLogger(SSOFakerResource.class);

	/**
	 * Get the user name for web which CAS proxy set into the header
	 * 
	 * @param request
	 * @return
	 */
	@GET
	@Path("user")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getUserName(@Context HttpServletRequest request) {
		try {
			String http_x_proxy_cas_loginname = request.getHeader("http_x_proxy_cas_loginname");
			String http_x_proxy_cas_username = request.getHeader("http_x_proxy_cas_username");
			http_x_proxy_cas_username = http_x_proxy_cas_username == null ? "" : http_x_proxy_cas_username;
			String http_x_proxy_cas_email = request.getHeader("http_x_proxy_cas_email");
			http_x_proxy_cas_email = http_x_proxy_cas_email == null ? "" : http_x_proxy_cas_email;
			String http_x_proxy_cas_userid = request.getHeader("http_x_proxy_cas_userid");
			http_x_proxy_cas_userid = http_x_proxy_cas_userid == null ? "" : http_x_proxy_cas_userid;
			String http_x_proxy_cas_mobile = request.getHeader("http_x_proxy_cas_mobile");
			http_x_proxy_cas_mobile = http_x_proxy_cas_mobile == null ? "" : http_x_proxy_cas_mobile;

			// make sure the user name is set
			if (http_x_proxy_cas_loginname == null) {
				return Response.status(Status.BAD_REQUEST)
						.entity(new ResourceResponseBean("Failed",
								"Can not get sso user information, please make sure the user info is set.", 200))
						.build();
			} else {

				// our system only has one system admin, so hard code here
				// if based on the system admin role id can return results, it
				// means
				// the user is system admin
				List<UserRoleView> urses = UserRoleViewPersistenceWrapper.getTURBasedOnUserNameAndRoleId(
						http_x_proxy_cas_loginname, "a10170cb-524a-11e7-9dbb-fa163ed7d0ae");

				boolean isAdmin = urses.size() == 0 ? false : true;

				return Response.ok()
						.entity(new SSOFakerUserBean(http_x_proxy_cas_loginname, http_x_proxy_cas_username,
								http_x_proxy_cas_email, http_x_proxy_cas_userid, http_x_proxy_cas_mobile, isAdmin))
						.build();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getUserName hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

}
