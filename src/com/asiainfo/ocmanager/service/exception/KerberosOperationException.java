package com.asiainfo.ocmanager.service.exception;

public class KerberosOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8185230931230851208L;

	/**
	 * Creates a new KerberosOperationException with a message
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 */
	public KerberosOperationException(String message) {
		super(message);
	}

	/**
	 * Creates a new KerberosOperationException with a message and a cause
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 * @param cause
	 *            a Throwable declaring the previously thrown Throwable that led
	 *            to this exception
	 */
	public KerberosOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}
