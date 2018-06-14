package com.asiainfo.ocmanager.service.client;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Ambari client
 * 
 * @author Ethan
 *
 */
public class AmbariClient {
	private static final Logger LOG = LoggerFactory.getLogger(AmbariClient.class);
	private HttpHost ambariHost;
	private String ambariUrl;
	private UsernamePasswordCredentials ambariCreds;
	private Cluster cluster;

	public byte[] getFile(String postffix) throws Exception {
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/services/" + postffix;

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			InputStream is = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to get yarn files, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to get yarn files, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				is = response1.getEntity().getContent();
				return readStream(is);
			} finally {
				close(response1, is, httpclient);
			}
		} catch (Exception e) {
			LOG.error("getYarnClientFiles hit exception -> ", e);
			throw new RuntimeException("getYarnClientFiles hit exception -> ", e);
		}
	}

	public String getConfigs(String type, String tag) {
		if (tag == null) {
			tag = getLatestTag(type);
		}
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/configurations?type=" + type + "&tag=" + tag;
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to getConfigs, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to getConfigs, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
				return bodyStr;
			} finally {
				close(response1, httpclient);
			}
		} catch (Exception e) {
			LOG.error("getConfigs hit exception -> ", e);
			throw new RuntimeException("getConfigs hit exception -> ", e);
		}
	}

	private String getLatestTag(String type) {
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/configurations?type=" + type;
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to getLatestTag, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to getLatestTag, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
				JsonObject parser = new JsonParser().parse(bodyStr).getAsJsonObject();
				JsonArray items = parser.getAsJsonArray("items");
				String latest = "";
				for (JsonElement item : items) {
					String tag = item.getAsJsonObject().getAsJsonPrimitive("tag").getAsString();
					if (tag.compareTo(latest) > 0) {
						latest = tag;
					}
				}
				return latest;
			} finally {
				close(response1, httpclient);
			}
		} catch (Exception e) {
			LOG.error("getLatestTag hit exception -> ", e);
			throw new RuntimeException("getLatestTag hit exception -> ", e);
		}
	}

	public String getComponent(String service, String component) {
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/services/" + service + "/components/" + component;

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to getComponent, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to getComponent, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
				return bodyStr;
			} finally {
				close(response1, httpclient);
			}
		} catch (Exception e) {
			LOG.error("getComponent hit exception -> ", e);
			throw new RuntimeException("getComponent hit exception -> ", e);
		}
	}

	public String getService(String service) {
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/services/" + service;

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to getService, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to getService, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
				return bodyStr;
			} finally {
				close(response1, httpclient);
			}
		} catch (Exception e) {
			LOG.error("getService hit exception -> ", e);
			throw new RuntimeException("getService hit exception -> ", e);
		}
	}

	public boolean isSecurity() {
		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl;
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);
			CloseableHttpResponse response1 = null;
			try {
				HttpGet httpGet = new HttpGet(url);
				response1 = httpclient.execute(httpGet, context);
				if (response1.getStatusLine().getStatusCode() != 200) {
					LOG.error("Failed to isSecurity, return code: " + response1.getStatusLine().getStatusCode()
							+ ", reason: " + response1.getEntity().toString());
					throw new RuntimeException(
							"Failed to isSecurity, return code: " + response1.getStatusLine().getStatusCode()
									+ ", reason: " + response1.getEntity().toString());
				}
				String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
				JsonObject parser = new JsonParser().parse(bodyStr).getAsJsonObject();
				JsonObject item = parser.getAsJsonObject("Clusters");
				String type = item.getAsJsonPrimitive("security_type").getAsString();
				return type.equalsIgnoreCase("KERBEROS");
			} finally {
				close(response1, httpclient);
			}
		} catch (Exception e) {
			LOG.error("isSecurity hit exception -> ", e);
			throw new RuntimeException("isSecurity hit exception -> ", e);
		}
	}

	private void close(Closeable... c) {
		for (Closeable e : c) {
			if (c != null) {
				try {
					e.close();
				} catch (IOException t) {
					LOG.error("close() hit IOException -> ", t);
				}
			}
		}
	}

	private byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	protected AmbariClient(Cluster cluster) {
		this.cluster = cluster;
		init();
	}

	private void init() {
		try {
			URL url = new URL(this.cluster.getCluster_url());
			ambariHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
			ambariUrl = this.cluster.getCluster_url() + "/api/v1/clusters/" + this.cluster.getCluster_name();
			ambariCreds = new UsernamePasswordCredentials(this.cluster.getCluster_admin(),
					this.cluster.getCluster_password());
		} catch (MalformedURLException e) {
			LOG.error("Error while parsing ambari url: " + this.cluster.getCluster_url());
			throw new RuntimeException("Error while parsing ambari url: " + this.cluster.getCluster_url());
		}
	}
}
