package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.bean.ClusterBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author zhaoyim
 *
 */

@Path("/v2/api/clusters")
public class ClusterResource {

	private static Logger logger = Logger.getLogger(ClusterResource.class);

	/**
	 * Get All OCManager users with tenants
	 *
	 * @return user list
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	public Response getClusters() {
		try {
			//TODO:
			List<ClusterBean> clusters = Lists.newArrayList();
			Map<String, String> map = Maps.newHashMap();
			map.put("ENV_ALPHA", "VALUE");
			map.put("ENV_BETA", "VALUE");
			clusters.add(new ClusterBean("", "cluster_alpha", "hadoop", "http://10.1.236.111:8080", "admin", map));
			return Response.ok().entity(clusters).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getClusters hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}
}
