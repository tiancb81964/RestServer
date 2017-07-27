package com.asiainfo.ocmanager.persistence.test;

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

public class TestDBConnectorFactory {

	public static SqlSessionFactory sessionFactory;

	static {

		try {
			String resource = "com/asiainfo/ocmanager/persistence/configuration.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);

			String currentClassPath = new DFPropertiesFoundry().getClass().getResource("/").getPath();
			String propertiesFilePath = currentClassPath.substring(0, currentClassPath.length() - 4)
					+ "WebContent/WEB-INF/conf/mysql.properties";

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

	public static void main(String[] args) {

	}

}
