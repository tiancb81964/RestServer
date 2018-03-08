package com.asiainfo.ocmanager.service.client.v2;

import java.io.IOException;
import java.security.PrivilegedActionException;

import javax.security.auth.Subject;

import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegator, by which service would perform actions. Moreover, privileged
 * actions can be performed by calling {@link #doAsPrivileged(SomeAction)}
 * 
 * @author Ethan
 *
 */
public class Delegator {
	private static final Logger LOG = LoggerFactory.getLogger(Delegator.class);
	private Object subject; // identity of current connector
	private boolean isUGI = false;

	/**
	 * Construct a new delegator with the given subject which can be instance of
	 * either {@link Subject} or {@link UserGroupInformation}
	 * 
	 * @param subject
	 */
	public Delegator(Object subject) {
		if (subject == null) {
			subject = new Subject();
		}
		if (subject instanceof Subject) {
			this.subject = subject;
			this.isUGI = false;
		} else if (subject instanceof UserGroupInformation) {
			this.subject = subject;
			this.isUGI = true;
		} else {
			LOG.error("Error: illegal subject type: " + subject.getClass().getName());
			throw new RuntimeException("illegal subject type: " + subject.getClass().getName());
		}
	}

	/**
	 * If the delegator is ugi
	 * 
	 * @return
	 */
	public boolean isUGI() {
		return isUGI;
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
			LOG.error("Subject is null.", this.toString());
			throw new RuntimeException("Subject is null.");
		}
		if (isUGI) {
			UserGroupInformation ugi = (UserGroupInformation) subject;
			return ugi.doAs(action);
		} else {
			Subject sub = (Subject) subject;
			try {
				return Subject.doAs(sub, action);
			} catch (PrivilegedActionException e) {
				LOG.error("Exception while doAs: ", e);
				throw new IOException("Exception while doAs: ", e);
			}
		}
	}

	public String toString() {
		return "Delegator=[isUGI=" + this.isUGI + "	subject=" + this.subject;
	}

}
