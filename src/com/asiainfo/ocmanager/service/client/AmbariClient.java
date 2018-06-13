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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;

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
		//TODO:
		return null;
	}
	
	public String getComponent(String service, String component) {
		//TODO:
		return null;
	}
	
	public String getService(String service) {
		//TODO:
		return null;
	}
	
	public boolean isSecurity() {
		//TODO:
		return true;
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
			URL url = new URL(this.cluster.getAmbari_url());
			ambariHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
			ambariUrl = this.cluster.getAmbari_url() + "/api/v1/clusters/" + this.cluster.getCluster_name();
			ambariCreds = new UsernamePasswordCredentials(this.cluster.getAmbari_user(),
					this.cluster.getAmbari_password());
		} catch (MalformedURLException e) {
			LOG.error("Error while parsing ambari url: " + this.cluster.getAmbari_url());
			throw new RuntimeException("Error while parsing ambari url: " + this.cluster.getAmbari_url());
		}
	}
}
