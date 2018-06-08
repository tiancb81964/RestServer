package com.asiainfo.ocmanager.rest.resource.persistence;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.ApplyBillMapper;
import com.asiainfo.ocmanager.persistence.model.ApplyBill;

/**
 * 
 * @author zhaoyim
 *
 */
public class ApplyBillPersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<ApplyBill> getAllApplyBills() {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			List<ApplyBill> bills = mapper.selectAllBills();
			session.commit();
			return bills;
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
	 * @return
	 */
	public static ApplyBill getAllApplyBillById(String id) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			ApplyBill bill = mapper.selectBillById(id);
			session.commit();
			return bill;
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param applyUser
	 * @return
	 */
	public static List<ApplyBill> getApplyBillsByUser(String applyUser) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			List<ApplyBill> bills = mapper.selectBillsByApplyUser(applyUser);
			session.commit();
			return bills;
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param applyUser
	 * @param applyQuota
	 * @param applyReason
	 * @param status
	 */
	public static ApplyBill createApplyBill(ApplyBill applyBill) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			mapper.insertApplyBill(applyBill);

			ApplyBill bill = mapper.selectBillById(applyBill.getId());
			session.commit();

			return bill;
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
	 * @param applyUser
	 * @param applyQuota
	 * @param applyReason
	 * @param status
	 */
	public static ApplyBill updateApplyBill(ApplyBill applyBill) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			mapper.updateApplyBillById(applyBill);

			ApplyBill bill = mapper.selectBillById(applyBill.getId());
			session.commit();

			return bill;

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
	 * @param status
	 */
	public static ApplyBill updateApplyBillStatus(String id, int status) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			mapper.updateApplyBillStatusById(id, status);
			ApplyBill bill = mapper.selectBillById(id);
			session.commit();

			return bill;
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
	public static void deleteApplyBill(String id) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			mapper.deleteApplyBillById(id);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
