package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
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
			return new DFRestResponse(response1.getStatusLine().getStatusCode(), rspEntity,
					response1.getStatusLine().getReasonPhrase());
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
			HttpPost httpPost = new HttpPost(dfRestUrl);
			httpPost.addHeader("Content-type", "application/json;charset=utf-8");
			httpPost.addHeader("Authorization", "bearer " + token);
			StringEntity se = new StringEntity(entity);
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPost.setEntity(se);
			CloseableHttpResponse response1 = httpclient.execute(httpPost);
			String rspEntity = EntityUtils.toString(response1.getEntity());
			return new DFRestResponse(response1.getStatusLine().getStatusCode(), rspEntity,
					response1.getStatusLine().getReasonPhrase());
		} finally {
			httpclient.close();
		}
	}
	
	public DFRestResponse sendPut(String uri, String entity)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String url = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_URL);
		String token = OsClusterIni.getConf().get(Constant.SERVICE_CLUSTER).getProperty(Constant.OS_TOKEN);
		String dfRestUrl = url + uri;
		SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPut httpPut = new HttpPut(dfRestUrl);
			httpPut.addHeader("Content-type", "application/json;charset=utf-8");
			httpPut.addHeader("Authorization", "bearer " + token);
			StringEntity se = new StringEntity(entity);
			se.setContentType("application/json");
			se.setContentEncoding("utf-8");
			httpPut.setEntity(se);
			CloseableHttpResponse response1 = httpclient.execute(httpPut);
			String rspEntity = EntityUtils.toString(response1.getEntity());
			return new DFRestResponse(response1.getStatusLine().getStatusCode(), rspEntity,
					response1.getStatusLine().getReasonPhrase());
		} finally {
			httpclient.close();
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {
		String reqDC = dc();
		String reqSVC = svc();
		String reqRoute = route();
		DFRestResponse rsp = new DFRestClient().sendPost("/api/v1/namespaces/dp-brokers/services", reqSVC);
		System.out.println(">>> rsp: " + rsp);
		System.out.println(">>> getStatusCode: " + rsp.getStatus());
		System.out.println(">>> end of main");
	}

	private static String route() {
		return StringEscapeUtils.unescapeJava("{" + "    \"kind\": \"Route\"," + "    \"apiVersion\": \"v1\"," + "    \"metadata\": {"
				+ "        \"name\": \"pre-test\"" + "    }," + "    \"spec\": {"
				+ "        \"host\": \"mydomain.prd.asiainfo.com\"," + "        \"to\": {"
				+ "            \"kind\": \"Service\"," + "            \"name\": \"pre-test\"" + "        },"
				+ "        \"port\": {" + "            \"targetPort\": \"9000-tcp\"" + "        }" + "    }" + "}");
	}

	private static String svc() {
		return StringEscapeUtils.unescapeJava("{" + "    \"kind\": \"Service\"," + "    \"apiVersion\": \"v1\"," + "    \"metadata\": {"
				+ "        \"name\": \"pre-test\"," + "        \"labels\": {" + "            \"app\": \"cm-broker\""
				+ "        }," + "        \"annotations\": {"
				+ "            \"dadafoundry.io/create-by\": \"clustermanager\"" + "        }" + "    },"
				+ "    \"spec\": {" + "        \"ports\": [" + "            {"
				+ "                \"name\": \"9000-tcp\"," + "                \"protocol\": \"TCP\","
				+ "                \"port\": 9000," + "                \"targetPort\": 9000" + "            }"
				+ "        ]," + "        \"selector\": {" + "            \"app\": \"cm-broker\","
				+ "            \"deploymentconfig\": \"pre-test\"" + "        }" + "    }" + "}");
	}

	private static String dc() {
		String input = "{    \"kind\": \"DeploymentConfig\",    \"apiVersion\": \"v1\",    \"metadata\": {        \"name\": \"pre-test\",        \"labels\": {            \"app\": \"cm-broker\"        },        \"annotations\": {            \"dadafoundry.io\\/create-by\": \"clustermanager\"        }    },    \"spec\": {        \"replicas\": 1,        \"selector\": {            \"app\": \"cm-broker\",            \"deploymentconfig\": \"cm-broker\"        },        \"template\": {            \"metadata\": {                \"labels\": {                    \"app\": \"cm-broker\",                    \"deploymentconfig\": \"cm-broker\"                }            },            \"spec\": {                \"containers\": [                    {                        \"name\": \"cm-broker\",                        \"image\": \"https:\\/\\/my.image.com\",                        \"ports\": [                            {                                \"containerPort\": 9000,                                \"protocol\": \"TCP\"                            }                        ],                        \"env\": [                            {                                \"name\": \"ENV1\",                                \"value\": \"http:\\/\\/10.1.236.60:9090\"                            },                            {                                \"name\": \"ENV2\",                                \"value\": \"http:\\/\\/10.1.236.59:9090\"                            }                        ]                    }                ]            }        }    }}";
		return StringEscapeUtils.unescapeJava(input);
	}
}
