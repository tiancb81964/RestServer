package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.UserRoleMapper;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.persistence.DBConnectorFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class UserRoleViewPersistenceWrapper {

	/**
	 * 
	 * @param tenantId
	 * @return
	 */
	public static List<UserRoleView> getUsersInTenant(String tenantId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<UserRoleView> usersWithRoles = new ArrayList<UserRoleView>();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			usersWithRoles = mapper.selectUsersRolesInTenant(tenantId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return usersWithRoles;
	}

	/**
	 * 
	 * @param userName
	 * @param tenantId
	 * @return
	 */
	public static UserRoleView getRoleBasedOnUserAndTenant(String userName, String tenantId) {
		SqlSession session = DBConnectorFactory.getSession();
		UserRoleView RUT = new UserRoleView();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			RUT = mapper.selectRoleBasedOnUserAndTenant(userName, tenantId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return RUT;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public static List<UserRoleView> getTenantAndRoleBasedOnUserId(String userId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<UserRoleView> turs = new ArrayList<UserRoleView>();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			turs = mapper.selectTenantAndRoleBasedOnUserId(userId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return turs;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static List<UserRoleView> getTenantAndRoleBasedOnUserName(String userName) {
		SqlSession session = DBConnectorFactory.getSession();
		List<UserRoleView> turs = new ArrayList<UserRoleView>();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			turs = mapper.selectTenantAndRoleBasedOnUserName(userName);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return turs;
	}

	
	/**
	 * 
	 * @param userName
	 * @param roleId
	 * @return
	 */
	public static List<UserRoleView> getTURBasedOnUserNameAndRoleId(String userName, String roleId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<UserRoleView> turs = new ArrayList<UserRoleView>();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			turs = mapper.selectTURBasedOnUserNameAndRoleId(userName, roleId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return turs;
	}
}
