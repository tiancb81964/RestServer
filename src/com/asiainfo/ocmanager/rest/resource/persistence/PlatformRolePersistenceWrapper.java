package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.PlatformRoleMapper;
import com.asiainfo.ocmanager.persistence.model.PlatformRole;

/**
 * 
 * @author zhaoyim
 *
 */
public class PlatformRolePersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<PlatformRole> getAllplatformRoles() {
		SqlSession session = DBConnectorFactory.getSession();
		List<PlatformRole> pRoles = new ArrayList<PlatformRole>();
		try {
			PlatformRoleMapper mapper = session.getMapper(PlatformRoleMapper.class);
			pRoles = mapper.selectAllPlatformRoles();

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return pRoles;
	}

	/**
	 * 
	 * @param platformRoleId
	 * @return
	 */
	public static PlatformRole getPlatformRoleById(int platformRoleId) {
		SqlSession session = DBConnectorFactory.getSession();
		PlatformRole pRole = null;
		try {
			PlatformRoleMapper mapper = session.getMapper(PlatformRoleMapper.class);
			pRole = mapper.selectPlatformRoleById(platformRoleId);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return pRole;
	}

}
