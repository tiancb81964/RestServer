package com.asiainfo.ocmanager.rest.bean;

public class AssignmentInfoBean {

	private String instanceName;
	private String assignmentStatus;
	private String phase;
	private String action;
	private String patch;

	public AssignmentInfoBean(){
		
	}
	
	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public AssignmentInfoBean(String instanceName, String assignmentStatus){
		this.instanceName = instanceName;
		this.assignmentStatus = assignmentStatus;
	}
	
	public AssignmentInfoBean(String instanceName, String assignmentStatus, String phase, String action, String patch) {
		this.instanceName = instanceName;
		this.assignmentStatus = assignmentStatus;
		this.phase = phase;
		this.action = action;
		this.patch = patch;
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

	@Override
	public String toString() {
		return "AssignmentInfoBean [instanceName=" + instanceName + ", assignmentStatus=" + assignmentStatus
				+ ", phase=" + phase + ", action=" + action + ", patch=" + patch + "]";
	}

}
