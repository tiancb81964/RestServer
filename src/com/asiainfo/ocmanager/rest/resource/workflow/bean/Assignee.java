package com.asiainfo.ocmanager.rest.resource.workflow.bean;

import java.io.Serializable;

/**
 * 
 * @author zhaoyim
 *
 */
public class Assignee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5275907500765517027L;
	private String assigneeName;

	public Assignee() {

	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	@Override
	public String toString() {
		return "Assignee: {assigneeName: " + assigneeName + "}";
	}

}
