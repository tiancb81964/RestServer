package com.asiainfo.ocmanager.rest.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.Dashboard;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.resource.persistence.DashboardPersistenceWrapper;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/dashboard")
public class DashboardResource {

	private static Logger logger = Logger.getLogger(TenantResource.class);

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("link")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getAllDashboardLinks() {
		try {
			List<Dashboard> dashboard = DashboardPersistenceWrapper.getAllLinks();

			return Response.ok().entity(dashboard).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getAllDashboardLinks -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GET
	@Path("link/{name}")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getDashboardLink(@PathParam("name") String name) {
		try {
			Dashboard dashboard = DashboardPersistenceWrapper.getLinkByName(name);

			return Response.ok().entity(dashboard).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getDashboardLink -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * 
	 * @param dashboard
	 * @return
	 */
	@POST
	@Path("link")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDashboardLink(Dashboard dashboard) {
		try {
			DashboardPersistenceWrapper.addLink(dashboard);

			return Response.ok().entity(new ResourceResponseBean("successful", "Add successfully", 200)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("addDashboardLink -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * 
	 * @param dashboard
	 * @return
	 */
	@PUT
	@Path("link/{id}")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDashboardLink(Dashboard dashboard, @PathParam("id") int id) {
		try {
			dashboard.setId(id);
			DashboardPersistenceWrapper.updateLink(dashboard);

			return Response.ok().entity(new ResourceResponseBean("successful", "Update successfully", 200)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("updateDashboardLink -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("link/{id}")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteDashboardLink(@PathParam("id") int id) {
		try {
			DashboardPersistenceWrapper.deleteLink(id);

			return Response.ok().entity(new ResourceResponseBean("successful", "Delete successfully", 200)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("deleteDashboardLink -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

}
