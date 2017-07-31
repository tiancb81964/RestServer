package com.asiainfo.ocmanager.auth.realm;

import com.asiainfo.ocmanager.rest.resource.manager.UserManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;

/**
 * Created by gq on 17/7/27.
 */
public class SimpleRealm extends AuthenticatingRealm{
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        if (username == null || password == null) {
            throw new AccountException("Null username or password is not allowed by this realm.");
        } else {
            if (UserManager.isValidUserByName(username, password)) {
                return new SimpleAuthenticationInfo(username, password, this.getName());
            } else {
                return new SimpleAuthenticationInfo(username, "", this.getName());
            }
        }
    }

}
