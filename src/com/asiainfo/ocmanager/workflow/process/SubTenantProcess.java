package com.asiainfo.ocmanager.workflow.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.workflow.constant.WorkflowConstant;
import com.asiainfo.ocmanager.workflow.util.ActivitiConfiguration;

/**
 * 
 * @author zhaoyim
 *
 */
public class SubTenantProcess extends Process {
	private static final Logger LOG = LoggerFactory.getLogger(SubTenantProcess.class);

	private final static String SUBTENANTPROCESS = "subTenantProcess";
	private final static String STPFLOWMESSAGE = "STPFLOWMESSAGE";

	private final static String STPTENANTADMINACCOUNT = "STPTenantAdminAccount";
	private final static String STPSYSTEMADMINACCOUNTS = "STPSystemAdminAccounts";

	private final static String TENANTADMINSUBMIT = "tenantAdminSubmit";
	private final static String TENANTADMINCANCEL = "tenantAdminCancel";

	public ProcessInstance startProcessInstance(String userAcount, Map<String, Object> variables) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		// Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(STPTENANTADMINACCOUNT, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(SUBTENANTPROCESS, variables);

		return pi;
	}

	public ProcessInstance startProcessInstance(String userAcount) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(STPTENANTADMINACCOUNT, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(SUBTENANTPROCESS, variables);

		return pi;
	}

	public void completeTenantAdminTask(String taskId, String flowAction, String parentTenantId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();

		if (flowAction.equals(TENANTADMINCANCEL)) {
			variables.put(STPFLOWMESSAGE, flowAction);
		} else {
			if (flowAction.equals(TENANTADMINSUBMIT)) {
				// if the tenant is root tenant only have the system admin
				// so the system admin deal with the apply
				if (parentTenantId.equals("ae783b6d-655a-11e7-aa10-fa163ed7d0ae")) {
					// get all system admin
					List<UserRoleView> URVs = UserRoleViewPersistenceWrapper.getTURBasedOnRoleNameAndTenantId(
							Constant.SYSADMIN, "ae783b6d-655a-11e7-aa10-fa163ed7d0ae");

					StringBuffer strBuffer = new StringBuffer();
					for (UserRoleView urv : URVs) {
						strBuffer.append(urv.getUserName());
						strBuffer.append(",");
					}
					variables.put(STPSYSTEMADMINACCOUNTS, strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
					variables.put(STPFLOWMESSAGE, flowAction);
				} else {
					// if the tenant is not root tenant, the parent tenant admin
					// deal with the apply
					List<UserRoleView> URVs = UserRoleViewPersistenceWrapper
							.getTURBasedOnRoleNameAndTenantId(Constant.TENANTADMIN, parentTenantId);
					StringBuffer strBuffer = new StringBuffer();
					for (UserRoleView urv : URVs) {
						strBuffer.append(urv.getUserName());
						strBuffer.append(",");
					}
					variables.put(STPSYSTEMADMINACCOUNTS, strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
					variables.put(STPFLOWMESSAGE, flowAction);
				}

			} else {
				LOG.warn("The action is not correct: action={0}", flowAction);
				return;
			}
		}

		pe.getTaskService().complete(taskId, variables);
	}

	public void completeParentTenantAdminTask(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getTaskService().complete(taskId);
	}
	
	
	public String getProcessBindingTenantId(String taskId){
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		String tenantId = pe.getTaskService().getVariable(taskId, WorkflowConstant.PROCESSBINDINGTENANTID_).toString();
		return tenantId;
	}
	

}
