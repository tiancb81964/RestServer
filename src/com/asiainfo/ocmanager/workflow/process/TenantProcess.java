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
public class TenantProcess extends Process {
	private static final Logger logger = LoggerFactory.getLogger(TenantProcess.class);

	private final static String TENANTPROCESS = "TenantProcess";
	private final static String TPFLOWMESSAGE_ = "TPFLOWMESSAGE_";

	private final static String TPTENANTADMINACCOUNT_ = "TPtenantAdminAccount_";
	private final static String TPPARENTTENANTADMINACCOUNTS_ = "TPparentTenantAdminAccounts_";

	private final static String TPAPPLICANTSUBMIT_ = "TPApplicantSubmit_";
	private final static String TPAPPLICANTCANCEL_ = "TPApplicantCancel_";

	public ProcessInstance startProcessInstance(String userAcount, Map<String, Object> variables) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		// Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(TPTENANTADMINACCOUNT_, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(TENANTPROCESS, variables);
		logger.info("Start Apply Tenant Process successfully!");

		return pi;
	}

	public ProcessInstance startProcessInstance(String userAcount) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(TPTENANTADMINACCOUNT_, userAcount);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(TENANTPROCESS, variables);

		return pi;
	}

	public void completeTenantAdminTask(String taskId, String flowAction, String parentTenantId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();

		Map<String, Object> variables = new HashMap<String, Object>();

		if (flowAction.equals(TPAPPLICANTCANCEL_)) {
			variables.put(TPFLOWMESSAGE_, flowAction);
		} else {
			if (flowAction.equals(TPAPPLICANTSUBMIT_)) {
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
					variables.put(TPPARENTTENANTADMINACCOUNTS_,
							strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
					variables.put(TPFLOWMESSAGE_, flowAction);
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
					variables.put(TPPARENTTENANTADMINACCOUNTS_,
							strBuffer.deleteCharAt(strBuffer.length() - 1).toString());
					variables.put(TPFLOWMESSAGE_, flowAction);
				}

			} else {
				logger.warn("The action is not correct: action={0}", flowAction);
				return;
			}
		}

		pe.getTaskService().complete(taskId, variables);
	}

	public void completeParentTenantAdminTask(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		pe.getTaskService().complete(taskId);
	}

	public String getProcessBindingTenantId(String taskId) {
		ProcessEngine pe = ActivitiConfiguration.getProcessEngine();
		String tenantId = pe.getTaskService().getVariable(taskId, WorkflowConstant.TPPROCESSBINDINGTENANTID_)
				.toString();
		return tenantId;
	}

}
