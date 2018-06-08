package com.asiainfo.ocmanager.rest.resource.persistence;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.ClusterMapper;
import com.asiainfo.ocmanager.persistence.model.Cluster;

/**
 * 
 * @author zhaoyim
 *
 */
public class ClusterPersistenceWrapper {

	/**
	 * 
	 * @param clustername
	 * @return
	 */
	public static Cluster getClusterByName(String clustername) {
		SqlSession session = DBConnectorFactory.getSession();
		Cluster service = null;
		try {
			ClusterMapper mapper = session.getMapper(ClusterMapper.class);
			service = mapper.getClusterByName(clustername);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return service;
	}

}
