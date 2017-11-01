package com.asiainfo.ocmanager.rest.resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.auth.LdapWrapper;
import com.asiainfo.ocmanager.auth.constant.AuthConstant;
import com.asiainfo.ocmanager.rest.constant.Constant;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/ldap")
public class LdapResource {
	private static Logger logger = Logger.getLogger(LdapResource.class);

	/**
	 * get ldap configurations
	 * 
	 * @return
	 */
	@GET
	@Path("configuration")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getLdapConfigurations() {
		try {
			Map<String, String> ldapConf = new HashMap<String, String>();

			ldapConf.put(AuthConstant.LDAP_URL, LdapWrapper.getProps().getProperty(AuthConstant.LDAP_URL).trim());
			ldapConf.put(AuthConstant.LDAP_BASE_NAME,
					LdapWrapper.getProps().getProperty(AuthConstant.LDAP_BASE_NAME).trim());
			ldapConf.put(AuthConstant.LDAP_SEARCH_FILTER,
					LdapWrapper.getProps().getProperty(AuthConstant.LDAP_SEARCH_FILTER).trim());

			return Response.ok().entity(ldapConf).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getLdapConfigurations -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
}
