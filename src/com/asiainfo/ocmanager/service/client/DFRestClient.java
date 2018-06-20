package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.utils.model.DFRestResponse;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.utils.OsClusterIni;

public class DFRestClient {

	public DFRestResponse sendGet(String uri, String entity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_URL);
		String token = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_TOKEN);
		String dfRestUrl = url + uri;
		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpGet httpGet = new HttpGet(dfRestUrl);
			httpGet.addHeader("Content-type", "application/json;charset=utf-8");
			httpGet.addHeader("Authorization", "bearer " + token);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			String rspEntity = EntityUtils.toString(response1.getEntity());
			return new DFRestResponse(response1.getStatusLine().getStatusCode(), rspEntity, response1.getStatusLine().getReasonPhrase());
		} finally {
			httpclient.close();
		}
	}
	
	public DFRestResponse sendPost(String uri, String entity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_URL);
		String token = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_TOKEN);
		String dfRestUrl = url + uri;
		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpGet = new HttpPost(dfRestUrl);
			httpGet.addHeader("Content-type", "application/json;charset=utf-8");
			httpGet.addHeader("Authorization", "bearer " + token);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			String rspEntity = EntityUtils.toString(response1.getEntity());
			return new DFRestResponse(response1.getStatusLine().getStatusCode(), rspEntity, response1.getStatusLine().getReasonPhrase());
			} finally {
			httpclient.close();
		}
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String reqBody = "{" + 
				"    \"kind\": \"DeploymentConfig\"," + 
				"    \"apiVersion\": \"v1\"," + 
				"    \"metadata\": {" + 
				"        \"name\": \"pre-test\"," + 
				"        \"labels\": {" + 
				"            \"app\": \"cm-broker\"" + 
				"        }," + 
				"        \"annotations\": {" + 
				"            \"dadafoundry.io/create-by\": \"clustermanager\"" + 
				"        }" + 
				"    }," + 
				"    \"spec\": {" + 
				"        \"replicas\": 1," + 
				"        \"selector\": {" + 
				"            \"app\": \"cm-broker\"," + 
				"            \"deploymentconfig\": \"cm-broker\"" + 
				"        }," + 
				"        \"template\": {" + 
				"            \"metadata\": {" + 
				"                \"labels\": {" + 
				"                    \"app\": \"cm-broker\"," + 
				"                    \"deploymentconfig\": \"cm-broker\"" + 
				"                }" + 
				"            }," + 
				"            \"spec\": {" + 
				"                \"containers\": [" + 
				"                    {" + 
				"                        \"name\": \"cm-broker\"," + 
				"                        \"image\": \"http://myimage.com\"," + 
				"                        \"ports\": [" + 
				"                            {" + 
				"                                \"containerPort\": 9000," + 
				"                                \"protocol\": \"TCP\"" + 
				"                            }" + 
				"                        ]," + 
				"                        \"env\": [" + 
				"                            {" + 
				"                                \"name\": \"ENV1\"," + 
				"                                \"value\": \"http://10.1.236.60:9090\"" + 
				"                            }," + 
				"                            {" + 
				"                                \"name\": \"ENV2\"," + 
				"                                \"value\": \"http://svc-amount2.cloud.prd.asiainfo.com\"" + 
				"                            }" + 
				"                        ]" + 
				"                    }" + 
				"                ]" + 
				"            }" + 
				"        }" + 
				"    }" + 
				"}";
		DFRestResponse rsp = new DFRestClient().sendPost("/oapi/v1/namespaces/dp-brokers/deploymentconfigs", reqBody);
		System.out.println(">>> rsp: " + rsp);
		System.out.println(">>> getStatusCode: " + rsp.getStatus());
		System.out.println(">>> end of main");
	}
}
