package com.asiainfo.ocmanager.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.asiainfo.ocmanager.rest.utils.DFPropertiesFoundry;

/**
 * 
 * @author zhaoyim
 *
 */

public class DBConnectorFactory {

	// TODO the db connection need to change not use the one connection
	public static SqlSessionFactory sessionFactory;

	static {

		try {
			String resource = "com/asiainfo/ocmanager/persistence/configuration.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			// we deployed in tomcat the path is: <tomcat
			// home>/webapps/ocmanager/
			// it will get <tomcat home>/webapps/ocmanager/classes/
			String currentClassPath = new DFPropertiesFoundry().getClass().getResource("/").getPath();
			// remove classes/
			// the path will be <tomcat home>/webapps/ocmanager/
			// for unit test
			// String propertiesFilePath = currentClassPath
			// + "../ocmanager/WEB-INF/conf/mysql.properties";
			String propertiesFilePath = currentClassPath + "../conf/mysql.properties";

			InputStream propInputStream = new FileInputStream(new File(propertiesFilePath));
			Properties prop = new Properties();
			prop.load(propInputStream);

			sessionFactory = new SqlSessionFactoryBuilder().build(inputStream, prop);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static SqlSession getSession() {
		return sessionFactory.openSession();
	}

}
