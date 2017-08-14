package com.asiainfo.ocmanager.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.auth.Authenticator;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;

/**
 * Created by gq on 17/7/18.
 */
public class AuthFilter implements Filter {
	private static Logger logger = Logger.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest hsRequest = (HttpServletRequest) servletRequest;
		logger.debug("Request received: " + hsRequest.getRequestURI());
		if (hsRequest.getRequestURI().endsWith("/login")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			try {
				String token = hsRequest.getHeader("token");
				if (token == null) {
					((HttpServletResponse) servletResponse).sendError(ResponseCodeConstant.FORBIDDEN);
				} else {
					boolean authcSuccess = authenticate(token);
					if (authcSuccess) {
						logger.debug("Authentication success with token: " + token);
						filterChain.doFilter(servletRequest, servletResponse);
					} else {
						logger.warn("Authentication fail with token: " + token);
						((HttpServletResponse) servletResponse).sendError(ResponseCodeConstant.FORBIDDEN);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				((HttpServletResponse) servletResponse).sendError(ResponseCodeConstant.FORBIDDEN);
			}
		}
	}

	@Override
	public void destroy() {

	}

	public boolean authenticate(String token) {
		return Authenticator.loginWithToken(token);
	}

}
