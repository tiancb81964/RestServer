package com.asiainfo.ocmanager.rest.bean;

public class KerberosBean {
	private String ENABLE_KERBEROS;
	
	public String getENABLE_KERBEROS() {
		return ENABLE_KERBEROS;
	}

	public void setENABLE_KERBEROS(String eNABLE_KERBEROS) {
		ENABLE_KERBEROS = eNABLE_KERBEROS;
	}

	public KerberosBean withStatus(boolean enable) {
		this.ENABLE_KERBEROS = String.valueOf(enable);
		return this;
	}
	
}
