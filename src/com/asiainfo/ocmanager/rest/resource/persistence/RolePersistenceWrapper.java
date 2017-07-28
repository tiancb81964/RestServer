package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.RoleMapper;
import com.asiainfo.ocmanager.persistence.model.Role;


/**
 * 
 * @author zhaoyim
 *
 */
public class RolePersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<Role> getRoles() {
		SqlSession session = DBConnectorFactory.getSession();
		List<Role> roles = new ArrayList<Role>();
		try {
			RoleMapper mapper = session.getMapper(RoleMapper.class);
			roles = mapper.selectAllRoles();

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return roles;
	}
}
