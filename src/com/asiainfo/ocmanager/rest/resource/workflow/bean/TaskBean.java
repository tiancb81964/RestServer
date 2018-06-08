package com.asiainfo.ocmanager.rest.resource.workflow.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author zhaoyim
 *
 */

public class TaskBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8343786423844469900L;
	private String taskId;
	private String taskName;
	private String assignee;
	private Date createTime;
	private String executionId;
	private String processDefinitionId;
	private String processInstanceId;
	private Map<String, Object> ProcessVariables;

	public TaskBean() {

	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Map<String, Object> getProcessVariables() {
		return ProcessVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		ProcessVariables = processVariables;
	}

}
