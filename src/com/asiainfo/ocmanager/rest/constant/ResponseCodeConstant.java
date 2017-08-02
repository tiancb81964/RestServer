package com.asiainfo.ocmanager.rest.constant;

/**
 * 
 * @author zhaoyim
 *
 */
public class ResponseCodeConstant {
	/*****************************************************************/
	/***************** adapter response code const *******************/
	/*****************************************************************/
	// common
	public final static int SUCCESS = 200;
	
	// token
	public final static int EMPTY_TOKEN = 4001;
	
	// user
	public final static int NO_CREATE_USER_PERMISSION = 4031;
	public final static int NO_UPDATE_USER_PERMISSION = 4032;
	public final static int NO_UPDATE_USER_PASSWORD_PERMISSION = 4033;
	public final static int NO_DELETE_USER_PERMISSION = 4034;
	public final static int USER_CAN_NOT_DELETE = 4035;
	
	public final static int USER_EXIST = 4091;
	
	public final static int USER_NOT_FOUND = 4041;
}
