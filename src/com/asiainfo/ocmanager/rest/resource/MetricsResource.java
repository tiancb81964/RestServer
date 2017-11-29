package com.asiainfo.ocmanager.rest.resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.google.common.base.Joiner;

@Path("/metrics")
public class MetricsResource {
	private static final Logger LOG = LoggerFactory.getLogger(MetricsResource.class);

	@GET
	@Path("kafka/serviceName")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response kafkaServiceName() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String name = ServerConfiguration.getConf().getProperty(Constant.KAFKA_SERVICENAME).trim();
			map.put(Constant.KAFKA_SERVICENAME, name);
			return Response.ok().entity(map).build();
		} catch (Exception e) {
			LOG.error("kafkaServiceName hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
	
	@GET
	@Path("resourcemanager/addresses")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getResourceManager() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String p = ServerConfiguration.getConf().getProperty(Constant.RM_HTTP).trim();
			List<String> list = Arrays.asList(p.split(","));
			list.replaceAll(rm -> rm.substring(7));
			map.put("RM_ADDR", Joiner.on(",").join(list));
			return Response.ok().entity(map).build();
		} catch (Exception e) {
			LOG.error("kafkaServiceName hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
	
	@GET
	@Path("hdp/version")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response hdpVersions() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String version = ServerConfiguration.getConf().getProperty(Constant.HDP_VERSION).trim();
			map.put("HDP_VERSION", version);
			return Response.ok().entity(map).build();
		} catch (Exception e) {
			LOG.error("Get HDP_VERSION hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
	
}
