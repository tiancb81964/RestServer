package com.asiainfo.ocmanager.rest.resource;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.directory.server.kerberos.shared.keytab.Keytab;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.bean.ResourceResponseBean;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.constant.ResponseCodeConstant;
import com.asiainfo.ocmanager.service.client.krbClient;
import com.asiainfo.ocmanager.service.exception.KerberosOperationException;
import com.asiainfo.ocmanager.utils.KerberosConfiguration;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/kerberos")
public class KerberosResource {

	private static Logger logger = Logger.getLogger(KerberosResource.class);

	@GET
	@Path("keytab/{userName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getKeyTabFile(@PathParam("userName") String userName) {
		try {

			String keytabFilePath = this.getClass().getResource("/").getPath() + ".." + File.separator + "keytabs"
					+ File.separator + userName + ".keytab";

			File file = new File(keytabFilePath);

			if (!file.exists()) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new ResourceResponseBean("download keytab failed!",
								userName + ".keytab is NOT exist, please generate the keytab by call"
										+ " REST API: /ocmanager/v1/api/kerberos/keytab ",
								ResponseCodeConstant.CAN_NOT_FIND_KRB_KEYTAB_FILE))
						.build();
			}

			return Response.ok(file).header("Content-disposition", "attachment;filename=" + userName + ".keytab")
					.header("Cache-Control", "no-cache").build();

		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("getKeyTabFile hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	@POST
	@Path("create/keytab")
	@Produces((MediaType.APPLICATION_JSON + ";charset=utf-8"))
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createKeyTabFile(String requestBody) {
		try {
			JsonElement req = new JsonParser().parse(requestBody);
			JsonObject obj = req.getAsJsonObject();

			JsonElement krbusernameJE = obj.get("krbusername");
			JsonElement krbpasswordJE = obj.get("krbpassword");

			if (krbusernameJE == null || krbpasswordJE == null) {
				return Response.status(Status.BAD_REQUEST)
						.entity(new ResourceResponseBean("generate keytab failed",
								"can NOT find krbusername or krbpassword, please make sure you "
										+ "input the krbusername and krbpassword in the request body correctly.",
								ResponseCodeConstant.CAN_NOT_FIND_KRB_USERNAME_OR_PASSWORD))
						.build();
			}

			String krbusername = krbusernameJE.getAsString();
			String krbpassword = krbpasswordJE.getAsString();

			this.createKeyTabFile(krbusername, krbpassword);

			return Response.ok().entity(new ResourceResponseBean("generate keytab successfully!",
					krbusername + ".keytab created", ResponseCodeConstant.SUCCESS)).build();
		} catch (Exception e) {
			// system out the exception into the console log
			logger.error("createKeyTabFile hit exception -> ", e);
			return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
		}
	}

	private void createKeyTabFile(String krbusername, String krbpassword) throws KerberosOperationException {
		krbClient client = krbClient.getInstance();
		String principalName = krbusername + "@" + KerberosConfiguration.getConf().getProperty(Constant.KERBEROS_REALM);

		if (client.principalExists(principalName)) {
			client.removePrincipal(principalName);
			client.createPrincipal(principalName, krbpassword);
		} else {
			client.createPrincipal(principalName, krbpassword);
		}

		String keytabFilePath = this.getClass().getResource("/").getPath() + ".." + File.separator + "keytabs"
				+ File.separator + krbusername + ".keytab";
		Keytab keyTab = client.createKeyTab(principalName, krbpassword, null);

		client.createKeyTabFile(keyTab, keytabFilePath);
	}
}
