package com.asiainfo.ocmanager.service.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.utils.OsClusterIni;

public class DFRestClient {

	public CloseableHttpResponse sendGet(String uri, String entity)
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
			return response1;
		} finally {
			httpclient.close();
		}
	}
}
