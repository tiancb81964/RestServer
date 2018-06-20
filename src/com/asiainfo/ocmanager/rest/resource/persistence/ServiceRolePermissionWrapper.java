package com.asiainfo.ocmanager.rest.resource.persistence;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.ServiceRolePermissionMapper;
import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceRolePermissionWrapper {

	/**
	 * 
	 * @return
	 */
	public static ServiceRolePermission getServicePermissionByRoleId(String serviceName, String roleId) {
		SqlSession session = DBConnectorFactory.getSession();
		ServiceRolePermission permission = null;
		try {
			ServiceRolePermissionMapper mapper = session.getMapper(ServiceRolePermissionMapper.class);
			permission = mapper.selectPermissionByServiceNameRoleId(serviceName, roleId);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return permission;
	}

	/**
	 * 
	 */
	public static void deleteAll() {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceRolePermissionMapper mapper = session.getMapper(ServiceRolePermissionMapper.class);
			mapper.deleteAll();

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
	 * @param srp
	 */
	public static void addServiceRolePermission(ServiceRolePermission srp) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ServiceRolePermissionMapper mapper = session.getMapper(ServiceRolePermissionMapper.class);
			mapper.insertServiceRolePermission(srp);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
