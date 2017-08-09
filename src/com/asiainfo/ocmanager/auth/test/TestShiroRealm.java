package com.asiainfo.ocmanager.auth.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by gq on 17/7/17.
 */
public class TestShiroRealm {

    @Test
    public void testLdapRealm() {
        try {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroLdap.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("gq2", "123");
        subject.login(token);
        System.out.println(token.toString());
        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void testJdbcRealm() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroJdbc.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("u1", "passw0rd");
        try {
            subject.login(token);
            System.out.println("login success!");
            System.out.println(token.toString());
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(subject.isAuthenticated());
        subject.logout();
    }

}
