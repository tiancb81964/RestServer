package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.DashboardMapper;
import com.asiainfo.ocmanager.persistence.model.Dashboard;

/**
 * 
 * @author zhaoyim
 *
 */
public class DashboardPersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<Dashboard> getAllLinks() {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			DashboardMapper mapper = session.getMapper(DashboardMapper.class);
			return mapper.selectAllLinks();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Dashboard getLinkByName(String name) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			DashboardMapper mapper = session.getMapper(DashboardMapper.class);
			return mapper.selectLinkByName(name);
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param dashboard
	 */
	public static void addLink(Dashboard dashboard) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			DashboardMapper mapper = session.getMapper(DashboardMapper.class);
			mapper.insertLink(dashboard);
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
	 * @param dashboard
	 */
	public static void updateLink(Dashboard dashboard) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			DashboardMapper mapper = session.getMapper(DashboardMapper.class);
			mapper.updateLink(dashboard);
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
	 * @param id
	 */
	public static void deleteLink(int id) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			DashboardMapper mapper = session.getMapper(DashboardMapper.class);
			mapper.deleteLink(id);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
