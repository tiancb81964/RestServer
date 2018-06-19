package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.HashMap;
import java.util.Map;

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

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.Assignee;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.ProcessInstanceBean;
import com.asiainfo.ocmanager.workflow.constant.WorkflowConstant;
import com.asiainfo.ocmanager.workflow.process.TenantProcess;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/v2/api/workflow/tenant")
public class TenantWorkflowResource {

	private static final Logger logger = LoggerFactory.getLogger(TenantWorkflowResource.class);

	@POST
	@Path("{tenantId}/start/process")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	@Audit(action = Action.START, targetType = TargetType.PROCESS)
	public Response startTenantProcess(Assignee assignee, @PathParam("tenantId") String tenantId,
			@Context HttpServletRequest request) {

		try {

			String token = request.getHeader("token");
			if (token == null || token.isEmpty()) {
				return Response.status(Status.NOT_FOUND)
						.entity(new ResourceResponseBean("start sub tenant process failed",
								"token is null or empty, please check the token in request header.",
								ResponseCodeConstant.EMPTY_TOKEN))
						.tag(assignee.getAssigneeName()).build();
			}

			TenantProcess subTenantProcess = new TenantProcess();
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(WorkflowConstant.TPPROCESSBINDINGTENANTID_, tenantId);
			ProcessInstance pi = subTenantProcess.startProcessInstance(assignee.getAssigneeName(), variables);

			return Response.ok().entity(new ProcessInstanceBean(pi.getId(), pi.getName()))
					.tag(assignee.getAssigneeName()).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "SubTenantWorkflowResource", "startSubTenantProcess");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(assignee.getAssigneeName()).build();
		}
	}

	@POST
	@Path("complete/task/{taskId}/{flowAction}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	@Audit(action = Action.COMPLETE, targetType = TargetType.TASK)
	public Response completeTenantTask(Assignee assignee, @PathParam("taskId") String taskId,
			@PathParam("flowAction") String flowAction, @Context HttpServletRequest request) {

		String token = request.getHeader("token");
		if (token == null || token.isEmpty()) {
			return Response.status(Status.NOT_FOUND)
					.entity(new ResourceResponseBean("complete sub tenant process failed",
							"token is null or empty, please check the token in request header.",
							ResponseCodeConstant.EMPTY_TOKEN))
					.tag(assignee.getAssigneeName()).build();
		}

		TenantProcess subTenantProcess = new TenantProcess();
		String parentTenantId = subTenantProcess.getProcessBindingTenantId(taskId);
		UserRoleView urv = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(assignee.getAssigneeName(),
				parentTenantId);

		if (urv == null) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity(new ResourceResponseBean("complete sub tenant process failed",
							"the assignee role is not belong to the sub tenant process, please contact admin.",
							ResponseCodeConstant.EMPTY_TOKEN))
					.tag(assignee.getAssigneeName()).build();
		}

		if (urv.getRoleName().equals(Constant.TEAMMEMBER)) {
			subTenantProcess.completeTenantAdminTask(taskId, flowAction, parentTenantId);
		}

		// of the user is the tenant admin or system admin, can complete the
		// step2 task
		if (urv.getRoleName().equals(Constant.TENANTADMIN) || urv.getRoleName().equals(Constant.SYSADMIN)) {
			subTenantProcess.completeParentTenantAdminTask(taskId);
		}

		return Response.ok()
				.entity(new ResourceResponseBean("complete task success",
						"taskId: " + taskId + "  assignee: " + assignee.getAssigneeName(),
						ResponseCodeConstant.SUCCESS))
				.tag(assignee.getAssigneeName()).build();
	}

}
