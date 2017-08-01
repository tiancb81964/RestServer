package com.asiainfo.ocmanager.rest.resource;

import java.util.ArrayList;
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

import com.asiainfo.ocmanager.auth.utils.CacheUtils;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.AdapterResponseBean;
import com.asiainfo.ocmanager.rest.bean.AssignmentInfoBean;
import com.asiainfo.ocmanager.rest.bean.UserRoleViewBean;
import com.asiainfo.ocmanager.rest.bean.UserWithTURBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceInstancePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.utils.TenantUtils;
import com.asiainfo.ocmanager.utils.TenantTree;
import com.asiainfo.ocmanager.utils.TenantTree.TenantTreeNode;
import com.asiainfo.ocmanager.utils.TenantTreeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 *
 * @author zhaoyim
 *
 */

@Path("/user")
public class UserResource {

	private static Logger logger = Logger.getLogger(TenantResource.class);

	/**
	 * Get All OCManager users with tenants
	 *
	 * @return user list
	 */
	@GET
	@Path("/with/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getUsersWithTenants() {
		try {
			List<User> users = UserPersistenceWrapper.getUsers();
			List<UserWithTURBean> usersWithTenants = new ArrayList<UserWithTURBean>();
			for (User u : users) {

				List<UserRoleViewBean> urvbs = new ArrayList<UserRoleViewBean>();
				List<UserRoleView> urvs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserId(u.getId());

				for (UserRoleView urv : urvs) {
					UserRoleViewBean urvb = new UserRoleViewBean(urv);
					String parentTenantName = UserResource.getParentTenantName(urv.getTenantId());
					urvb.setParentTenantName(parentTenantName);
					urvbs.add(urvb);
				}

				UserWithTURBean userBean = new UserWithTURBean(u);
				userBean.setUrv(urvbs);
				usersWithTenants.add(userBean);
			}

			return Response.ok().entity(usersWithTenants).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getUsersWithTenants -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the specific user with tenants by id
	 *
	 * @param userId
	 *            user id
	 * @return user
	 */
	@GET
	@Path("{id}/with/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getUserWithTenantsById(@PathParam("id") String userId) {
		try {
			User user = UserPersistenceWrapper.getUserById(userId);

			if (user == null) {
				return Response.status(Status.NOT_FOUND).entity("The user " + userId + "can not find.").build();
			}
			List<UserRoleViewBean> urvbs = new ArrayList<UserRoleViewBean>();
			List<UserRoleView> urvs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserId(userId);

			for (UserRoleView urv : urvs) {
				UserRoleViewBean urvb = new UserRoleViewBean(urv);
				String parentTenantName = UserResource.getParentTenantName(urv.getTenantId());
				urvb.setParentTenantName(parentTenantName);
				urvbs.add(urvb);
			}

			UserWithTURBean userBean = new UserWithTURBean(user);
			userBean.setUrv(urvbs);
			return Response.ok().entity(userBean).build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getUserWithTenantsById -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private static String getParentTenantName(String tenantId) {
		Tenant ct = TenantPersistenceWrapper.getTenantById(tenantId);
		if (ct != null) {
			Tenant pt = TenantPersistenceWrapper.getTenantById(ct.getParentId());
			return pt == null ? null : pt.getName();
		}
		return null;
	}

	/**
	 * Get All OCManager users
	 *
	 * @return user list
	 */
	@GET
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getUsers() {
		try {
			List<User> users = UserPersistenceWrapper.getUsers();
			return Response.ok().entity(users).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getUsers -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Get the specific user by id
	 *
	 * @param userId
	 *            user id
	 * @return user
	 */
	@GET
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getUserById(@PathParam("id") String userId) {
		try {
			User user = UserPersistenceWrapper.getUserById(userId);
			return Response.ok().entity(user == null ? new User() : user).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getUserById -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Create a new user
	 *
	 * @param user
	 *            user obj json
	 * @return new user info
	 */
	@POST
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user, @Context HttpServletRequest request) {
		try {
//			TODO: get createdUser from token in header
			String createdUser = request.getHeader("username");
			if (createdUser == null) {
				return Response.status(Status.NOT_FOUND)
						.entity("Can not get the login user, please make sure the login user is pass to adapter.")
						.build();
			}

			List<UserRoleView> urvs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserName(createdUser);
			boolean canCreateUser = false;
			for (UserRoleView urv : urvs) {
				if (Constant.canCreateUserList.contains(urv.getRoleName())) {
					canCreateUser = true;
					break;
				}
			}

			if (canCreateUser) {
				user.setCreatedUser(createdUser);
				user = UserPersistenceWrapper.createUser(user);
				return Response.ok().entity(user).build();
			} else {
				return Response.status(Status.BAD_REQUEST)
						.entity(new AdapterResponseBean("create failed",
								"The user " + createdUser + " can not add user, because it is team member role.", 4003))
						.build();
			}

		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("createUser -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Update the existing user info
	 *
	 * @param user
	 *            user obj json
	 * @return updated user info
	 */
	@PUT
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User user, @Context HttpServletRequest request) {
		try {
			String loginUser = request.getHeader("username");
			User updateUser = UserPersistenceWrapper.getUserById(user.getId());

			if (updateUser == null) {
				return Response.status(Status.NOT_FOUND).entity("The user " + user.getUsername() + "can not find.")
						.build();
			}

			if (updateUser.getCreatedUser().equals(loginUser)) {
				user = UserPersistenceWrapper.updateUser(user);
				return Response.ok().entity(user).build();
			} else {
				return Response.status(Status.BAD_REQUEST).entity(new AdapterResponseBean("update failed",
						"Can not update the user: " + updateUser.getUsername() + ", it is created by user: "
								+ updateUser.getCreatedUser() + ". please use the created user to update.",
						4004)).build();
			}

		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("updateUser -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 * Delete a user
	 *
	 * @param userId
	 *            user id
	 */
	@DELETE
	@Path("{id}")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response deleteUser(@PathParam("id") String userId, @Context HttpServletRequest request) {
		String userName = null;
		try {
			String loginUser = request.getHeader("username");
			User user = UserPersistenceWrapper.getUserById(userId);

			if (user == null) {
				return Response.status(Status.NOT_FOUND).entity("The user " + userId + "can not find.").build();
			}

			userName = user.getUsername();

			if (user.getCreatedUser().equals(loginUser)) {
				UserPersistenceWrapper.deleteUser(userId);
				CacheUtils.removeToken(userName);
			} else {
				return Response.status(Status.BAD_REQUEST)
						.entity(new AdapterResponseBean("delete failed",
								"Can not delete the user: " + user.getUsername() + ", it is created by user: "
										+ user.getCreatedUser() + ". please use the created user to delete.",
								4001))
						.build();
			}
			return Response.ok().entity(new AdapterResponseBean("delete success", userId, 200)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("deleteUser -> " + e.getMessage());

			if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {

				List<UserRoleView> urvs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserName(userName);

				String tenants = "";
				for (UserRoleView t : urvs) {
					tenants = tenants + t.getTenantName() + ",";
				}
				tenants = tenants.substring(0, tenants.length() - 1);
				return Response
						.status(Status.BAD_REQUEST).entity(
								new AdapterResponseBean("delete failed",
										"The user is assign with the tenants: [" + tenants
												+ "], please unassign the user, then try to delete it again.",
										4002))
						.build();
			} else {
				return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
			}
		}
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	@GET
	@Path("id/{id}/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantAndRoleById(@PathParam("id") String userId) {
		try {
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserId(userId);
			return Response.ok().entity(turs).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantAndRoleById -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	@GET
	@Path("name/{name}/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantAndRoleByName(@PathParam("name") String userName) {
		try {
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserName(userName);
			return Response.ok().entity(turs).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getTenantAndRoleByName -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	@GET
	@Path("id/{id}/all/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantsById(@PathParam("id") String userId) {
		try {
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserId(userId);

			List<Tenant> tenantList = new ArrayList<Tenant>();
			for (UserRoleView tur : turs) {
				TenantTree tree = TenantTreeUtil.constructTree(tur.getTenantId());
				List<TenantTreeNode> nodes = tree.listAllNodes();
				tenantList.addAll(TenantTreeUtil.transform(nodes));
			}

			return Response.ok().entity(tenantList).build();
		} catch (Exception e) {
			logger.error("Error while getting Tenants by user id: " + userId, e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	/**
	 *
	 * @param userName
	 * @return
	 */
	@GET
	@Path("name/{name}/all/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getTenantsByName(@PathParam("name") String userName) {
		try {
			List<Tenant> tenantList = new ArrayList<Tenant>();
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserName(userName);
			for (UserRoleView tur : turs) {
				TenantTree tree = TenantTreeUtil.constructTree(tur.getTenantId());
				List<TenantTreeNode> allNodes = tree.listAllNodes();
				tenantList.addAll(TenantTreeUtil.transform(allNodes));
			}
			return Response.ok().entity(tenantList).build();
		} catch (Exception e) {
			logger.error("Error while getting Tenants by user name: " + userName, e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	@GET
	@Path("id/{id}/tenant/{tenantId}/children/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getChildrenTenantsByUserIdTenantId(@PathParam("id") String userId,
			@PathParam("tenantId") String tenantId) {
		try {
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserId(userId);

			List<Tenant> tenantList = UserResource.getTenantsList(turs);

			ArrayList<String> tenantIdList = new ArrayList<String>();
			ArrayList<Tenant> tenants = new ArrayList<Tenant>();
			for (Tenant ten : tenantList) {
				if (!tenantIdList.contains(ten.getId())) {
					tenantIdList.add(ten.getId());
					tenants.add(ten);
				}
			}

			ArrayList<Tenant> children = new ArrayList<Tenant>();
			for (Tenant t : tenants) {
				if (t.getParentId() != null) {
					if (t.getParentId().equals(tenantId)) {
						children.add(t);
					}
				}
			}

			return Response.ok().entity(children).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getChildrenTenantsByUserIdTenantId -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	@GET
	@Path("name/{name}/tenant/{tenantId}/children/tenants")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getChildrenTenantsByUserNameTenantId(@PathParam("name") String userName,
			@PathParam("tenantId") String tenantId) {
		try {
			List<UserRoleView> turs = UserRoleViewPersistenceWrapper.getTenantAndRoleBasedOnUserName(userName);

			List<Tenant> tenantList = UserResource.getTenantsList(turs);

			ArrayList<String> tenantIdList = new ArrayList<String>();
			ArrayList<Tenant> tenants = new ArrayList<Tenant>();
			for (Tenant ten : tenantList) {
				if (!tenantIdList.contains(ten.getId())) {
					tenantIdList.add(ten.getId());
					tenants.add(ten);
				}
			}

			ArrayList<Tenant> children = new ArrayList<Tenant>();
			for (Tenant t : tenants) {
				if (t.getParentId() != null) {
					if (t.getParentId().equals(tenantId)) {
						children.add(t);
					}
				}
			}

			return Response.ok().entity(children).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getChildrenTenantsByUserNameTenantId -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private static List<Tenant> getTenantsList(List<UserRoleView> turs) {
		List<Tenant> tenantList = new ArrayList<Tenant>();
		for (UserRoleView tur : turs) {
			TenantTree tree = TenantTreeUtil.constructTree(tur.getTenantId());
			List<TenantTreeNode> allNodes = tree.listAllNodes();
			tenantList.addAll(TenantTreeUtil.transform(allNodes));
		}
		return tenantList;
	}

	@GET
	@Path("name/{userName}/tenant/{tenantId}/assignments/info")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	public Response getAssignmentsInfoForUser(@PathParam("userName") String userName,
			@PathParam("tenantId") String tenantId) {
		try {
			List<AssignmentInfoBean> assignmentInfoBeans = new ArrayList<AssignmentInfoBean>();
			List<ServiceInstance> instaces = ServiceInstancePersistenceWrapper.getServiceInstancesInTenant(tenantId);

			for (ServiceInstance instace : instaces) {
				String instanceStr = TenantUtils.getTenantServiceInstancesFromDf(tenantId,
						instace.getInstanceName());
				JsonElement instanceJE = new JsonParser().parse(instanceStr);
				JsonObject instanceJson = instanceJE.getAsJsonObject();
				JsonObject spec = instanceJson.getAsJsonObject("spec");
				String phase = instanceJson.getAsJsonObject("status").get("phase").getAsString();
				JsonElement binding = spec.get("binding");
				String serviceName = spec.getAsJsonObject("provisioning").get("backingservice_name").getAsString();

				JsonElement action = instanceJson.getAsJsonObject("status").get("action");
				JsonElement patch = instanceJson.getAsJsonObject("status").get("patch");

				if (!phase.equals(Constant.FAILURE)) {
					if (Constant.list.contains(serviceName.toLowerCase())) {
						if (!binding.isJsonNull()) {
							JsonArray bindingArray = spec.getAsJsonArray("binding");
							List<String> bindingedUserNames = new ArrayList<String>();
							for (JsonElement je : bindingArray) {
								String bindingUserName = je.getAsJsonObject().get("bind_hadoop_user").getAsString();
								bindingedUserNames.add(bindingUserName);
							}
							if (bindingedUserNames.contains(userName)) {
								AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
										"Authorization Success");
								assignmentInfoBeans.add(AIB);
							} else {
								if (action == null && patch == null) {
									AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
											"Failure OR Not Begin");
									assignmentInfoBeans.add(AIB);
								} else {
									if (patch != null) {
										if (patch.getAsString().equals(Constant.FAILURE)) {
											AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
													"Failure OR Not Begin");
											assignmentInfoBeans.add(AIB);
										} else {
											AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
													"Authorization Running");
											assignmentInfoBeans.add(AIB);
										}
									} else {
										if (action.getAsString().equals(Constant._TOBIND)
												|| action.getAsString().equals(Constant._TOUNBIND)) {
											AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
													"Authorization Running");
											assignmentInfoBeans.add(AIB);
										} else {
											AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
													"Failure OR Not Begin");
											assignmentInfoBeans.add(AIB);
										}
									}
								}
							}
						} else {
							if (action == null && patch == null) {
								AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
										"Failure OR Not Begin");
								assignmentInfoBeans.add(AIB);
							} else {
								if (patch != null) {
									if (patch.getAsString().equals(Constant.FAILURE)) {
										AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
												"Failure OR Not Begin");
										assignmentInfoBeans.add(AIB);
									} else {
										AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
												"Authorization Running");
										assignmentInfoBeans.add(AIB);
									}
								} else {
									if (action.getAsString().equals(Constant._TOBIND)
											|| action.getAsString().equals(Constant._TOUNBIND)) {
										AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
												"Authorization Running");
										assignmentInfoBeans.add(AIB);
									} else {
										AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(),
												"Failure OR Not Begin");
										assignmentInfoBeans.add(AIB);
									}
								}
							}
						}
					} else {
						AssignmentInfoBean AIB = new AssignmentInfoBean(instace.getInstanceName(), "Success");
						assignmentInfoBeans.add(AIB);
					}
				}
			}

			return Response.ok().entity(assignmentInfoBeans).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.info("getAssignmentsInfoForUser -> " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

}
