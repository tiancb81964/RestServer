package com.asiainfo.ocmanager.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;

import com.asiainfo.ocmanager.rest.resource.utils.UserUtils;

/**
 * Created by gq on 17/7/27.
 */
public class EncryptedPasswordDaoRealm extends AuthenticatingRealm{
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        if (username == null || password == null) {
            throw new AccountException("Null username or password is not allowed by this realm.");
        } else {
            if (UserUtils.isValidUserByName(username, password)) {
                return new SimpleAuthenticationInfo(username, password, this.getName());
            } else {
                return new SimpleAuthenticationInfo(username, "", this.getName());
            }
        }
    }

}
