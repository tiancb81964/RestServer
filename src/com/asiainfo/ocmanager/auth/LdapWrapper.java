package com.asiainfo.ocmanager.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;

import com.asiainfo.ocmanager.auth.constant.AuthConstant;
import com.asiainfo.ocmanager.exception.OcmanagerRuntimeException;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * LDAP context
 * 
 * @author EthanWang
 *
 */
public class LdapWrapper {
	private static final Logger LOG = Logger.getLogger(LdapWrapper.class);
	private static Properties props;
	private static SearchControls cons;
	private static JndiLdapContextFactory factory;

	private LdapWrapper() {

	}

	public static boolean isLdapEnabled() {
		String type = ServerConfiguration.getConf().getProperty(Constant.AUTHTYPE).trim();
		return "ldap".equals(type);
	}

	/**
	 * Get Ldap user template by configuration file
	 * 
	 * @return
	 */
	public static String getAssembledLdapTemplate() {
		String baseName = props.getProperty(AuthConstant.LDAP_BASE).trim();
		return "uid={0}," + baseName;
	}

	/**
	 * Get available users in Ldap server.
	 * 
	 * @return
	 */
	public static List<String> allUsers() {
		try {
			List<String> users = new ArrayList<>();
			NamingEnumeration<SearchResult> results = searchUsers();
			while (results.hasMore()) {
				SearchResult ele = results.next();
				users.add((String) ele.getAttributes().get("uid").get());
			}
			return users;
		} catch (Exception e) {
			LOG.error(e);
			throw new OcmanagerRuntimeException(e);
		}
	}

	/**
	 * Get the properties in the ldap.properties file
	 * 
	 * @return
	 */
	public static Properties getProps() {
		if (props == null) {
			synchronized (LdapWrapper.class) {
				if (props == null) {
					new LdapWrapper();
				}
			}
		}
		return props;
	}

	private static void initClz() {
		InputStream inputStream = null;
		try {
			String base = LdapWrapper.class.getResource("/").getPath() + ".." + File.separator;
			String confpath = base + "conf" + File.separator + "ldap.properties";
			inputStream = new FileInputStream(new File(confpath));
			props = new Properties();
			props.load(inputStream);
		} catch (Exception e) {
			LOG.error("Error while locating file ldap.properties: ", e);
			throw new OcmanagerRuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.error("Error while init Class", e);
				}
			}
		}
	}

	static {
		try {
			initClz();
			factory = new JndiLdapContextFactory();
			factory.setUrl(props.getProperty("ldap.url").trim());
			cons = new SearchControls();
			cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
			cons.setReturningAttributes(new String[] { "uid" });
		} catch (Exception e) {
			LOG.error("Error while static block", e);
			throw new OcmanagerRuntimeException(e);
		}
	}

	private static NamingEnumeration<SearchResult> searchUsers() {
		LdapContext ctx = null;
		try {
			ctx = factory.getSystemLdapContext();
			return ctx.search(props.getProperty("ldap.base.name").trim(), props.getProperty("ldap.search.filter"),
					cons);
		} catch (Exception e) {
			LOG.error("Failed to get Ldap users: ", e);
			throw new OcmanagerRuntimeException(e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					LOG.error("Failed to close LdapContext: ", e);
				}
			}
		}
	}

}
