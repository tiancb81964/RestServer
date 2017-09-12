package com.asiainfo.ocmanager.rest.resource.utils.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantQuotaCheckerResponse {

	private boolean canCreate;
	private String messages;

	public boolean isCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "TenantQuotaCheckerResponse [canCreate: " + canCreate + ", messages: " + messages + "]";
	}

}
