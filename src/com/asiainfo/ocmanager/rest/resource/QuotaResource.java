package com.asiainfo.ocmanager.rest.resource;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.bean.QuotaResponse;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.utils.PeekerUtils;
import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.utils.ResourcePeekerFactory;

@Path("/quota")
public class QuotaResource {
	private static final Logger LOG = Logger.getLogger(QuotaResource.class);

	@GET
	@Path("hdfs")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getHdfsQuota(@Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			String path = request.getParameter("path");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(path)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("mapreduce/{queuename}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getYarnQuota(@PathParam("queuename") String queuename, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(queuename)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("hbase/{namespace}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getHbaseQuota(@PathParam("namespace") String namespace, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(namespace)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("kafka/{topic}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getKafkaQuota(@PathParam("topic") String topic, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(topic)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("greenplum/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getGpQuota(@PathParam("serviceInstanceId") String instanceId, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("mongodb/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getMongoQuota(@PathParam("serviceInstanceId") String instanceId, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("spark/{queueName}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getSparkQuota(@PathParam("queueName") String queueName, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(queueName)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching spark resource usage: ", e);
			throw new RuntimeException("Error while fetching spark resource usage: ", e);
		}
	}

	@GET
	@Path("hive/{dbname}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getHiveQuota(@PathParam("dbname") String dbname, @Context HttpServletRequest request) {
		try {
			String service = request.getParameter("service");
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(service);
			String path = "/apps/hive/warehouse/" + dbname + ".db";
			QuotaResponse response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(path)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

}
