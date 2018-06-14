package com.asiainfo.ocmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.ServiceResource;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Service catalog, including all available services and details of those
 * services.
 * 
 * @author EthanWang
 *
 */
public class Catalog {
	private static final Logger LOG = LoggerFactory.getLogger(Catalog.class);
	private static Catalog instance;
	private JsonObject obj;
	// Map<serviceName, serviceObject>
	private Map<String, JsonObject> services = Maps.newHashMap();

	public static Catalog getInstance() {
		if (instance == null) {
			synchronized (Catalog.class) {
				if (instance == null) {
					instance = new Catalog();
				}
			}
		}
		return instance;
	}

	public List<String> getServiceQuotaKeys(String servicename) {
		if (!services.containsKey(servicename.toLowerCase())) {
			LOG.error("Service not found in catalog: " + servicename.toLowerCase());
			throw new RuntimeException("Service not found in catalog: " + servicename.toLowerCase());
		}
		JsonObject service = services.get(servicename);
		JsonArray plans = service.getAsJsonObject("spec").getAsJsonArray("plans");
		List<String> list = new ArrayList<>();
		plans.forEach(p -> {
			JsonObject meta = p.getAsJsonObject().getAsJsonObject("metadata");
			JsonElement cuz = meta.get("customize");
			if (cuz instanceof JsonNull) {
				return;
			}
			JsonObject jobj = (JsonObject) cuz;
			jobj.entrySet().forEach(e -> {
				list.add(e.getKey());
			});
		});
		return list;
	}

	private Catalog() {
		try {
			String raw = ServiceResource.callDFToGetAllServices();
			JsonElement json = new JsonParser().parse(raw);
			obj = json.getAsJsonObject();
			parse();
		} catch (Exception e) {
			LOG.error("Failed to get catalog from DF: ", e);
			throw new RuntimeException("Failed to get catalog from DF: ", e);
		}
	}

	/**
	 * List all available services.
	 * 
	 * @return multimap with key=serviceType, value=servicesList
	 */
	public Multimap<String, String> listAllServices() {
		Multimap<String, String> map = HashMultimap.create();
		for (JsonObject service : services.values()) {
			JsonObject sm = service.getAsJsonObject("spec").getAsJsonObject("metadata");
			JsonPrimitive name = service.getAsJsonObject("spec").getAsJsonPrimitive("name");
			JsonPrimitive type = sm.getAsJsonPrimitive("type") == null ? name : sm.getAsJsonPrimitive("type");
			map.put(type.getAsString(), name.getAsString().toLowerCase());
		}
		return map;
	}

	/**
	 * Get all services by the specified type
	 * 
	 * @param type
	 * @return
	 */
	public Set<String> getServices(String type) {
		Collection<String> ss = listAllServices().get(type);
		return Sets.newHashSet(ss);
	}

	private void parse() {
		Preconditions.checkNotNull(obj);
		JsonArray array = obj.getAsJsonArray("items");
		array.forEach(e -> {
			JsonObject s = e.getAsJsonObject();
			JsonObject m = s.getAsJsonObject("metadata");
			String name = m.getAsJsonPrimitive("name").getAsString();
			services.put(name.toLowerCase(), s);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Service [{}] added to catalog.", name);
			}
		});
	}

	public static void main(String[] args) {
		Collection<String> svc = Catalog.getInstance().listAllServices().values();
		svc.forEach(s -> {
			try {
				List<String> keys = Catalog.getInstance().getServiceQuotaKeys(s);
				System.out.println(">>> " + s + "->" + keys);
			} catch (Exception e) {
				System.out.println("ERROR: " + s);
			}

		});

	}

	/**
	 * Get service by the specified service name.
	 * 
	 * @param serviceName
	 * @return
	 */
	public JsonObject getServiceByName(String serviceName) {
		return services.get(serviceName.toLowerCase());
	}

	public JsonObject getJson() {
		return obj;
	}

	/**
	 * Get service type by the specified service name. If can NOT find the
	 * service type using the service name.
	 * 
	 * @param serviceName
	 * @return
	 */
	public String getServiceType(String serviceName) {
		JsonObject service = services.get(serviceName.toLowerCase());
		if (service == null) {
			LOG.error("getServiceType: Service [{}] not found in catalog.", serviceName);
			throw new RuntimeException("getServiceType: Service not found in catalog: " + serviceName);
		}
		JsonObject sm = service.getAsJsonObject("spec").getAsJsonObject("metadata");
		JsonPrimitive type = sm.getAsJsonPrimitive("type");
		if (type != null) {
			return type.getAsString().toLowerCase();
		} else {
			LOG.debug("Can NOT find the service type in the Catalog, set the service type using the service name: "
					+ serviceName);
			return serviceName.toLowerCase();
		}
	}

	public String getServiceCategory(String serviceName) {
		JsonObject service = services.get(serviceName.toLowerCase());
		if (service == null) {
			LOG.error("getServiceCategory: Service [{}] not found in catalog.", serviceName);
			throw new RuntimeException("getServiceCategory: Service not found in catalog: " + serviceName);
		}
		JsonObject sm = service.getAsJsonObject("spec").getAsJsonObject("metadata");
		JsonPrimitive category = sm.getAsJsonPrimitive("category");
		if (category != null) {
			return category.getAsString().toLowerCase();
		} else {
			LOG.debug("Can NOT find the service Category in the Catalog, set the category using default value: "
					+ Constant.SERVICE);
			return Constant.SERVICE;
		}
	}

}
