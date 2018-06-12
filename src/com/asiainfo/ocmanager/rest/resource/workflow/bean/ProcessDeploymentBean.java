package com.asiainfo.ocmanager.rest.resource.workflow.bean;

import java.io.Serializable;

/**
 * 
 * @author zhaoyim
 *
 */
public class ProcessDeploymentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -889036101747314115L;
	private String processName;
	private String bpmnPath;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getBpmnPath() {
		return bpmnPath;
	}

	public void setBpmnPath(String bpmnPath) {
		this.bpmnPath = bpmnPath;
	}

	@Override
	public String toString() {
		return "ProcessDeploymentBean: {processName: " + processName + ", bpmnPath: " + bpmnPath + "}";
	}

}
