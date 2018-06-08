package com.asiainfo.ocmanager.workflow.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.workflow.util.ActivitiConfiguration;

import com.asiainfo.ocmanager.rest.constant.Constant;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceProcess extends Process {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceInstanceProcess.class);

	private final static String TENANTMEMBERACCOUNT = "tenantMemberAccount";
	private final static String FLOWMESSAGE = "FLOWMESSAGE";

	private final static String SERVICEINSTANCEPROCESS = "serviceInstanceProcess";
	private final static String TENANTADMINACCOUNTS = "tenantAdminAccounts";

	private final static String MEMBERSUBMIT = "memberSubmit";
	private final static String MEMBERCANCEL = "memberCancel";

	public ProcessInstance startProcessInstance(String userAcount, Map<String, Object> variables) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		// Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(TENANTMEMBERACCOUNT, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(SERVICEINSTANCEPROCESS, variables);

		return pi;
	}

	public ProcessInstance startProcessInstance(String userAcount) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(TENANTMEMBERACCOUNT, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(SERVICEINSTANCEPROCESS, variables);

		return pi;
	}

	public void completeMemberTask(String taskId, String flowAction, String tenantId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();

		if (flowAction.equals(MEMBERCANCEL)) {
			variables.put(FLOWMESSAGE, flowAction);

		} else {
			if (flowAction.equals(MEMBERSUBMIT) && tenantId != null) {
				List<UserRoleView> URVs = UserRoleViewPersistenceWrapper
						.getTURBasedOnRoleNameAndTenantId(Constant.TENANTADMIN, tenantId);

				StringBuffer strBuffer = new StringBuffer();
				for (UserRoleView urv : URVs) {
					strBuffer.append(urv.getUserName());
					strBuffer.append(",");
				}
				variables.put(TENANTADMINACCOUNTS, strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
				variables.put(FLOWMESSAGE, flowAction);
			} else {
				LOG.warn("The action or tenantId is not correct: action={0}, tenantId={1}", flowAction, tenantId);
				return;
			}
		}

		pe.getTaskService().complete(taskId, variables);
	}

	public void completeTenantAdminTask(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getTaskService().complete(taskId);
	}

}
