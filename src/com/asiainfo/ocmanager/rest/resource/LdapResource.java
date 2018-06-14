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
import com.asiainfo.ocmanager.rest.bean.LdapBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * 
 * @author zhaoyim
 *
 */
@Deprecated
@Path("/v1/api/ldap")
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
			ldapConf.put("LDAP_ADDR", LdapWrapper.getProps().getProperty(AuthConstant.LDAP_URL).trim());
			ldapConf.put("USER_DN_TEMPLATE", LdapWrapper.getAssembledLdapTemplate());
			return Response.ok().entity(ldapConf).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getLdapConfigurations -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	@GET
	@Path("status")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response ldapEnabled() {
		try {
			String auth = ServerConfiguration.getConf().getProperty(Constant.AUTHTYPE).trim();
			boolean enable = auth.equals("ldap");
			return Response.ok().entity(new LdapBean().withStatus(enable)).build();
		} catch (Exception e) {
			logger.error("kerberosEnabled hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
}
