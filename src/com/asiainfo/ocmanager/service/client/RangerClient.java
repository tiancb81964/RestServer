package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ClusterConfig;

/**
 * Ranger client
 * 
 * @author EthanWang
 *
 */
public class RangerClient {
	private static final Logger LOG = LoggerFactory.getLogger(RangerClient.class);
	private CloseableHttpClient httpClient;
	private HttpClientContext context;
	private URI rangerURI;
	private static final String TEMP = "{\"name\" : \"{$USER_NAME}\", \"firstName\" : \"{$FIRST_NAME}\", \"lastName\" : \"\", \"emailAddress\" : \"\", \"password\" : \"{$PASSWORD}\", \"description\" : \"by ocm at {$CREATE_TIME}\", \"groupIdList\" : null, \"status\" : 1, \"userRoleList\" : [\"ROLE_USER\"]}";
	private ClusterConfig conf;
	public static void main(String[] args) throws Exception {
		User user = new User();
		user.setUsername("ethanwang7");
//		user.setPassword("ethanwang3");
//		user.setCreateTime(new Date(System.currentTimeMillis()).toString());
	}

	protected RangerClient(ClusterConfig cluster) {
		this.conf = cluster;
		try {
			this.context = HttpClientContext.create();
			initContext();
			this.httpClient = HttpClientBuilder.create().build();
		} catch (Exception e) {
			LOG.error("Error while init ranger client: ", e);
			throw e;
		}
	}
	
	/**
	 * Add user to ranger
	 * 
	 * @param user
	 * @return true if added, false if failed
	 * @throws UserExistedException
	 *             if user existed
	 */
	public boolean addUser(User user) throws UserExistedException, Exception {
		try {
			validateUSer(user);
			HttpPost request = new HttpPost(this.rangerURI.resolve("/service/xusers/secure/users"));
			request.setEntity(toEntity(assembleBody(user)));
			CloseableHttpResponse response = this.httpClient.execute(request, this.context);
			if (response.getStatusLine().getStatusCode() == 200) {
				LOG.debug("Add user to ranger succeeded: " + user);
				return true;
			} else if (userExists(response)) {
				throw new UserExistedException("User already existed: " + user);
			}
			LOG.error("Add user to ranger abnormal, return code: " + response.getStatusLine().getStatusCode()
					+ ", reason phrase: " + response.getStatusLine().getReasonPhrase());
			return false;
		} catch (UserExistedException e) {
			LOG.warn("User already existed: " + user);
			throw e;
		} catch (Exception e) {
			LOG.error("Exception while add user to ranger: ", e);
			throw e;
		}
	}

	private void validateUSer(User user) {
		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			throw new RuntimeException("User name must not be null or empty!");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			LOG.debug("User's password is null: " + user);
			user.setPassword("");
		}
		if (user.getCreateTime() == null || user.getCreateTime().isEmpty()) {
			LOG.debug("User's create time is null: " + user);
			user.setCreateTime(new Date(System.currentTimeMillis()).toString());
		}
	}

	private boolean userExists(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		HttpEntity entity = response.getEntity();
		byte[] b = new byte[new Long(entity.getContentLength()).intValue()];
		InputStream in = null;
		try {
			in = entity.getContent();
			if (in.read(b) > 0) {
				String msg = new String(b);
				LOG.info("Add-User-to-Ranger returned message: " + msg);
				return msg.indexOf("XUser already exists") != -1;
			} else {
				return false;
			}
		}finally {
			if (in != null) {
				in.close();
			}
		}
	}

	private String assembleBody(User user) {
		String newUser = TEMP.replace("{$USER_NAME}", user.getUsername()).replace("{$FIRST_NAME}", user.getUsername())
				.replace("{$PASSWORD}", user.getPassword()).replace("{$CREATE_TIME}", user.getCreateTime());
		LOG.debug("New user to be appended to ranger: " + newUser);
		return newUser;
	}

	@SuppressWarnings("deprecation")
	private HttpEntity toEntity(String addUserBody) {
		StringEntity entity = new StringEntity(addUserBody, HTTP.UTF_8);
        entity.setContentType("application/json");
		return entity;
	}

	private void initContext() {
		//TODO: config to clusters.ini
		CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(conf.getProperty(Constant.RANGER_ADMIN),
						conf.getProperty(Constant.RANGER_PASSWD)));
		AuthCache authCache = new BasicAuthCache();
		StringBuilder sb = new StringBuilder("http://");
		sb.append(conf.getProperty(Constant.RANGER_HOSTS)).append(":")
				.append(Integer.valueOf(conf.getProperty(Constant.RANGER_PORT)));
		this.rangerURI = URI.create(sb.toString());
		authCache.put(
				new HttpHost(conf.getProperty(Constant.RANGER_HOSTS),
						Integer.valueOf(conf.getProperty(Constant.RANGER_PORT)), "http"),
				new BasicScheme());
		this.context.setCredentialsProvider(provider);
		this.context.setAuthCache(authCache);
	}

	@SuppressWarnings("serial")
	public static class UserExistedException extends IOException {
		public UserExistedException(String msg) {
			super(msg);
		}
	}

}
