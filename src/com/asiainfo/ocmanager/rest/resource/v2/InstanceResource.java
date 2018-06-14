package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;

/**
 * 
 * @author zhaoyim
 *
 */
@Path("/v2/api/instance")
public class InstanceResource {

	private static final Logger logger = LoggerFactory.getLogger(ApplyBillResource.class);

	@GET
	@Path("service/tenant/{tenantId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.INSTANCES)
	public Response getServiceInstancesInTenant(@PathParam("tenantId") String tenantId) {
		try {
			List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
					.getInstancesByTenantAndCategory(tenantId, Constant.SERVICE);
			return Response.ok().entity(serviceInstances).tag(tenantId).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "InstanceResource", "getServiceInstancesInTenant");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(tenantId).build();
		}
	}

	@GET
	@Path("tool/tenant/{tenantId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.TOOLS)
	public Response getToolInstancesInTenant(@PathParam("tenantId") String tenantId) {
		try {
			List<ServiceInstance> serviceInstances = ServiceInstancePersistenceWrapper
					.getInstancesByTenantAndCategory(tenantId, Constant.TOOL);
			return Response.ok().entity(serviceInstances).tag(tenantId).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "InstanceResource", "getToolInstancesInTenant");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(tenantId).build();
		}
	}

}
