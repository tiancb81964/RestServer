package com.asiainfo.ocmanager.rest.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.bean.QuotaBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.utils.PeekerUtils;
import com.asiainfo.ocmanager.service.broker.ResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.GPResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.HDFSResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.HbaseResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.HiveResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.KafkaResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.MapRedResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.MongoResourcePeeker;
import com.asiainfo.ocmanager.service.broker.plugin.SparkResourcePeeker;
import com.asiainfo.ocmanager.service.broker.utils.ResourcePeekerFactory;

@Path("/quota")
public class QuotaResource {
	private static final Logger LOG = Logger.getLogger(QuotaResource.class);

	@GET
	@Path("hdfs")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getHdfsQuota(@Context HttpServletRequest request) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(HDFSResourcePeeker.class);
			String path = request.getParameter("path");
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(path)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("mapreduce/{queuename}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getYarnQuota(@PathParam("queuename") String queuename) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(MapRedResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(queuename)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("hbase/{namespace}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getHbaseQuota(@PathParam("namespace") String namespace) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(HbaseResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(namespace)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("kafka/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getKafkaQuota(@PathParam("serviceInstanceId") String instanceId) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(KafkaResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("greenplum/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getGpQuota(@PathParam("serviceInstanceId") String instanceId) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(GPResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("mongodb/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getMongoQuota(@PathParam("serviceInstanceId") String instanceId) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(MongoResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

	@GET
	@Path("spark/{serviceInstanceId}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getSparkQuota(@PathParam("serviceInstanceId") String instanceId) {
		try {
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(SparkResourcePeeker.class);
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(instanceId)));
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
			ResourcePeeker peeker = ResourcePeekerFactory.getPeeker(HiveResourcePeeker.class);
			String queuename = request.getParameter("queue");
			String path = "/apps/hive/warehouse/" + dbname + ".db";
			List<QuotaBean> response = PeekerUtils.transform(peeker.peekOn(Arrays.asList(queuename, path)));
			return Response.ok().entity(response).build();
		} catch (Exception e) {
			LOG.error("Error while fetching resource usage: ", e);
			throw new RuntimeException("Error while fetching resource usage: ", e);
		}
	}

}
