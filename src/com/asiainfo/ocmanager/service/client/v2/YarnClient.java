package com.asiainfo.ocmanager.service.client.v2;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.Subject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Yarn client
 * @author Ethan
 *
 */
public class YarnClient extends ServiceClient{
	private CloseableHttpClient httpClient;
	private String[] baseUrls; // active/standby rm.

	protected YarnClient(String serviceName, Subject subject) {
		super(serviceName, subject);
		init();
	}

	private void init() {
		httpClient = HttpClientBuilder.create().build();
		baseUrls = this.serviceConfig.getProperty(Constant.RM_HTTP).split(",");		
	}
	
	public static void main(String[] args) {
		String info = null;
		try {
			info = new YarnClient("yarnon111", new Subject()).fetchQueuesInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(">>> info: " + info);
	}
	
	/**
	 * Fetch all queues info from RM with returned String in json.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fetchQueuesInfo() throws Exception {
		CloseableHttpResponse rsp = null;
		try {
			String url = activeRM() + "/ws/v1/cluster/scheduler";
			rsp = httpClient.execute(new HttpGet(URI.create(url)));
			return EntityUtils.toString(rsp.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
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
	 * Fetch queue info by specified queueName. Specified queue should be child
	 * of <code>root</code> queue, such as <code>default</code> queue.
	 * 
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public JsonObject fetchQueueInfoByName(String queueName) throws Exception {
		JsonObject jsonObj = new JsonParser().parse(fetchQueuesInfo()).getAsJsonObject();
		JsonObject queue = findQueueByName(jsonObj, queueName);
		return queue;
	}

	private JsonObject findQueueByName(JsonObject json, String queueName) {
		JsonArray queues = json.getAsJsonObject("scheduler").getAsJsonObject("schedulerInfo").getAsJsonObject("queues")
				.getAsJsonArray("queue");
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
	
	/**
	 * Get yarn metrics. Returned mapping consist of: "appsSubmitted"
	 * "appsCompleted" "appsPending" "appsRunning" "appsFailed" "appsKilled"
	 * "reservedMB" "availableMB" "allocatedMB" "reservedVirtualCores"
	 * "availableVirtualCores" "allocatedVirtualCores" "containersAllocated"
	 * "containersReserved" "containersPending" "totalMB" "totalVirtualCores"
	 * "totalNodes" "lostNodes" "unhealthyNodes" "decommissionedNodes"
	 * "rebootedNodes" "activeNodes"
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fetchMetircs() throws Exception {
		Map<String, String> metrics = new HashMap<>();
		JsonObject jsonObj = new JsonParser().parse(fetchMetrics()).getAsJsonObject();
		for (Entry<String, JsonElement> element : jsonObj.getAsJsonObject("clusterMetrics").entrySet()) {
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
		} finally {
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
	 * Get the active resource manager url.
	 * 
	 * @return
	 */
	private String activeRM() {
		CloseableHttpResponse rsp = null;
		List<IOException> errors = new ArrayList<>();
		for (String url : this.baseUrls) {
			try {
				rsp = httpClient.execute(new HttpGet(url + "/ws/v1/cluster/"));
				if (rsp.getStatusLine().getStatusCode() == 200) {
					return url;
				}
				throw new IOException(
						"ResourceManager connected but return error: " + EntityUtils.toString(rsp.getEntity()));
			} catch (IOException e) {
				errors.add(e);
				continue;
			} finally {
				if (rsp != null) {
					try {
						rsp.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		printError(errors);
		throw new RuntimeException("ERROR: Both ResourceManager nodes unavailable: " + Arrays.asList(this.baseUrls));
	}
	
	private void printError(List<IOException> errorInfos) {
		System.out.println("ERROR: Both ResourceManager nodes unavailable: " + Arrays.asList(this.baseUrls)
				+ ". Due to exceptions:");
		Iterator<IOException> it = errorInfos.iterator();
		while (it.hasNext()) {
			it.next().printStackTrace();
		}
	}
}
