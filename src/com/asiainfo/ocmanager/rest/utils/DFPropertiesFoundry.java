package com.asiainfo.ocmanager.rest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import com.asiainfo.ocmanager.rest.constant.Constant;

public class DFPropertiesFoundry {

	private static HashMap<String, String> map = new HashMap<String, String>();

	public static HashMap<String, String> getDFProperties() throws IOException {

		if (map.size() == 0) {
			synchronized (DFPropertiesFoundry.class) {
				if (map.size() == 0) {
					// we deployed in tomcat the path is: <tomcat
					// home>/webapps/ocmanager/
					// it will get <tomcat home>/webapps/ocmanager/classes/
					String currentClassPath = new DFPropertiesFoundry().getClass().getResource("/").getPath();
					String propertiesFilePath = currentClassPath + "../conf/dataFoundry.properties";
					InputStream inStream = new FileInputStream(new File(propertiesFilePath));
					Properties prop = new Properties();
					prop.load(inStream);
					map.put(Constant.DATAFOUNDRY_URL, prop.getProperty(Constant.DATAFOUNDRY_URL));
					map.put(Constant.DATAFOUNDRY_TOKEN, prop.getProperty(Constant.DATAFOUNDRY_TOKEN));
				}
			}
		}

		return map;

	}

}
