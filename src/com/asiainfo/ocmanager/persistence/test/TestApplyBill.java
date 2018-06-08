package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ApplyBillMapper;
import com.asiainfo.ocmanager.persistence.model.ApplyBill;

public class TestApplyBill {

	public static void main(String[] args) {
		SqlSession session = TestDBConnectorFactory.getSession();
		try {

			ApplyBillMapper mapper = session.getMapper(ApplyBillMapper.class);
			ApplyBill ab11 = mapper.selectBillById("999");
			System.out.println(ab11.getApplyDate());
			System.out.println(ab11.getApplyQuota());
			System.out.println(ab11.getApplyReason());
			System.out.println(ab11.getApplyUser());
			System.out.println(ab11.getId());
			System.out.println(ab11.getStatus());
			
			System.out.println("insert=======");
			mapper.insertApplyBill(new ApplyBill("111", "zhaoyim", "{aa}", "bb", 2));
			session.commit();

			System.out.println("select all=======");
			List<ApplyBill> abs = mapper.selectAllBills();

			for (ApplyBill ab : abs) {
				System.out.println(ab.getApplyDate());
				System.out.println(ab.getApplyQuota());
				System.out.println(ab.getApplyReason());
				System.out.println(ab.getApplyUser());
				System.out.println(ab.getId());
				System.out.println(ab.getStatus());
			}

			session.commit();

			System.out.println("update =======");
			mapper.updateApplyBillById(new ApplyBill("111", "gsm", "{cc}", "dd", 9));
			session.commit();

			System.out.println("select by name=======");
			List<ApplyBill> abs1 = mapper.selectBillsByApplyUser("gsm");

			for (ApplyBill ab : abs1) {
				System.out.println(ab.getApplyDate());
				System.out.println(ab.getApplyQuota());
				System.out.println(ab.getApplyReason());
				System.out.println(ab.getApplyUser());
				System.out.println(ab.getId());
				System.out.println(ab.getStatus());
			}

			session.commit();

			System.out.println("update status=======");
			mapper.updateApplyBillStatusById("111", 99);
			session.commit();

			System.out.println("select all =======");
			List<ApplyBill> abs2 = mapper.selectAllBills();

			for (ApplyBill ab : abs2) {
				System.out.println(ab.getApplyDate());
				System.out.println(ab.getApplyQuota());
				System.out.println(ab.getApplyReason());
				System.out.println(ab.getApplyUser());
				System.out.println(ab.getId());
				System.out.println(ab.getStatus());
			}
			session.commit();

			System.out.println("delete =======");
			mapper.deleteApplyBillById("111");
			session.commit();

		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}

}
