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
	public final static int FORBIDDEN = 403;
	
	// token
	public final static int EMPTY_TOKEN = 4001;
	
	// user
	public final static int NO_CREATE_USER_PERMISSION = 4011;
	public final static int NO_UPDATE_USER_PERMISSION = 4012;
	public final static int NO_UPDATE_USER_PASSWORD_PERMISSION = 4013;
	public final static int NO_DELETE_USER_PERMISSION = 4014;
	public final static int USER_CAN_NOT_DELETE = 4015;
	// sb
	public final static int NO_ADD_SERVICE_BROKER_PERMISSION = 4016;
	public final static int NO_DELETE_SERVICE_BROKER_PERMISSION = 4017;
	// tenant
	public final static int NO_PERMISSION_ON_TENANT = 4018;
	public final static int NOT_SYSTEM_ADMIN = 4019;

	
	public final static int USER_EXIST = 4091;
	
	public final static int USER_NOT_FOUND = 4041;


}
