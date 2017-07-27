package com.asiainfo.ocmanager.auth;

import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by gq on 17/7/25.
 */
public class JdbcEnhancedRealm extends JdbcRealm{
    public void clearCachedInfoWhenPasswordChanged(PrincipalCollection principals) {
        this.clearCache(principals);
    }
}
