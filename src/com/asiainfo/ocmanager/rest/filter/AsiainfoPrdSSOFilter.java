package com.asiainfo.ocmanager.rest.filter;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.utils.SSOConfiguration;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class AsiainfoPrdSSOFilter implements Filter {

	private static Logger logger = Logger.getLogger(AsiainfoPrdSSOFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest hsRequest = (HttpServletRequest) servletRequest;
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpServletRequest ssoHsRequest = (HttpServletRequest) servletRequest;
			
			String validateUrl = SSOConfiguration.getConf().getProperty(Constant.PRD_SSO_VALIDATE_URL);
			String token = ssoHsRequest.getHeader("token");
			if (token == null || token.equals("null")) {
				Cookie[] cookies = hsRequest.getCookies();
				if (cookies != null) {
					token = this.getTokenFromCookie(cookies);
				}
			}
			if (token == null || token.equals("null")) {
				((HttpServletResponse) servletResponse).sendError(ResponseCodeConstant.FORBIDDEN);
			} else {
				try {
					HttpGet httpGet = new HttpGet(validateUrl + token.split("_")[1]);
					httpGet.addHeader("Content-type", "application/json;charset=utf-8");
					CloseableHttpResponse response1 = httpclient.execute(httpGet);
					try {
						String bodyStr = EntityUtils.toString(response1.getEntity(), "UTF-8");
						JsonElement resJson = new JsonParser().parse(bodyStr);
						String result = resJson.getAsJsonObject().get("result").getAsString();
						if (result.equals("suc")) {
							logger.debug("SSO Authentication success with token: " + token);
							filterChain.doFilter(servletRequest, servletResponse);
						} else {
							logger.warn("SSO Authentication fail with token: " + token);
							((HttpServletResponse) servletResponse).sendError(ResponseCodeConstant.FORBIDDEN);
						}
					} finally {
						response1.close();
					}
				} finally {
					httpclient.close();
				}
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			logger.error("doFilter() hit KeyManagementException | NoSuchAlgorithmException | KeyStoreException -> ", e);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	private String getTokenFromCookie(Cookie[] cookies) {
		logger.debug("get token from cookies");
		String token = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
				break;
			}
		}
		logger.debug("token is: " + token);
		return token;
	}

}
