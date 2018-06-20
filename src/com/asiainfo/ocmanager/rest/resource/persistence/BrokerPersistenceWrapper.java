package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.List;

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
	
	public static Broker getBrokerByName(String name) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			BrokerMapper mapper = session.getMapper(BrokerMapper.class);
			Broker broker = mapper.getBrokerByName(name);
			session.commit();
			return broker;
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
	
	public static void updateStatus(String broker_name, String status) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			BrokerMapper mapper = session.getMapper(BrokerMapper.class);
			mapper.updateStatus(broker_name, status);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public static List<Broker> getBrokers() {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			BrokerMapper mapper = session.getMapper(BrokerMapper.class);
			List<Broker> brokers = mapper.getBrokers();
			session.commit();
			return brokers;
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
