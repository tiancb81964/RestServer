package com.asiainfo.ocmanager.rest.resource.persistence;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.BrokerMapper;
import com.asiainfo.ocmanager.persistence.model.Broker;

/**
 * 
 * @author zhaoyim
 *
 */
public class BrokerPersistenceWrapper {

	public static void insert(Broker broker) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			BrokerMapper mapper = session.getMapper(BrokerMapper.class);
			mapper.insert(broker);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public static void updateURL(String broker_name, String url) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			BrokerMapper mapper = session.getMapper(BrokerMapper.class);
			mapper.updateURL(broker_name, url);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
