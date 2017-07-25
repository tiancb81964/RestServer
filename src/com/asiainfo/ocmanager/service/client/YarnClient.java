package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Yarn client.
 * @author EthanWang
 *
 */
public class YarnClient {
	private static YarnClient instance;
	private static final String CONF_YARN = "oc.yarn.resourcemanager.http.url";
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
	 * Fetch all queues info from RM with returned String in json.
	 * @return
	 * @throws Exception
	 */
	public String fetchQueuesInfo() throws Exception{
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
	
	/**
	 * Get yarn metrics. Returned mapping consist of:     
	 *  "appsSubmitted"
        "appsCompleted"
        "appsPending"
        "appsRunning"
        "appsFailed"
        "appsKilled"
        "reservedMB"
        "availableMB"
        "allocatedMB"
        "reservedVirtualCores"
        "availableVirtualCores"
        "allocatedVirtualCores"
        "containersAllocated"
        "containersReserved"
        "containersPending"
        "totalMB"
        "totalVirtualCores"
        "totalNodes"
        "lostNodes"
        "unhealthyNodes"
        "decommissionedNodes"
        "rebootedNodes"
        "activeNodes"
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> fetchMetircs() throws Exception{
		Map<String, String> metrics = new HashMap<>();
		JsonObject jsonObj = new JsonParser().parse(fetchMetrics()).getAsJsonObject();
		for(Entry<String, JsonElement> element : jsonObj.getAsJsonObject("clusterMetrics").entrySet()){
			metrics.put(element.getKey(), element.getValue().getAsString());
		}
		return metrics;
	}
	
	private String fetchMetrics() throws Exception {
		CloseableHttpResponse rsp = null;
		try {
			String url = activeRM() + "/ws/v1/cluster/metrics";
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

	/**
	 * Fetch queue info by specified queueName. Specified queue
	 * should be child of <code>root</code> queue, such as <code>default</code> queue.
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public JsonObject fetchQueueInfoByName(String queueName) throws Exception{
		JsonObject jsonObj = new JsonParser().parse(fetchQueuesInfo()).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		return queue;
	}
	
	private JsonObject findQueueByName(JsonObject json, String queueName) {
		JsonArray queues = json.getAsJsonObject("scheduler").getAsJsonObject("schedulerInfo").getAsJsonObject("queues").getAsJsonArray("queue");
		Iterator<JsonElement> it = queues.iterator();
		String name = "";
		while (it.hasNext()) {
			JsonObject queue = it.next().getAsJsonObject();
			name = queue.getAsJsonPrimitive("queueName").getAsString();
			if (name.equals(queueName)) {
				return queue;
			}
		}
		System.out.println("Queue not exist: " + queueName);
		throw new RuntimeException("Queue not exist: " + queueName);
	}
	
	private YarnClient(){
         httpClient = HttpClientBuilder.create().build();
         baseUrls = ServerConfiguration.getConf().getProperty(CONF_YARN).split(",");
	}
}
