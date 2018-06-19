package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.Assignee;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.ProcessDeploymentBean;
import com.asiainfo.ocmanager.rest.resource.workflow.bean.TaskBean;
import com.asiainfo.ocmanager.workflow.process.Process;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/v2/api/workflow/common")
public class CommonWorkflowResource {

	private static final Logger logger = LoggerFactory.getLogger(CommonWorkflowResource.class);

	@GET
	@Path("list/assignee/{assigneeName}/tasks")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.TASKS)
	public Response listAssigneeTasks(@PathParam("assigneeName") String assigneeName,
			@Context HttpServletRequest request) {
		try {

			Process pro = new Process();

			List<Task> tasks = pro.queryParticipantTasks(assigneeName);
			List<TaskBean> taskBeans = new ArrayList<TaskBean>();

			for (Task task : tasks) {
				TaskBean taskB = new TaskBean();
				taskB.setAssignee(task.getAssignee());
				taskB.setCreateTime(task.getCreateTime());
				taskB.setExecutionId(task.getExecutionId());
				taskB.setProcessDefinitionId(task.getProcessDefinitionId());
				taskB.setProcessInstanceId(task.getProcessInstanceId());
				taskB.setProcessVariables(task.getProcessVariables());
				taskB.setTaskId(task.getId());
				taskB.setTaskName(task.getName());
				taskBeans.add(taskB);
			}

			return Response.ok().entity(taskBeans).tag(assigneeName).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "CommonWorkflowResource", "listAssigneeTasks");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(assigneeName).build();
		}
	}

	@GET
	@Path("list/candidate/{candidateName}/tasks")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.TASKS)
	public Response listCandidateTasks(@PathParam("candidateName") String candidateName,
			@Context HttpServletRequest request) {
		try {
			Process pro = new Process();

			List<Task> tasks = pro.queryCandidateTasks(candidateName);

			List<TaskBean> taskBeans = new ArrayList<TaskBean>();

			for (Task task : tasks) {
				TaskBean taskB = new TaskBean();
				taskB.setAssignee(task.getAssignee());
				taskB.setCreateTime(task.getCreateTime());
				taskB.setExecutionId(task.getExecutionId());
				taskB.setProcessDefinitionId(task.getProcessDefinitionId());
				taskB.setProcessInstanceId(task.getProcessInstanceId());
				taskB.setProcessVariables(task.getProcessVariables());
				taskB.setTaskId(task.getId());
				taskB.setTaskName(task.getName());
				taskBeans.add(taskB);
			}

			return Response.ok().entity(taskBeans).tag(candidateName).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "CommonWorkflowResource", "listCandidateTasks");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(candidateName).build();
		}
	}

	@POST
	@Path("accept/candidate/task/{taskId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	@Audit(action = Action.ACCEPT, targetType = TargetType.TASK)
	public Response acceptServiceInstanceTask(Assignee assignee, @PathParam("taskId") String taskId,
			@Context HttpServletRequest request) {

		Process pro = new Process();
		pro.acceptCandidateTask(taskId, assignee.getAssigneeName());

		return Response.ok()
				.entity(new ResourceResponseBean("accept candidate task success",
						"taskId: " + taskId + "  assignee: " + assignee.getAssigneeName(),
						ResponseCodeConstant.SUCCESS))
				.tag(taskId).build();
	}

	@POST
	@Path("deployment/process")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	@Audit(action = Action.DEPLOY, targetType = TargetType.PROCESS)
	public Response deployProcess(ProcessDeploymentBean processDeployment, @Context HttpServletRequest request) {

		if (processDeployment.getProcessName() == null || processDeployment.getBpmnPath() == null) {
			logger.info("{} : {} parameters is null", "CommonWorkflowResource", "deployProcess");
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity(new ResourceResponseBean("Deploy process failed",
							"parameter is null, please check, make sure the parameter input correctly.",
							ResponseCodeConstant.PARAMETER_IS_NULL))
					.tag(processDeployment.getProcessName()).build();
		}

		try {
			Process p = new Process();
			p.deployProcess(processDeployment.getProcessName(), processDeployment.getBpmnPath());
			return Response.ok()
					.entity(new ResourceResponseBean("Deploy process successfully!",
							"ProcessName: " + processDeployment.getProcessName() + ", BpmnPath: "
									+ processDeployment.getBpmnPath(),
							ResponseCodeConstant.SUCCESS))
					.tag(processDeployment.getProcessName()).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{} : {} hit exception", "CommonWorkflowResource", "deployProcess");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).tag(processDeployment.getProcessName()).build();
		}

	}

}
