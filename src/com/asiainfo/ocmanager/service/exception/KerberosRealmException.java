package com.asiainfo.ocmanager.service.exception;

public class KerberosRealmException extends KerberosOperationException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8949262233314793721L;

	/**
	 * Creates a new KerberosRealmException with a message
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 */
	public KerberosRealmException(String message) {
		super(message);
	}

	/**
	 * Creates a new KerberosRealmException with a message and a cause
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 * @param cause
	 *            a Throwable declaring the previously thrown Throwable that led
	 *            to this exception
	 */
	public KerberosRealmException(String message, Throwable cause) {
		super(message, cause);
	}
}