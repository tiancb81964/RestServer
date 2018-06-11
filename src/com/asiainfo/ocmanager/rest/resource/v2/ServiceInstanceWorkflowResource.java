package com.asiainfo.ocmanager.rest.resource.v2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.Assignee;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.ProcessInstanceBean;
import com.asiainfo.ocmanager.workflow.process.ServiceInstanceProcess;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/v2/api/workflow/service/instance")
public class ServiceInstanceWorkflowResource {
	private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceWorkflowResource.class);

	@POST
	@Path("{tenantId}/start/process")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	// @Audit(action = Action.CREATE, targetType = TargetType.USER)
	public Response startServiceInstanceProcess(Assignee assignee, @PathParam("tenantId") String tenantId,
			@Context HttpServletRequest request) {
		try {
			String token = request.getHeader("token");
			if (token == null || token.isEmpty()) {
				return Response.status(Status.NOT_FOUND)
						.entity(new ResourceResponseBean("start service instance process failed",
								"token is null or empty, please check the token in request header.",
								ResponseCodeConstant.EMPTY_TOKEN))
						.build();
			}

			// String loginUser = TokenPaserUtils.paserUserName(token);

			ServiceInstanceProcess siPro = new ServiceInstanceProcess();
			ProcessInstance pi = siPro.startProcessInstance(assignee.getAssigneeName());

			return Response.ok().entity(new ProcessInstanceBean(pi.getId(), pi.getName())).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ServiceInstanceWorkflowResource",
					"startServiceInstanceProcess");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}
	}

	@POST
	@Path("{tenantId}/complete/task/{taskId}/{flowAction}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	// @Audit(action = Action.CREATE, targetType = TargetType.USER)
	public Response completeServiceInstanceTask(Assignee assignee, @PathParam("tenantId") String tenantId,
			@PathParam("taskId") String taskId, @PathParam("flowAction") String flowAction,
			@Context HttpServletRequest request) {

		String token = request.getHeader("token");
		if (token == null || token.isEmpty()) {
			return Response.status(Status.NOT_FOUND)
					.entity(new ResourceResponseBean("complete service instance process failed",
							"token is null or empty, please check the token in request header.",
							ResponseCodeConstant.EMPTY_TOKEN))
					.build();
		}

		UserRoleView urv = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(assignee.getAssigneeName(),
				tenantId);

		ServiceInstanceProcess siPro = new ServiceInstanceProcess();

		if (urv == null) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity(new ResourceResponseBean("complete service instance process failed",
							"the assignee role is not belong to the service instance process, please contact admin.",
							ResponseCodeConstant.EMPTY_TOKEN))
					.build();
		}

		if (urv.getRoleName().equals(Constant.TEAMMEMBER)) {
			siPro.completeMemberTask(taskId, flowAction, tenantId);
		}

		if (urv.getRoleName().equals(Constant.TENANTADMIN)) {
			siPro.completeTenantAdminTask(taskId);
		}

		return Response.ok().entity(new ResourceResponseBean("complete task success",
				"taskId: " + taskId + "  assignee: " + assignee.getAssigneeName(), ResponseCodeConstant.SUCCESS))
				.build();
	}

}
