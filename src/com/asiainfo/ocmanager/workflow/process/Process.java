package com.asiainfo.ocmanager.workflow.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.asiainfo.ocmanager.workflow.util.ActivitiConfiguration;

/**
 * 
 * @author zhaoyim
 *
 */
public class Process {

	public List<Task> queryParticipantTasks(String assignee) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		List<Task> list = pe.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		return list;
	}

	public List<Task> queryCandidateTasks(String candidateUser) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		List<Task> list = pe.getTaskService().createTaskQuery().taskCandidateUser(candidateUser).list();
		return list;
	}

	public void acceptCandidateTask(String taskId, String userId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getTaskService().claim(taskId, userId);
	}

	public boolean isProcessComplete(String processInstanceId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		ProcessInstance pi = pe.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();

		if (pi == null) {
			// process instance complete return true
			return true;
		}
		// process instance not complete return false
		return false;
	}

	public void deployProcess(String processName, String bpmnPath) throws FileNotFoundException {

		InputStream bpmnInputStream = new FileInputStream(new File(bpmnPath));
		String resourceName = processName + ".bpmn";

		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getRepositoryService().createDeployment().addInputStream(resourceName, bpmnInputStream).name(processName)
				.deploy();
	}

}
