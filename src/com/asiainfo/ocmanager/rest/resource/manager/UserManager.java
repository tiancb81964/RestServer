package com.asiainfo.ocmanager.rest.resource.manager;

import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.rest.resource.utils.UserPersistenceWrapper;

/**
 * 
 * @author zhaoyim
 *
 */
public class UserManager {

	/**
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	public static boolean isValidUserById(String userId, String password) {
		User user = UserPersistenceWrapper.getUserByIdAndPwd(userId, password);
		if (user != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static boolean isValidUserByName(String userName, String password) {
		User user = UserPersistenceWrapper.getUserByNameAndPwd(userName, password);
		if (user != null) {
			return true;
		}
		return false;
	}

}
