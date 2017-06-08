package com.asiainfo.ocmanager.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.ServiceMapper;
import com.asiainfo.ocmanager.persistence.model.Service;
import com.asiainfo.ocmanager.persistence.test.DBConnectorFactory;

/**
 * 
 * @author zhaoyim
 *
 */
@Path("/service")
public class ServiceResource {

	/**
	 * Get All OCManager services
	 * 
	 * @return service list
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Service> getServices() {
		SqlSession session = DBConnectorFactory.getSession();
		List<Service> services = new ArrayList<Service>();
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);
			services = mapper.selectAllServices();
			System.out.println(services);

			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

		return services;
	}

	/**
	 * Get service by id
	 * 
	 * @return service
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Service getServiceById(@PathParam("id") String serviceId) {
		SqlSession session = DBConnectorFactory.getSession();
		Service service = null;
		try {
			ServiceMapper mapper = session.getMapper(ServiceMapper.class);
			service = mapper.selectServiceById(serviceId);
			System.out.println(service);

			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

		return service == null ? new Service() : service;
	}

}
