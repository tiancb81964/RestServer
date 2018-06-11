package com.asiainfo.ocmanager.workflow.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.exception.OcmanagerRuntimeException;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.workflow.constant.WorkflowConstant;
import com.asiainfo.ocmanager.workflow.util.ActivitiConfiguration;

import com.asiainfo.ocmanager.rest.constant.Constant;

/**
 * 
 * @author zhaoyim
 *
 */
public class InstanceProcess extends Process {

	private static final Logger logger = LoggerFactory.getLogger(InstanceProcess.class);

	private final static String INSTANCEPROCESS = "InstanceProcess";
	private final static String IPFLOWMESSAGE_ = "IPFLOWMESSAGE_";

	private final static String IPTENANTMEMBERACCOUNT_ = "IPtenantMemberAccount_";
	private final static String IPTENANTADMINACCOUNTS_ = "IPtenantAdminAccounts_";

	private final static String IPAPPLICANTSUBMIT_ = "IPApplicantSubmit_";
	private final static String IPAPPLICANTCANCEL_ = "IPApplicantCancel_";

	public ProcessInstance startProcessInstance(String userAcount, Map<String, Object> variables) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		// Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(IPTENANTMEMBERACCOUNT_, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(INSTANCEPROCESS, variables);
		logger.info("Start Apply Instance Process successfully!");

		return pi;
	}

	public ProcessInstance startProcessInstance(String userAcount) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(IPTENANTMEMBERACCOUNT_, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(INSTANCEPROCESS, variables);

		return pi;
	}

	public void completeMemberTask(String taskId, String flowAction, String tenantId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();

		if (flowAction.equals(IPAPPLICANTCANCEL_)) {
			variables.put(IPFLOWMESSAGE_, flowAction);

		} else {
			if (flowAction.equals(IPAPPLICANTSUBMIT_) && tenantId != null) {
				List<UserRoleView> URVs = UserRoleViewPersistenceWrapper
						.getTURBasedOnRoleNameAndTenantId(Constant.TENANTADMIN, tenantId);

				if (URVs.size() == 0) {
					throw new OcmanagerRuntimeException("The tenant did NOT have tenant Admin, should contact Admin!");
				}
				StringBuffer strBuffer = new StringBuffer();
				for (UserRoleView urv : URVs) {
					strBuffer.append(urv.getUserName());
					strBuffer.append(",");
				}
				variables.put(IPTENANTADMINACCOUNTS_, strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
				variables.put(IPFLOWMESSAGE_, flowAction);
			} else {
				logger.warn("The action or tenantId is not correct: action={0}, tenantId={1}", flowAction, tenantId);
				return;
			}
		}

		pe.getTaskService().complete(taskId, variables);
	}

	public void completeTenantAdminTask(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getTaskService().complete(taskId);
	}

	public String getProcessBindingTenantId(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		String tenantId = pe.getTaskService().getVariable(taskId, WorkflowConstant.IPPROCESSBINDINGTENANTID_)
				.toString();
		return tenantId;
	}

}
