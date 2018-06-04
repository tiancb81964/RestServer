package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.UserRoleMapper;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;

public class TestUserRole {

	public static void main(String[] args) {
		SqlSession session = TestDBConnectorFactory.getSession();
		try {
			UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
			List<UserRoleView> urs = mapper.selectUsersRolesInTenant("t1");

			for (UserRoleView ur : urs) {
				System.out.println(ur.getUserId());
				System.out.println(ur.getUserName());
				System.out.println(ur.getUserDescription());
				System.out.println(ur.getUserEmail());
				System.out.println(ur.getUserPassword());
				System.out.println(ur.getUserPhone());
				System.out.println(ur.getRoleId());
				System.out.println(ur.getRoleName());
				System.out.println(ur.getTenantId());
			}
			session.commit();

			System.out.println("============");
			UserRoleView urs1 = mapper.selectRoleBasedOnUserAndTenant("u1", "t1");

			System.out.println(urs1.getUserId());
			System.out.println(urs1.getUserName());
			System.out.println(urs1.getUserDescription());
			System.out.println(urs1.getRoleId());
			System.out.println(urs1.getRoleName());
			System.out.println(urs1.getTenantId());

			session.commit();

			System.out.println("======User Id======");
			List<UserRoleView> urs2 = mapper.selectTenantAndRoleBasedOnUserId("u1");

			for (UserRoleView ur : urs2) {
				System.out.println(ur.getUserId());
				System.out.println(ur.getUserName());
				System.out.println(ur.getUserDescription());
				System.out.println(ur.getRoleId());
				System.out.println(ur.getRoleName());
				System.out.println(ur.getTenantId());
				System.out.println(ur.getTenantName());
			}
			session.commit();

			System.out.println("======User Name======");
			List<UserRoleView> urs3 = mapper.selectTenantAndRoleBasedOnUserName("u2");

			for (UserRoleView ur : urs3) {
				System.out.println(ur.getUserId());
				System.out.println(ur.getUserName());
				System.out.println(ur.getUserDescription());
				System.out.println(ur.getRoleId());
				System.out.println(ur.getRoleName());
				System.out.println(ur.getTenantId());
				System.out.println(ur.getTenantName());
			}
			session.commit();

			System.out.println("======User Name And Role Id======");
			List<UserRoleView> urs4 = mapper.selectTURBasedOnUserNameAndRoleId("u1", "r1");

			for (UserRoleView ur : urs4) {
				System.out.println(ur.getUserId());
				System.out.println(ur.getUserName());
				System.out.println(ur.getUserDescription());
				System.out.println(ur.getRoleId());
				System.out.println(ur.getRoleName());
				System.out.println(ur.getTenantId());
				System.out.println(ur.getTenantName());
			}
			session.commit();

			System.out.println("======Role Name And Tenant Id======");
			List<UserRoleView> urs5 = mapper.selectUserBasedOnRoleNameAndTenantId("tenant.admin", "admin-1528116924");

			for (UserRoleView ur : urs5) {
				System.out.println(ur.getUserId());
				System.out.println(ur.getUserName());
				System.out.println(ur.getUserDescription());
				System.out.println(ur.getRoleId());
				System.out.println(ur.getRoleName());
				System.out.println(ur.getTenantId());
				System.out.println(ur.getTenantName());
			}
			session.commit();

		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}

}
