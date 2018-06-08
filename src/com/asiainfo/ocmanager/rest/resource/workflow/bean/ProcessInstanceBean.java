package com.asiainfo.ocmanager.rest.resource.workflow.bean;

import java.io.Serializable;

/**
 * 
 * @author zhaoyim
 *
 */
public class ProcessInstanceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368903900373890949L;
	private String processInstanceId;
	private String processInstanceName;

	public ProcessInstanceBean() {

	}

	public ProcessInstanceBean(String processInstanceId, String processInstanceName) {
		this.processInstanceId = processInstanceId;
		this.processInstanceName = processInstanceName;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

}
