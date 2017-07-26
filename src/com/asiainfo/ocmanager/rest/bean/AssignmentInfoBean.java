package com.asiainfo.ocmanager.rest.bean;

public class AssignmentInfoBean {

	private String instanceName;
	private String assignmentStatus;

	public AssignmentInfoBean(){
		
	}
	
	public AssignmentInfoBean(String instanceName, String assignmentStatus){
		this.instanceName = instanceName;
		this.assignmentStatus = assignmentStatus;
	}
	
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

}
