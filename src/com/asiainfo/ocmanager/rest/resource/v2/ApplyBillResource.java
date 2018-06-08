package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.ApplyBill;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.exception.bean.ResponseExceptionBean;
import com.asiainfo.ocmanager.rest.resource.persistence.ApplyBillPersistenceWrapper;

/**
 * 
 * @author zhaoyim
 *
 */

@Path("/v2/api/workflow/apply/bill")
public class ApplyBillResource {

	private static final Logger logger = LoggerFactory.getLogger(ApplyBillResource.class);

	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	// @Audit(action = Action.GET, targetType = TargetType.USERS)
	public Response getApplyBills() {
		try {
			List<ApplyBill> bills = ApplyBillPersistenceWrapper.getAllApplyBills();
			return Response.ok().entity(bills).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "getApplyBills");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}
	}

	@GET
	@Path("user/{userName}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	// @Audit(action = Action.GET, targetType = TargetType.USERS)
	public Response getApplyBillsByUser(@PathParam("userName") String userName) {
		try {
			List<ApplyBill> bills = ApplyBillPersistenceWrapper.getApplyBillsByUser(userName);
			return Response.ok().entity(bills).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "getApplyBillsByUser");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}
	}

	@POST
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	// @Audit(action = Action.CREATE, targetType = TargetType.USER)
	public Response createApplyBill(ApplyBill applyBill, @Context HttpServletRequest request) {
		try {
			ApplyBill bill = ApplyBillPersistenceWrapper.createApplyBill(applyBill);
			return Response.ok().entity(bill).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "createApplyBill");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}

	}

	@PUT
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	// @Audit(action = Action.CREATE, targetType = TargetType.USER)
	public Response updateApplyBill(ApplyBill applyBill, @Context HttpServletRequest request) {
		try {
			ApplyBill bill = ApplyBillPersistenceWrapper.updateApplyBill(applyBill);
			return Response.ok().entity(bill).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "updateApplyBill");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}

	}

	@PUT
	@Path("{id}/status/{status}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Consumes(MediaType.APPLICATION_JSON)
	// @Audit(action = Action.CREATE, targetType = TargetType.USER)
	public Response updateApplyBillStatus(@PathParam("id") String id, @PathParam("status") int status,
			@Context HttpServletRequest request) {
		try {
			ApplyBill bill = ApplyBillPersistenceWrapper.updateApplyBillStatus(id, status);
			return Response.ok().entity(bill).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "updateApplyBillStatus");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}

	}

	@DELETE
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	// @Audit(action = Action.DELETE, targetType = TargetType.USER)
	public Response deleteApplyBill(@PathParam("id") String id, @Context HttpServletRequest request) {
		try {
			ApplyBillPersistenceWrapper.deleteApplyBill(id);
			return Response.ok()
					.entity(new ResourceResponseBean("delete bill success", id, ResponseCodeConstant.SUCCESS)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("{0} : {1} hit exception -> ", "ApplyBillResource", "deleteApplyBill");
			ResponseExceptionBean ex = new ResponseExceptionBean();
			ex.setException(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(ex).build();
		}
	}

}
