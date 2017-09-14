package com.asiainfo.ocmanager.rest.resource.utils.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantQuotaCheckerResponse {

	private boolean canChange;
	private String messages;

	public boolean isCanChange() {
		return canChange;
	}

	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "TenantQuotaCheckerResponse [canChange: " + canChange + ", messages: " + messages + "]";
	}

}
