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

}
