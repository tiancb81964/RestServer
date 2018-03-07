package com.asiainfo.ocmanager.service.client.v2;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy delegator, by which service would perform actions. Moreover, privileged actions can be 
 * performed by calling {@link #doAsPrivileged(SomeAction)}
 * 
 * @author Ethan
 *
 */
public abstract class Delegator {
	private static final Logger LOG = LoggerFactory.getLogger(Delegator.class);
	private Subject subject; // identity of current connector

	public Delegator(Subject subject) {
		this.subject = subject;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 * Do action by current connector.
	 * 
	 * @throws PrivilegedActionException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public final <T> T doAsPrivileged(SomeAction<T> action) throws InterruptedException, IOException {
		if (subject == null) {
			LOG.error("Subject of connector [{}] is null.", this.toString());
			throw new RuntimeException("Subject is null.");
		}
		try {
			return Subject.doAs(subject, action);
		} catch (PrivilegedActionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				LOG.error("Exception while do action: ", cause);
				throw (IOException) cause;
			} else if (cause instanceof Error) {
				LOG.error("Exception while do action: ", cause);
				throw (Error) cause;
			} else if (cause instanceof RuntimeException) {
				LOG.error("Exception while do action: ", cause);
				throw (RuntimeException) cause;
			} else if (cause instanceof InterruptedException) {
				LOG.error("Exception while do action: ", cause);
				throw (InterruptedException) cause;
			} else {
				LOG.error("Exception while do action: ", cause);
				throw new UndeclaredThrowableException(cause);
			}
		}
	}

	public String toString() {
		return "subject=" + subject;
	}

}
