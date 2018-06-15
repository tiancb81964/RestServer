package com.asiainfo.ocmanager.rest.utils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

/**
 * 
 * @author zhaoyim
 *
 */
public class SSLSocketIgnoreCA {

	public static SSLConnectionSocketFactory createSSLSocketFactory()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return true;
			}
		}).build();
		// support "TLSv1", "TLSv1.2", "SSLv3"
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
				new String[] { "TLSv1", "TLSv1.2", "SSLv3" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		return sslsf;
	}
}
