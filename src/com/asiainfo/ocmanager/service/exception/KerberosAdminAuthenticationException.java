package com.asiainfo.ocmanager.service.exception;

public class KerberosAdminAuthenticationException extends KerberosOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766795215828925333L;

	/**
	 * Creates a new KerberosAdminAuthenticationException with a message
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 */
	public KerberosAdminAuthenticationException(String message) {
		super(message);
	}

	/**
	 * Creates a new KerberosAdminAuthenticationException with a message and a
	 * cause
	 *
	 * @param message
	 *            a String containing the message indicating the reason for this
	 *            exception
	 * @param cause
	 *            a Throwable declaring the previously thrown Throwable that led
	 *            to this exception
	 */
	public KerberosAdminAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
