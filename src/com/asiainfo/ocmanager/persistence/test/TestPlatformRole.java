package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.PlatformRoleMapper;
import com.asiainfo.ocmanager.persistence.model.PlatformRole;

/**
 * 
 * @author zhaoyim
 *
 */
public class TestPlatformRole {

	public static void main(String[] args) {
		SqlSession session = TestDBConnectorFactory.getSession();
		try {
			PlatformRoleMapper mapper = session.getMapper(PlatformRoleMapper.class);

			System.out.println("===========select all platform roles==============");
			List<PlatformRole> pRoles = mapper.selectAllPlatformRoles();
			for (PlatformRole pr : pRoles) {
				System.out.println(pr.toString());
			}

			System.out.println("===========select platform role by id==============");
			PlatformRole pRole = mapper.selectPlatformRoleById(1);
			System.out.println(pRole.toString());

			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}

}
