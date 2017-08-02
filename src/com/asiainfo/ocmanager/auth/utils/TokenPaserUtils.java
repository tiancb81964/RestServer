package com.asiainfo.ocmanager.auth.utils;

/**
 * 
 * @author zhaoyim
 *
 */
public class TokenPaserUtils {

	/**
	 * 
	 * @param token
	 * @return username form the token
	 */
	public static String paserUserName(String token) {
		// token first string is username
		return token.split("_")[0];
	}
}
