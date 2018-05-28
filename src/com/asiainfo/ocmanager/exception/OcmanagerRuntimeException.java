package com.asiainfo.ocmanager.exception;

/**
 * 
 * @author zhaoyim
 *
 */
public class OcmanagerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6911199525780681790L;

	public OcmanagerRuntimeException(Exception e) {
		super(e);
	}

	public OcmanagerRuntimeException(String msg, Exception e) {
		super(msg, e);
	}

	public OcmanagerRuntimeException(String msg) {
		super(msg);
	}

}
