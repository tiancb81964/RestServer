package com.asiainfo.ocmanager.auth;
import com.asiainfo.ocmanager.rest.constant.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by gq on 17/7/18.
 */
public class Authenticator {
    static Logger logger = LoggerFactory.getLogger(Authenticator.class.getName());
    private static boolean isAuthcSuccess = false;

    public boolean loginWithUsernamePassword(String username, String password, String config) {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(config);
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        logger.info("Try to login with Username Password: " + token.toString());
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            logger.warn("Auth failed!"+ e.getMessage());
            return false;
        }
        return subject.isAuthenticated(); //断言用户已经登录
    }


    public static boolean loginWithToken(String Dtoken, String Dconfig) {
        String token = Dtoken;
        String shiroConfig = Dconfig;
        UsernamePasswordToken usernamePasswordToken;
        logger.info("Token: " + token);

        try {
            usernamePasswordToken = parseTokenWithTTl(token);
        }
        catch (Exception e) {
            logger.warn("parse token Exception: " + e.getMessage());
            return false;
        }
        boolean cacheLoginsuccess = false;

        if(!cacheLoginsuccess) {
            Factory<SecurityManager> factory =
                    new IniSecurityManagerFactory(shiroConfig);
            org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);
            Subject subject = SecurityUtils.getSubject();
            try {
                logger.info("Starting login authenticate...");
                subject.login(usernamePasswordToken);
            }catch (AuthenticationException e) {
                logger.error(e.toString());
            }
            if (subject.isAuthenticated()){
                logger.info("Authentication success from "+ subject.getPrincipals().getRealmNames());
                isAuthcSuccess = true;
            }else {
                logger.info("Authenticated Failed");
                isAuthcSuccess = false;
            }
            subject.logout();
        }
        return isAuthcSuccess;
    }

    public static String generateToken(String username,String password) {

        String encryptPwd = AESUtils.encrypt(password,username);
        String token = username+"_"+encryptPwd;
        return token;

    }

    public static String generateTokenWithTTL(String username,String password) {
        Date curDate = new Date();
        Long ttl = Constant.AUTHTOKENTTL;
        Long expiredTime = curDate.getTime()+ttl;
        Date expiredDate = new Date(expiredTime);
        logger.info("Generated token will be exproed in " + expiredDate.toString());
        String encryptTokenPart = AESUtils.encrypt(password + "_" + String.valueOf(expiredTime),username);
        String token = username+"_"+encryptTokenPart;
        return token;
    }

    public static UsernamePasswordToken parseTokenWithTTl(String token) throws Exception{
        String[] tokenArgs = token.split("_");
        String username = tokenArgs[0];
        String encryptedContent = tokenArgs[1];

        String content = AESUtils.decrypt(encryptedContent,username);
        if (content == null) {
            logger.warn("Exception in decryption of token. ");
            throw new Exception("Decryption of token error.");
        }
        logger.debug("Decrypted secrest content: " + content );
        String[] contentArray = content.split("_");
        Date expiredTime = new Date(Long.parseLong(contentArray[1]));
        logger.info("Token expiredTime: " + expiredTime.toString());
        if (expiredTime.before(new Date())) {
            throw new Exception("Token expired!");
        }
        String password = contentArray[0];
        logger.info("Password:" + password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
        return usernamePasswordToken;

    }

}
