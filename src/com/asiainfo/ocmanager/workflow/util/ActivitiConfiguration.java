package com.asiainfo.ocmanager.workflow.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.workflow.constant.WorkflowConstant;

/**
 * 
 * @author zhaoyim
 *
 */
public class ActivitiConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(ActivitiConfiguration.class);
	private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	public static ProcessEngine getProcessEngine() {

		if (processEngine == null) {
			synchronized (ActivitiConfiguration.class) {
				if (processEngine == null) {
					new ActivitiConfiguration();
				}
			}
		}

		return processEngine;

	}

	private ActivitiConfiguration() {
		init();
	}

	private void init() {

		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration().setJdbcDriver("com.mysql.jdbc.Driver")
				.setJdbcUrl("jdbc:mysql://10.1.236.95:3306/activiti?useUnicode=true&characterEncoding=utf8")
				.setJdbcUsername("root").setJdbcPassword("passw0rd")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		//TODO should changed now it just for testing
//		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
//				.createStandaloneProcessEngineConfiguration()
//				.setJdbcDriver(MysqlConfiguration.getConf().getProperty(Constant.JDBC_DRIVER))
//				.setJdbcUrl(MysqlConfiguration.getConf().getProperty(Constant.JDBC_URL) + "?"
//						+ MysqlConfiguration.getConf().getProperty(Constant.JDBC_ENCODING))
//				.setJdbcUsername(MysqlConfiguration.getConf().getProperty(Constant.JDBC_USERNAME))
//				.setJdbcPassword(MysqlConfiguration.getConf().getProperty(Constant.JDBC_PASSWORD))
//				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		processEngine = processEngineConfiguration.buildProcessEngine();

		LOG.info("init Process Engine successfully! processEngine: ", processEngine.getName());
	}

}
