package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ServiceMapper;
import com.asiainfo.ocmanager.persistence.model.Service;
import com.asiainfo.ocmanager.persistence.DBConnectorFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServicePersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<Service> getAllServices() {
		SqlSession session = DBConnectorFactory.getSession();
		List<Service> services = new ArrayList<Service>();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);

			services = mapper.selectAllServices();

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return services;
	}

	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	public static Service getServiceById(String serviceId) {
		SqlSession session = DBConnectorFactory.getSession();
		Service service = null;
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);
			service = mapper.selectServiceById(serviceId);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return service;
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	public static void addService(Service service) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);

			mapper.insertService(service);
			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param originList
	 * @return
	 */
	public static List<Service> getServicesByOrigin(List<String> originList) {
		SqlSession session = DBConnectorFactory.getSession();
		List<Service> services = new ArrayList<Service>();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);

			services = mapper.selectServicesByOrigin(originList);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return services;
	}

}
