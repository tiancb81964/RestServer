package com.asiainfo.ocmanager.rest.resource.v2;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.audit.Audit;
import com.asiainfo.ocmanager.audit.Audit.Action;
import com.asiainfo.ocmanager.audit.Audit.TargetType;
import com.asiainfo.ocmanager.auth.utils.TokenPaserUtils;
import com.asiainfo.ocmanager.persistence.model.Broker;
import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.persistence.model.UserRoleView;
import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.rest.resource.persistence.ClusterPersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.UserRoleViewPersistenceWrapper;
import com.asiainfo.ocmanager.rest.utils.DataFoundryConfiguration;
import com.asiainfo.ocmanager.rest.utils.SSLSocketIgnoreCA;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;
import com.asiainfo.ocmanager.service.broker.utils.BrokerUtils;
import com.asiainfo.ocmanager.utils.DFTemplate;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author zhaoyim
 *
 */
@Path("/v2/api/brokers")
public class BrokerResource {

	private static Logger logger = LoggerFactory.getLogger(BrokerResource.class);

	@POST
	@Path("/dc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.CREATE, targetType = TargetType.BROKER_DC)
	public Response createBrokerDC(@Context HttpServletRequest request) {
		String clustername = request.getParameter("clustername");
		Preconditions.checkArgument(Strings.isNullOrEmpty(clustername));
		try {
			Cluster cluster = ClusterPersistenceWrapper.getClusterByName(clustername);
			BrokerInterface adapter = BrokerUtils.getAdapter(cluster);
			String dcreq = DFTemplate.Create_DC.assembleString(adapter);
			String rsp = createdc(dcreq);
			// TODP: insert broker info into CM_BROKERS table
			return Response.ok().entity(rsp).tag(clustername).build();
		} catch (Exception e) {
			logger.error("createBrokerDC hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(clustername).build();
		}
	}

	@POST
	@Path("/svc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.CREATE, targetType = TargetType.BROKER_SVC)
	public Response createBrokerSVC(@Context HttpServletRequest request) {
		String dcName = request.getParameter("dcname");
		Preconditions.checkArgument(Strings.isNullOrEmpty(dcName));
		try {
			String svcreq = DFTemplate.Create_SVC.assembleString(dcName);
			String rsp = createsvc(svcreq);
			// TODP: insert broker info into CM_BROKERS table
			return Response.ok().entity(getSVCConfig()).tag(dcName).build();
		} catch (Exception e) {
			logger.error("createBrokerSVC hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(dcName).build();
		}
	}

	private String getSVCConfig() {
		return "{     \"apiVersion\": \"v1\",     \"kind\": \"Service\",     \"metadata\": {         \"annotations\": {             \"dadafoundry.io\\/create-by\": \"clustermanager\"         },         \"creationTimestamp\": \"2018-06-05T02:33:13Z\",         \"labels\": {             \"app\": \"cm-broker\"         },         \"name\": \"cm-broker\",         \"namespace\": \"southbase\",         \"resourceVersion\": \"9194792\",         \"selfLink\": \"\\/api\\/v1\\/namespaces\\/southbase\\/services\\/cm-broker\",         \"uid\": \"cb7f1b68-6868-11e8-ae4e-fa163ef134de\"     },     \"spec\": {         \"clusterIP\": \"172.25.247.231\",         \"ports\": [             {                 \"name\": \"9000-tcp\",                 \"port\": 9000,                 \"protocol\": \"TCP\",                 \"targetPort\": 9000             }         ],         \"selector\": {             \"app\": \"cm-broker\",             \"deploymentconfig\": \"cm-broker\"         },         \"sessionAffinity\": \"None\",         \"type\": \"ClusterIP\"     },     \"status\": {         \"loadBalancer\": {}     } } ";
	}

	@POST
	@Path("/router")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.CREATE, targetType = TargetType.BROKER_ROUTER)
	public Response createBrokerRouter(@Context HttpServletRequest request) {
		String svcname = request.getParameter("svcname");
		Preconditions.checkArgument(Strings.isNullOrEmpty(svcname));
		try {
			String svcreq = DFTemplate.Create_Router.assembleString(svcname);
			String rsp = createrouter(svcreq);
			// TODP: insert broker info into CM_BROKERS table
			return Response.ok().entity(getRouterConfig()).tag(svcname).build();
		} catch (Exception e) {
			logger.error("createBrokerRouter hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(svcname).build();
		}
	}

	private String getRouterConfig() {
		return "{     \"apiVersion\": \"v1\",     \"kind\": \"Route\",     \"metadata\": {         \"creationTimestamp\": \"2018-06-05T02:35:50Z\",         \"name\": \"cm-broker\",         \"namespace\": \"southbase\",         \"resourceVersion\": \"9195063\",         \"selfLink\": \"\\/oapi\\/v1\\/namespaces\\/southbase\\/routes\\/cm-broker\",         \"uid\": \"28f36555-6869-11e8-ae4e-fa163ef134de\"     },     \"spec\": {         \"host\": \"cm.southbase.prd.dataos.io\",         \"port\": {             \"targetPort\": \"9000-tcp\"         },         \"tls\": {             \"insecureEdgeTerminationPolicy\": \"Redirect\",             \"termination\": \"edge\"         },         \"to\": {             \"kind\": \"Service\",             \"name\": \"cm-broker\",             \"weight\": 50         },         \"wildcardPolicy\": \"None\"     },     \"status\": {         \"ingress\": [             {                 \"conditions\": [                     {                         \"lastTransitionTime\": \"2018-06-05T02:35:50Z\",                         \"status\": \"True\",                         \"type\": \"Admitted\"                     }                 ],                 \"host\": \"cm.southbase.prd.dataos.io\",                 \"routerName\": \"router\",                 \"wildcardPolicy\": \"None\"             }         ]     } } ";
	}

	private String createsvc(String svcreq) {
		// TODO Auto-generated method stub
		return "svc_name_987654321";
	}

	private String createdc(String dcreq) {
		// TODO Auto-generated method stub
		return "dc_name_123456789";
	}

	private String createrouter(String reqBody) {
		// TODO Auto-generated method stub
		return "rsp";
	}

	/**
	 * get a broker dc
	 * 
	 * @param request
	 * @return
	 */
	@GET
	@Path("/{id}/dc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.BROKER_DC)
	public Response getBrokerDC(@PathParam("id") String brokerid) {
		// TODO:
		return Response.ok().entity(getDCConfig()).build();
	}

	private String getDCConfig() {
		return "{     \"apiVersion\": \"v1\",     \"kind\": \"DeploymentConfig\",     \"metadata\": {         \"annotations\": {             \"dadafoundry.io\\/create-by\": \"chaizs\",             \"openshift.io\\/generated-by\": \"OpenShiftWebConsole\"         },         \"creationTimestamp\": \"2018-06-05T02:33:13Z\",         \"generation\": 3,         \"labels\": {             \"app\": \"cm-console\"         },         \"name\": \"cm-console\",         \"namespace\": \"southbase\",         \"resourceVersion\": \"9200111\",         \"selfLink\": \"\\/oapi\\/v1\\/namespaces\\/southbase\\/deploymentconfigs\\/cm-console\",         \"uid\": \"cb8ea774-6868-11e8-ae4e-fa163ef134de\"     },     \"spec\": {         \"replicas\": 1,         \"selector\": {             \"app\": \"cm-console\",             \"deploymentconfig\": \"cm-console\"         },         \"strategy\": {             \"activeDeadlineSeconds\": 21600,             \"resources\": {},             \"rollingParams\": {                 \"intervalSeconds\": 1,                 \"maxSurge\": \"25%\",                 \"maxUnavailable\": \"25%\",                 \"timeoutSeconds\": 600,                 \"updatePeriodSeconds\": 1             },             \"type\": \"Rolling\"         },         \"template\": {             \"metadata\": {                 \"annotations\": {                     \"openshift.io\\/generated-by\": \"OpenShiftWebConsole\"                 },                 \"creationTimestamp\": null,                 \"labels\": {                     \"app\": \"cm-console\",                     \"deploymentconfig\": \"cm-console\"                 }             },             \"spec\": {                 \"containers\": [                     {                         \"env\": [                             {                                 \"name\": \"ADAPTER_API_SERVER\",                                 \"value\": \"http:\\/\\/10.1.236.60:9090\"                             },                             {                                 \"name\": \"SVCAMOUNT_API_SERVER\",                                 \"value\": \"http:\\/\\/svc-amount2.cloud.prd.asiainfo.com\"                             }                         ],                         \"image\": \"docker-registry.default.svc:5000\\/southbase\\/cm-console@sha256:8f0b437a91bed1ab44cfdda6b989debc078dfba8a2013ef38e5a824dff42afd7\",                         \"imagePullPolicy\": \"IfNotPresent\",                         \"name\": \"cm-console\",                         \"ports\": [                             {                                 \"containerPort\": 9000,                                 \"protocol\": \"TCP\"                             }                         ],                         \"resources\": {},                         \"terminationMessagePath\": \"\\/dev\\/termination-log\",                         \"terminationMessagePolicy\": \"File\"                     }                 ],                 \"dnsPolicy\": \"ClusterFirst\",                 \"restartPolicy\": \"Always\",                 \"schedulerName\": \"default-scheduler\",                 \"securityContext\": {},                 \"terminationGracePeriodSeconds\": 30             }         },         \"test\": false,         \"triggers\": [             {                 \"type\": \"ConfigChange\"             }         ]     },     \"status\": {         \"availableReplicas\": 1,         \"conditions\": [             {                 \"lastTransitionTime\": \"2018-06-05T02:34:59Z\",                 \"lastUpdateTime\": \"2018-06-05T02:34:59Z\",                 \"message\": \"Deployment config has minimum availability.\",                 \"status\": \"True\",                 \"type\": \"Available\"             },             {                 \"lastTransitionTime\": \"2018-06-05T03:28:27Z\",                 \"lastUpdateTime\": \"2018-06-05T03:28:29Z\",                 \"message\": \"replication controller \'cm-console-2\' successfully rolled out\",                 \"reason\": \"NewReplicationControllerAvailable\",                 \"status\": \"True\",                 \"type\": \"Progressing\"             }         ],         \"details\": {             \"causes\": [                 {                     \"type\": \"ConfigChange\"                 }             ],             \"message\": \"config change\"         },         \"latestVersion\": 2,         \"observedGeneration\": 3,         \"readyReplicas\": 1,         \"replicas\": 1,         \"unavailableReplicas\": 0,         \"updatedReplicas\": 1     } } ";
	}

	/**
	 * update a broker
	 * 
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/{id}/dc")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.UPDATE, targetType = TargetType.BROKER_DC)
	public Response updateBrokerDC(@Context HttpServletRequest request) {
		// TODO:
		return Response.ok().entity(getDCConfig()).build();
	}

	@GET
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.GET, targetType = TargetType.BROKER)
	public Response getBrokers(@Context HttpServletRequest request) {
		String dcName = request.getParameter("clustername");
		// TODO:
		List<Broker> list = Lists.newArrayList();
		list.add(new Broker("1", "broker1", "http://myimage.com", "https://mybroker.com", "cluster1",
				"cm-broker-123456"));
		return Response.ok().entity(list).build();
	}

	/**
	 * instantiate a broker
	 * 
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/{id}/dc/instantiate")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.INSTANTIATE, targetType = TargetType.BROKER_DC)
	public Response instantiateBrokerDC(@Context HttpServletRequest request) {
		// TODO:
		return Response.ok().entity(getDCConfig()).build();
	}

	/**
	 * Register service broker
	 *
	 * @return service
	 */
	@PUT
	@Path("/register")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.REGISTER, targetType = TargetType.BROKER)
	public Response registerServiceBroker(String reqBodyStr, @Context HttpServletRequest request) {
		String brokerIP = extractIP(reqBodyStr);
		try {

			String adToken = request.getHeader("token");
			if (adToken == null || adToken.isEmpty()) {
				return Response.status(Status.NOT_FOUND)
						.entity(new ResourceResponseBean("add service broker failed",
								"token is null or empty, please check the token in request header.",
								ResponseCodeConstant.EMPTY_TOKEN))
						.tag(brokerIP).build();
			}

			String loginUser = TokenPaserUtils.paserUserName(adToken);
			logger.debug("addServiceBroker -> add service broker with login user: " + loginUser);

			UserRoleView role = UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser,
					Constant.ROOTTENANTID);

			if (role == null || !(role.getRoleName().equals(Constant.SYSADMIN))) {
				return Response.status(Status.UNAUTHORIZED)
						.entity(new ResourceResponseBean("add service broker failed",
								"the user is not system admin role, does NOT have the add service broker permission.",
								ResponseCodeConstant.NO_ADD_SERVICE_BROKER_PERMISSION))
						.tag(brokerIP).build();
			}

			String url = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
			String token = DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
			String dfRestUrl = url + "/oapi/v1/servicebrokers";

			// parse the req body make sure it is json
			JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);
			SSLConnectionSocketFactory sslsf = SSLSocketIgnoreCA.createSSLSocketFactory();

			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpPost = new HttpPost(dfRestUrl);
				httpPost.addHeader("Content-type", "application/json");
				httpPost.addHeader("Authorization", "bearer " + token);
				StringEntity se = new StringEntity(reqBodyJson.toString());
				se.setContentType("application/json");
				se.setContentEncoding("utf-8");
				httpPost.setEntity(se);

				CloseableHttpResponse response2 = httpclient.execute(httpPost);

				try {
					// int statusCode =
					// response2.getStatusLine().getStatusCode();
					String bodyStr = EntityUtils.toString(response2.getEntity());

					return Response.ok().entity(bodyStr).tag(brokerIP).build();
				} finally {
					response2.close();
				}
			} finally {
				httpclient.close();
			}
			// TODO: update binded_cluster colume of CM_BROKERS table
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("addServiceBroker hit exception-> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(brokerIP).build();
		}
	}

	private String extractIP(String reqBodyStr) {
		JsonElement reqBodyJson = new JsonParser().parse(reqBodyStr);
		JsonObject spec = reqBodyJson.getAsJsonObject().getAsJsonObject("spec");
		JsonObject url = spec.getAsJsonObject("url");
		return url.getAsString();
	}

	/**
	 * delete service broker
	 *
	 * @return service
	 */
	@DELETE
	@Path("/broker/{name}")
	@Produces((MediaType.APPLICATION_JSON + Constant.SEMICOLON + Constant.CHARSET_EQUAL_UTF_8))
	@Audit(action = Action.DELETE, targetType = TargetType.BROKER)
	public Response deleteServiceBroker(@PathParam("name") String serviceBrokerName,
			@Context HttpServletRequest request) {
		logger.error("delete not support in current version");
		return Response.status(Status.BAD_REQUEST).entity("Delete not support in current version")
				.tag(serviceBrokerName).build();
		// try {
		//
		// String adToken = request.getHeader("token");
		// if (adToken == null || adToken.isEmpty()) {
		// return Response.status(Status.NOT_FOUND)
		// .entity(new ResourceResponseBean("delete service broker failed",
		// "token is null or empty, please check the token in request header.",
		// ResponseCodeConstant.EMPTY_TOKEN))
		// .tag(serviceBrokerName).build();
		// }
		//
		// String loginUser = TokenPaserUtils.paserUserName(adToken);
		// logger.debug("deleteServiceBroker -> delete service broker with login user: "
		// + loginUser);
		//
		// UserRoleView role =
		// UserRoleViewPersistenceWrapper.getRoleBasedOnUserAndTenant(loginUser,
		// Constant.ROOTTENANTID);
		//
		// if (role == null || !(role.getRoleName().equals(Constant.SYSADMIN))) {
		// return Response.status(Status.UNAUTHORIZED)
		// .entity(new ResourceResponseBean("delete service broker failed",
		// "the user is not system admin role, does NOT have the add service broker
		// permission.",
		// ResponseCodeConstant.NO_DELETE_SERVICE_BROKER_PERMISSION))
		// .tag(serviceBrokerName).build();
		// }
		//
		// String url =
		// DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_URL);
		// String token =
		// DataFoundryConfiguration.getDFProperties().get(Constant.DATAFOUNDRY_TOKEN);
		// String dfRestUrl = url + "/oapi/v1/servicebrokers/" + serviceBrokerName;
		//
		// SSLConnectionSocketFactory sslsf =
		// SSLSocketIgnoreCA.createSSLSocketFactory();
		//
		// CloseableHttpClient httpclient =
		// HttpClients.custom().setSSLSocketFactory(sslsf).build();
		// try {
		// HttpDelete httpDelete = new HttpDelete(dfRestUrl);
		// httpDelete.addHeader("Content-type", "application/json");
		// httpDelete.addHeader("Authorization", "bearer " + token);
		//
		// CloseableHttpResponse response1 = httpclient.execute(httpDelete);
		//
		// try {
		// // int statusCode =
		// // response1.getStatusLine().getStatusCode();
		//
		// String bodyStr = EntityUtils.toString(response1.getEntity());
		//
		// return Response.ok().entity(bodyStr).tag(serviceBrokerName).build();
		// } finally {
		// response1.close();
		// }
		// } finally {
		// httpclient.close();
		// }
		// } catch (Exception e) {
		// // system out the exception into the console log
		// logger.error("deleteServiceBroker hit exception -> ", e);
		// return
		// Response.status(Status.BAD_REQUEST).entity(e.toString()).tag(serviceBrokerName).build();
		// }
	}

}
