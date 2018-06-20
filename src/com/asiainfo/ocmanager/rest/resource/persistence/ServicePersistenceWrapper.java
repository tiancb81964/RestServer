package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.ServiceMapper;
import com.asiainfo.ocmanager.persistence.model.Service;

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

	/**
	 * 
	 * @param serviceTypes
	 * @return
	 */
	public static List<Service> getServicesByServiceType(List<String> serviceTypes) {
		SqlSession session = DBConnectorFactory.getSession();
		List<Service> services = new ArrayList<Service>();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);

			services = mapper.selectServicesByServiceType(serviceTypes);

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
	 * @param service
	 * @return
	 */
	public static Service updateService(Service service) {

		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);
			mapper.updateService(service);
			// enhance the performance not select from db return the input
			// service directly
			// service = mapper.selectServiceByName(service.getServicename());
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
