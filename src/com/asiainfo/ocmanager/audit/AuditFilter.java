package com.asiainfo.ocmanager.audit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.rest.bean.ResponseBean;

/**
 * Filter for auditting user-end operations
 * 
 * @author Ethan
 *
 */
@Provider
public class AuditFilter implements ContainerResponseFilter {
	private static final Logger LOG = LoggerFactory.getLogger("AuditFilter.audit");
	@Context
	private HttpServletRequest sr;

	@Context
	private ResourceInfo ri;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		try {
			Audit anno = ri.getResourceMethod().getAnnotation(Audit.class);
			if (anno == null) {
				return;
			}
			int status = responseContext.getStatus();
			Object entity = responseContext.getEntity();
			String target;
			if (entity instanceof ResponseBean) {
				target = ((ResponseBean) entity).getOperand().toString();
			} else {
				target = "ParseError:" + entity.getClass().getSimpleName();
			}
			String ip = sr.getRemoteAddr();
			String user = isLogin(requestContext) ? target : getUser(requestContext);
			Action action = anno.action();
			TargetType type = anno.targetType();
			AuditString audit = new AuditString().status(status).user(user).ip(ip).action(action.name())
					.targetType(type.name()).target(target).entity(entity.toString());
			LOG.info(audit.toString());
		} catch (Exception e) {
			LOG.error("Exception doing auditting: ", e);
		}

	}

	private boolean isLogin(ContainerRequestContext requestContext) {
		return requestContext.getUriInfo().getPath().contains("authc/login");
	}

	private String getUser(ContainerRequestContext requestContext) {
		Cookie token = requestContext.getCookies().get("token");
		return token.getValue().split("_")[0];
	}

	private static class AuditString {
		private String template = "user=$USER  ip=$IP  status=$STATUS  action=$ACTION  tType=$TARGETTYPE  tValue=$TARGET desc=$ENTITY";

		public AuditString status(int status) {
			template = template.replace("$STATUS", String.valueOf(status));
			return this;
		}

		public AuditString user(String user) {
			if (user == null) {
				return this;
			}
			template = template.replace("$USER", user);
			return this;
		}

		public AuditString ip(String ip) {
			if (ip == null) {
				return this;
			}
			template = template.replace("$IP", ip);
			return this;
		}

		public AuditString action(String action) {
			if (action == null) {
				return this;
			}
			template = template.replace("$ACTION", action);
			return this;
		}

		public AuditString targetType(String targetType) {
			if (targetType == null) {
				return this;
			}
			template = template.replace("$TARGETTYPE", targetType);
			return this;
		}

		public AuditString target(String target) {
			if (target == null) {
				return this;
			}
			template = template.replace("$TARGET", target);
			return this;
		}

		public AuditString entity(String description) {
			if (description == null) {
				return this;
			}
			template = template.replace("$ENTITY", description);
			return this;
		}

		@Override
		public String toString() {
			return this.template;
		}
	}
}
