package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ServiceInstanceMapper;
import com.asiainfo.ocmanager.persistence.model.ServiceInstance;

public class TestServiceInstance {

	public static void main(String[] args) {
		
		
		SqlSession session = TestDBConnectorFactory.getSession();
		try {
			ServiceInstanceMapper mapper = session.getMapper(ServiceInstanceMapper.class);
//			List<ServiceInstance> sis2 = mapper.selectServiceInstanceByServiceType("777", "hdfs");
//			for (ServiceInstance si : sis2) {
//				System.out.println(si.getId());
//				System.out.println(si.getServiceTypeName());
//				System.out.println(si.getQuota());
//				
//				JsonObject serviceQuota = new JsonObject();
//				serviceQuota.addProperty("hdfs", si.getQuota());
//				
//				Map<String, String> mm = TenantQuotaUtils.getTenantQuotaByService("hdfs", serviceQuota.getAsString());
//				System.out.println(mm);
//		}
			
			mapper.insertServiceInstance(
					new ServiceInstance("9", "inst9", "t1", "300", "ETCD", "hdfs tenant 005 quota", "active"));
			mapper.deleteServiceInstance("2", "inst9");
			System.out.println("=== delete successfully ==");

			List<ServiceInstance> sis = mapper.selectServiceInstancesByTenant("2");

			for (ServiceInstance si : sis) {
				System.out.println(si.getId());
				System.out.println(si.getServiceTypeName());
			}

			List<ServiceInstance> sis1 = mapper.selectAllServiceInstances();

			for (ServiceInstance si : sis1) {
				System.out.println(si.getId());
				System.out.println(si.getServiceTypeName());
			}

			
			System.out.println("=== update quota successfully ==");
			mapper.updateInstanceQuota("zhaoyim", "hive-instance001", null);
			
			ServiceInstance si = mapper.selectServiceInstanceByCuzBsiName("aaaaaaa", "hive");
			System.out.println(si.getInstanceName());
			System.out.println(si.getId());
			System.out.println(si.getCuzBsiName());
			
			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}

}
