package com.asiainfo.ocmanager.persistence.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ServiceMapper;
import com.asiainfo.ocmanager.persistence.model.Service;

public class TestService {

	public static void main(String[] args) {
		SqlSession session = TestDBConnectorFactory.getSession();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);
			mapper.insertService(new Service("700", "hdfs17", "hdfs description", "ocdp", "hdfs"));
			session.commit();
			List<Service> services = mapper.selectAllServices();
			System.out.println("=== All services ===");
			for (Service s : services) {
				System.out.println(s.getId());
				System.out.println(s.getServicename());
				System.out.println(s.getDescription());
			}
			session.commit();
			System.out.println("=== Service by id ===");
			Service service = mapper.selectServiceById("100");
			System.out.println(service.getId());
			System.out.println(service.getServicename());
			System.out.println(service.getDescription());

			session.commit();

			List<String> originList = new ArrayList<String>();
			originList.add("cluster2");
			originList.add("cluster61");
			List<Service> services1 = mapper.selectServicesByOrigin(originList);
			for (Service s : services1) {
				System.out.println(s.getId());
				System.out.println(s.getServicename());
				System.out.println(s.getDescription());
			}

			List<Service> services2 = mapper
					.selectServicesByServiceType(Arrays.asList("hdfs", "hbase", "hive", "mapreduce", "spark", "kafka"));
			for (Service s : services2) {
				System.out.println(s.getId());
				System.out.println(s.getServicename());
				System.out.println(s.getDescription());
			}

		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}

}
