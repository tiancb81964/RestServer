package com.asiainfo.ocmanager.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for auditting
 * @author Ethan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface Audit {
	/**
	 * target type
	 * @return
	 */
	TargetType targetType();
	/**
	 * action on entity
	 * @return
	 */
	Action action();
	
	/**
	 * User-end action type
	 * @author Ethan
	 *
	 */
	public static enum Action {
		GET,
		CREATE,
		UPDATE,
		DELETE,
		LOGIN,
		LOGOUT,
		ASSIGN,
		UNASSIGN
	}
	
	/**
	 * Entity type to audit of user-end operation
	 * @author Ethan
	 *
	 */
	public static enum TargetType {
		TENANT,
		INSTANCE,
		USER,
		ROLE,
		SERVICE,
		ASSIGNMENT
	}
}
