package com.asiainfo.ocmanager.auth;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.google.gson.Gson;

@Provider
@PageAuth
@Deprecated
public class PageFilter implements ContainerRequestFilter {

  @Context
  private ResourceInfo resourceInfo;

  private static Gson gson = new Gson();

  private Logger logger = Logger.getLogger(PageFilter.class);

  public void filter(ContainerRequestContext requestContext) throws IOException {
    logger.info("Page filter running to intercept requests");
    Method method = resourceInfo.getResourceMethod();
    PageAuth pageAuth = method.getAnnotation(PageAuth.class);
    String requiredPermission = pageAuth.requiredPermission();
    String username = requestContext.getHeaders().getFirst("username");
    String tenantId = requestContext.getHeaders().getFirst("tenantId");
    Response response;
    UserRoleView userRoleView = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(username, tenantId);
    if(userRoleView != null && userRoleView.getPermission() != null) {
      PagePermission permission = gson.fromJson(userRoleView.getPermission(), PagePermission.class);
      try {
        Class cls = Class.forName("com.asiainfo.ocmanager.auth.PagePermission");
        Method getMethod = cls.getMethod("is" + requiredPermission);
        if (!(Boolean) getMethod.invoke(permission)) {
          response = Response.status(Response.Status.UNAUTHORIZED).build();
          requestContext.abortWith(response);
        }
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        requestContext.abortWith(response);
        e.printStackTrace();
      }
    }else{
      response = Response.status(Response.Status.UNAUTHORIZED).build();
      requestContext.abortWith(response);
    }
  }

}
