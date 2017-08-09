package com.asiainfo.ocmanager.auth;
import com.asiainfo.ocmanager.auth.utils.AESUtils;
import com.asiainfo.ocmanager.auth.utils.CacheUtils;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ServerConfiguration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

/**
 * Created by gq on 17/7/18.
 */
public class Authenticator {
    static Logger logger = LoggerFactory.getLogger(Authenticator.class.getName());

    public boolean loginWithUsernamePassword(String username, String password) {
        boolean isAuthcSuccess;
        String confPath = this.getClass().getResource("/").getPath() + ".." + File.separator + "conf" + File.separator;
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(confPath + Constant.SHIROINIPATHS.get(getAuthType()));
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        logger.info("Try to login with Username Password: " + token.toString());
        Session session=subject.getSession();
        logger.info("Session Id: [{}], Timeout : [{}]",session.getId(),String.valueOf(session.getTimeout()));

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            logger.warn("Auth failed!"+ e.getMessage());
            session.stop();
//            e.printStackTrace();
            return false;
        }
        if (subject.isAuthenticated()){
            logger.info("loginWithUsernamePassword: Authentication success from "+ subject.getPrincipals().getRealmNames());
            isAuthcSuccess = true;
        }else {
            logger.info("Authenticated Failed");
            isAuthcSuccess = false;
        }
        logger.info("Session Id: [{}], Timeout : [{}]",subject.getSession().getId(),String.valueOf(subject.getSession().getTimeout()));
        subject.logout();
        return isAuthcSuccess;
    }

    public static boolean loginWithToken(String token) {
        return CacheUtils.isTokenInCache(token);
    }

    public static void logout(String username) {
        CacheUtils.removeToken(username);
        logger.info("remove user [{}] token in Cache.");
    }

    public static String generateToken(String username,String password) {
        Date curDate = new Date();
        Long curTime = curDate.getTime();
        String encryptTokenPart = AESUtils.encrypt(password + "_" + String.valueOf(curTime),username);
        String token = username+"_"+encryptTokenPart;
        CacheUtils.addToken(username,token);
        logger.info("Add token [{}] in Cache.", token);
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
        CacheUtils.addToken(username,token);
        logger.info("Add token [{}] in Cache.", token);
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
        logger.debug("Decrypted secret content: " + content );
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

	/**
	 * Auth type, with ladp(0) and mysql(1).
	 * @author EthanWang
	 *
	 */
	public enum AuthType
	{
		ldap(0),
		mysql(1);
		private int type;
		private AuthType(int type)
		{
			this.type = type;
		}

		public int code() {
			return type;
		}
	}

    public static int getAuthType() {
        AuthType type = AuthType.valueOf(ServerConfiguration.getConf().getProperty(Constant.AUTHTYPE));
        return type.code();
    }

}
