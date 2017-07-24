package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.asiainfo.ocmanager.utils.ServerConfiguration;

/**
 * Yarn client.
 * @author EthanWang
 *
 */
public class YarnClient {
	private static YarnClient instance;
	private static final String CONF_YARN = "yarn.resourcemanager.http.url";
	private CloseableHttpClient httpClient;
	private String[] baseUrls; // active/standby rm.
	
	public static YarnClient getInstance(){
		if (instance == null) {
			synchronized(YarnClient.class	){
				if (instance == null) {
					instance = new YarnClient();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Get the active resource manager url.
	 * @return
	 */
	private String activeRM(){
		CloseableHttpResponse rsp = null;
		for(String url : this.baseUrls){
			try {
				rsp = httpClient.execute(new HttpGet(url + "/ws/v1/cluster/"));
				if (rsp.getStatusLine().getStatusCode() == 200) {
					return url;
				}
				System.out.println("WARN: ResourceManager return error: " + EntityUtils.toString(rsp.getEntity()));
				throw new IOException("WARN: ResourceManager return error: " + EntityUtils.toString(rsp.getEntity()));
			} catch (IOException e) {
				System.out.println("WARN: Ping to ResourceManager failed: " + url);
				e.printStackTrace();
				continue;
			}
			finally {
				if (rsp != null) {
					try {
						rsp.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("ERROR: Both ResourceManager nodes unavailable: " + this.baseUrls);
		throw new RuntimeException("ERROR: Both ResourceManager nodes unavailable: " + this.baseUrls);
	}
	
	/**
	 * Fetch queues info from RM.
	 * @return
	 * @throws Exception
	 */
	public String fetchQueueInfo() throws Exception{
		CloseableHttpResponse rsp = null;
		try {
			String url = activeRM() + "/ws/v1/cluster/scheduler";
			rsp = httpClient.execute(new HttpGet(URI.create(url)));
			return EntityUtils.toString(rsp.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if (rsp != null) {
				try {
					rsp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private YarnClient(){
         httpClient = HttpClientBuilder.create().build();
         baseUrls = ServerConfiguration.getConf().getProperty(CONF_YARN).split(",");
	}
}
