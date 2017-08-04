package com.asiainfo.ocmanager.auth.utils;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.ocmanager.persistence.model.User;

/**
 * Ldap utils
 * @author EthanWang
 *
 */
public class LdapUtils {
	
	public static List<User> transform(List<String> users){
		List<User> rsp = new ArrayList<>();
		for (String user : users) {
			User en = new User();
			en.setUsername(user);
			rsp.add(en);
		}
		return rsp;
	}

}
