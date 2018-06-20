package com.asiainfo.ocmanager.rest.resource.utils.model;

public class DFRestResponse {
	private int status;
	private String entity;
	private String phrase;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public DFRestResponse(int status, String entity, String phrase) {
		this.status = status;
		this.entity = entity;
		this.phrase = phrase;
	}
	@Override
	public String toString() {
		return "DFRestResponse [status=" + status + ", entity=" + entity + ", phrase=" + phrase + "]";
	}
}
