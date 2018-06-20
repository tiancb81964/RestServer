package com.asiainfo.ocmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.service.broker.BrokerAdapterInterface;

/**
 * DF request template
 * 
 * @author Ethan
 *
 */
public class DFTemplate {
	private static final Logger LOG = LoggerFactory.getLogger(DFTemplate.class);
	private static final String BASE = DFTemplate.class.getResource("/").getPath();

	public static class Create_DC {
		private static String template = parseTemplate("create-dc.json");
		private static final String ID = "${id}";
		private static final String IMAGE_URL = "${image-url}";
		private static final String ENV_LIST = "\"${env_list}\"";

		public static String assembleString(BrokerAdapterInterface adapter) {
			String image = adapter.getImage();
			String str = template.replace(IMAGE_URL, image);
			str = str.replace(ID, adapter.getCluster().getCluster_name().toLowerCase());
			str = str.replace(ENV_LIST, envToJson(adapter.getEnv()));
			return str;
		}
	}

	public static class Create_SVC {
		private static String template = parseTemplate("create-svc.json");
		private static final String ID = "${id}";
		private static final String DC_NAME = "${cm-dc-name}";

		public static String assembleString(String dcName) {
			String str = template.replace(ID, dcName);
			str = str.replace(DC_NAME, dcName);
			return str;
		}
	}

	public static class Create_Router {
		private static String template = parseTemplate("create-router.json");
		private static final String ID = "${id}";
		private static final String SVC_NAME = "${cm-svc-name}";
		private static final String ROUTER_HOST = "${cm-router-prefix}";
		public static final String HOST_POSTFIX = ".cm.prd.dataos.io";

		public static String assembleString(String svcName) {
			String str = template.replace(ID, svcName);
			str = str.replace(SVC_NAME, svcName);
			str = str.replace(ROUTER_HOST, svcName + HOST_POSTFIX);
			return str;
		}
	}

	private static String envToJson(Map<String, String> env) {
		JsonArray array = new JsonArray();
		for (Entry<String, String> en : env.entrySet()) {
			if (en.getValue() == null) {
				LOG.warn("Environment kv value is null: " + en.toString());
				array.add(en.getKey(), "");
				continue;
			}
			array.add(en.getKey(), en.getValue());
		}
		return array.toString();
	}

	private static class JsonArray {
		private JsonArrayBuilder sb = Json.createArrayBuilder();

		public JsonArray add(String key, String value) {
			sb.add(Json.createObjectBuilder().add("name", key).add("value", value));
			return this;
		}

		@Override
		public String toString() {
			return sb.build().toString();
		}
	}
	
	private static String parseTemplate(String filename) {
		BufferedReader br = null;
		try {
			File file = new File(BASE + File.separator + ".." + File.separator + "conf" + File.separator + "templates"
					+ File.separator + filename);
			if (!file.exists()) {
				LOG.error("Template file not exist: " + file.getPath());
				throw new RuntimeException("Template file not exist: " + file.getPath());
			}
			FileReader reader = new FileReader(file);
			br = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			LOG.error("Exception while parsing file: " + filename, e);
			throw new RuntimeException("Exception while parsing file: " + filename, e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
