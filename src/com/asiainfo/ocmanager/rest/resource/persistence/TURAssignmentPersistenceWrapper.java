package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.TenantUserRoleAssignmentMapper;
import com.asiainfo.ocmanager.persistence.model.TenantUserRoleAssignment;
import com.asiainfo.ocmanager.persistence.DBConnectorFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class TURAssignmentPersistenceWrapper {

	/**
	 * 
	 * @param assignment
	 */
	public static TenantUserRoleAssignment assignRoleToUserInTenant(TenantUserRoleAssignment assignment) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			TenantUserRoleAssignmentMapper mapper = session.getMapper(TenantUserRoleAssignmentMapper.class);

			mapper.insertTenantUserRoleAssignment(assignment);
			assignment = mapper.selectAssignmentByTenantUserRole(assignment.getTenantId(), assignment.getUserId(), assignment.getRoleId());
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return assignment;
	}
	
	/**
	 * 
	 * @param assignment
	 */
	public static TenantUserRoleAssignment updateRoleToUserInTenant(TenantUserRoleAssignment assignment) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			TenantUserRoleAssignmentMapper mapper = session.getMapper(TenantUserRoleAssignmentMapper.class);

			mapper.updateTenantUserRoleAssignment(assignment);
			assignment = mapper.selectAssignmentByTenantUserRole(assignment.getTenantId(), assignment.getUserId(), assignment.getRoleId());
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return assignment;
	}
	
	
	/**
	 * 
	 * @param tenantId
	 * @param userId
	 */
	public static void unassignRoleFromUserInTenant(String tenantId, String userId) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			TenantUserRoleAssignmentMapper mapper = session.getMapper(TenantUserRoleAssignmentMapper.class);

			mapper.deleteTenantUserRoleAssignment(tenantId, userId);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public static List<TenantUserRoleAssignment> getTenantAdmin(String tenantId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<TenantUserRoleAssignment> assignment;
		try {
			TenantUserRoleAssignmentMapper mapper = session.getMapper(TenantUserRoleAssignmentMapper.class);
			assignment = mapper.getTenantAdmin(tenantId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return assignment;
	}
	
	public static List<TenantUserRoleAssignment> getUsers(String tenantId) {
		SqlSession session = DBConnectorFactory.getSession();
		List<TenantUserRoleAssignment> assignment;
		try {
			TenantUserRoleAssignmentMapper mapper = session.getMapper(TenantUserRoleAssignmentMapper.class);
			assignment = mapper.getUsers(tenantId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
		return assignment;
	}
}
