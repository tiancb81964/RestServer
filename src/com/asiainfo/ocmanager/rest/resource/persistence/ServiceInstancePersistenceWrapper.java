package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ServiceInstanceMapper;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.DBConnectorFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstancePersistenceWrapper {

	/**
	 * 
	 * @param tenantId
	 * @return
	 */
	public static List<ServiceInstance> getServiceInstancesInTenant(String tenantId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<ServiceInstance> serviceInstances = new ArrayList<ServiceInstance>();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);

			serviceInstances = mapper.selectServiceInstancesByTenant(tenantId);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return serviceInstances;
	}

	/**
	 * 
	 * @param tenant
	 */
	public static void createServiceInstance(ServiceInstance serviceInstance) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);

			mapper.insertServiceInstance(serviceInstance);

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
	 * @param tenantId
	 * @param instanceName
	 */
	public static void deleteServiceInstance(String tenantId, String instanceName) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);

			mapper.deleteServiceInstance(tenantId, instanceName);

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
	 * @return
	 */
	public static List<ServiceInstance> getAllServiceInstances() {
		SqlSession session = DBConnectorFactory.getSession();
		List<ServiceInstance> serviceInstances = new ArrayList<ServiceInstance>();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);

			serviceInstances = mapper.selectAllServiceInstances();

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return serviceInstances;
	}

	/**
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param quota
	 */
	public static void updateServiceInstanceQuota(String tenantId, String instanceName, String quota) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);

			mapper.updateInstanceQuota(tenantId, instanceName, quota);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	/**
	 * Get serviceInstance by tenantID and instanceName
	 * @param tenantId
	 * @param instanceName
	 * @return
	 */
	public static ServiceInstance getServiceInstance(String tenantId, String instanceName) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);
			ServiceInstance serviceInstance = mapper.getServiceInstance(tenantId, instanceName);
			return serviceInstance;
		} catch (Exception e) {
			throw e;
		}finally {
			session.close();
		}
	}

}
