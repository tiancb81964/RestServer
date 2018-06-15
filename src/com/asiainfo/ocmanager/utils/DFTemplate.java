package com.asiainfo.ocmanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
		private static final String ID_POSTFIX = "${id}";
		private static final String IMAGE_URL = "${image-url}";
		private static final String ENV_LIST = "\"${env_list}\"";

		public static String assembleString(BrokerAdapterInterface adapter) {
			String image = adapter.getImage();
			String str = template.replace(IMAGE_URL, image);
			str = str.replace(ID_POSTFIX, adapter.getCluster().getCluster_name() + UUID.randomUUID().toString());
			str = str.replace(ENV_LIST, envToJson(adapter.getEnv()));
			return str;
		}
	}

	public static class Create_SVC {
		private static String template = parseTemplate("create-svc.json");
		private static final String ID_POSTFIX = "${id}";
		private static final String DC_NAME = "${cm-dc-name}";

		public static String assembleString(String dcName) {
			String str = template.replace(ID_POSTFIX, UUID.randomUUID().toString());
			str = str.replace(DC_NAME, dcName);
			return str;
		}
	}

	public static class Create_Router {
		private static String template = parseTemplate("create-router.json");
		private static final String ID_POSTFIX = "${id}";
		private static final String SVC_NAME = "${cm-svc-name}";
		private static final String URI_PREFIX = "${cm-router-prefix}";

		public static String assembleString(String svcName) {
			String str = template.replace(ID_POSTFIX, UUID.randomUUID().toString());
			str = str.replace(SVC_NAME, svcName);
			str = str.replace(URI_PREFIX, UUID.randomUUID().toString());
			return str;
		}
	}

	private static String envToJson(Map<String, String> env) {
		JsonArray array = new JsonArray();
		for (Entry<String, String> en : env.entrySet()) {
			array.add(en.getKey(), en.getValue());
		}
		return array.toString();
	}

	private static class JsonArray {
		private JsonArrayBuilder sb = Json.createArrayBuilder();

		public JsonArray add(String key, String value) {
			sb.add(Json.createObjectBuilder().add(key, value));
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
