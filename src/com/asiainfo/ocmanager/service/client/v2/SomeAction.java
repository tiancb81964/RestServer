package com.asiainfo.ocmanager.service.client.v2;

import java.security.PrivilegedExceptionAction;

/**
 * Client actions of certain user.
 * @author Ethan
 * @param <T>
 *
 */
public interface  SomeAction<T> extends PrivilegedExceptionAction<T>{
	
}
