package com.asiainfo.ocmanager.rest.filter;

import com.asiainfo.ocmanager.auth.Authenticator;
import org.apache.log4j.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        logger.info("Request URI: "+hsRequest.getRequestURI() + ", begin servlet filter......");
        if (hsRequest.getRequestURI().endsWith("/login")) {
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            try {
                String token = hsRequest.getHeader("token");
                if(token == null)
                {
                    ((HttpServletResponse)servletResponse).sendError(403);
                }else {
                    boolean authcSuccess = authenticate(token);
                    if(authcSuccess) {
                        logger.info("Authentication success with token: " + token);
                        filterChain.doFilter(servletRequest,servletResponse);
                    }
                    else {
                        logger.warn("Authentication fail with token: " + token);
                        ((HttpServletResponse)servletResponse).sendError(403);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((HttpServletResponse)servletResponse).sendError(403);
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
