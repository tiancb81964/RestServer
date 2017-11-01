package com.asiainfo.ocmanager.rest.resource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.constant.AmbariConstant;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/ambari")
public class AmbariResource {

	private static Logger logger = Logger.getLogger(AmbariResource.class);

	private static String tarGz = ".tar.gz";

	private static HttpHost ambariHost;
	private static String ambariUrl;
	private static UsernamePasswordCredentials ambariCreds;
	static {
		ambariHost = new HttpHost(ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_HOSTNAME).trim(),
				Integer.parseInt(ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_PORT).trim()),
				ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_PROTOCOL).trim());

		ambariUrl = ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_PROTOCOL).trim() + "://"
				+ ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_HOSTNAME).trim() + ":"
				+ ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_PORT).trim() + "/api/v1/clusters/"
				+ ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_CLUSTERNAME).trim() + "/services";

		ambariCreds = new UsernamePasswordCredentials(
				ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_USERNAME).trim(),
				ServerConfiguration.getConf().getProperty(AmbariConstant.OC_AMBARI_PASSWORD).trim());
	}

	/**
	 * get yarn client configuration files from ambari
	 * 
	 * @param filename
	 * @return
	 */
	@GET
	@Path("yarnclient")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getYarnClientFiles(@QueryParam("filename") String filename) {

		if (filename == null || filename.isEmpty()) {
			filename = "yarnclient";
		}

		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/YARN/components/YARN_CLIENT?format=client_config_tar";

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);

			try {
				HttpGet httpGet = new HttpGet(url);
				CloseableHttpResponse response1 = httpclient.execute(httpGet, context);
				InputStream is = response1.getEntity().getContent();
				try {
					return Response.ok(this.readStream(is))
							.header("Content-disposition", "attachment;filename=" + filename + tarGz)
							.header("Cache-Control", "no-cache").build();
				} finally {
					response1.close();
					is.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getYarnClientFiles hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * get hdfs client configuration files from ambari
	 * 
	 * @param filename
	 * @return
	 */
	@GET
	@Path("hdfsclient")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getHdfsClientFiles(@QueryParam("filename") String filename) {

		if (filename == null || filename.isEmpty()) {
			filename = "hdfsclient";
		}

		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/HDFS/components/HDFS_CLIENT?format=client_config_tar";

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);

			try {
				HttpGet httpGet = new HttpGet(url);
				CloseableHttpResponse response1 = httpclient.execute(httpGet, context);
				InputStream is = response1.getEntity().getContent();
				try {
					return Response.ok(this.readStream(is))
							.header("Content-disposition", "attachment;filename=" + filename + tarGz)
							.header("Cache-Control", "no-cache").build();
				} finally {
					response1.close();
					is.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getHdfsClientFiles hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * get spark client configuration files from ambari
	 * 
	 * @param filename
	 * @return
	 */
	@GET
	@Path("sparkclient")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getSparkClientFiles(@QueryParam("filename") String filename) {

		if (filename == null || filename.isEmpty()) {
			filename = "sparkclient";
		}

		try {
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			String url = ambariUrl + "/SPARK/components/SPARK_CLIENT?format=client_config_tar";

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ambariHost.getHostName(), ambariHost.getPort()), ambariCreds);
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(ambariHost, basicAuth);

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);

			try {
				HttpGet httpGet = new HttpGet(url);
				CloseableHttpResponse response1 = httpclient.execute(httpGet, context);
				InputStream is = response1.getEntity().getContent();
				try {
					return Response.ok(this.readStream(is))
							.header("Content-disposition", "attachment;filename=" + filename + tarGz)
							.header("Cache-Control", "no-cache").build();
				} finally {
					response1.close();
					is.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getHdfsClientFiles hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
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

}
