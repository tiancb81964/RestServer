package com.asiainfo.ocmanager.rest.bean;

public class LdapBean {
	private String ENABLE_LDAP;
	
	public String getENABLE_LDAP() {
		return ENABLE_LDAP;
	}

	public void setENABLE_LDAP(String eNABLE_LDAP) {
		ENABLE_LDAP = eNABLE_LDAP;
	}

	public LdapBean withStatus(boolean enable) {
		this.ENABLE_LDAP = String.valueOf(enable);
		return this;
	}

}
