package com.asiainfo.ocmanager.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.ServiceResource;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	 * @return multimap with key=serviceType, value=servicesList
	 */
	public Multimap<String, String> listAllServices(){
		Multimap<String, String> map = HashMultimap.create();
		for (JsonObject service : services.values()) {
			JsonObject sm = service.getAsJsonObject("spec").getAsJsonObject("metadata");
			JsonPrimitive type = sm.getAsJsonPrimitive("type");
			JsonPrimitive name = service.getAsJsonObject("spec").getAsJsonPrimitive("name");
			map.put(type.getAsString(), name.getAsString());
		}
		return map;
	}
	
	/**
	 * Get all services by the specified type
	 * @param type
	 * @return
	 */
	public Set<String> getServices(String type){
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
		String type = Catalog.getInstance().getServiceType("HDFSon111");
		System.out.println(">>> HDFSon111 service type: " + type);

		System.out.println(">>> all services： " + Catalog.getInstance().listAllServices());
	}

	/**
	 * Get service by the specified service name.
	 * 
	 * @param serviceName
	 * @return
	 */
	public JsonObject getServiceByName(String serviceName) {
		return services.get(serviceName);
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
			LOG.error("Service [{}] not found in catalog.", serviceName);
			throw new RuntimeException("Service not found in catalog: " + serviceName);
		}
		JsonObject sm = service.getAsJsonObject("spec").getAsJsonObject("metadata");
		JsonPrimitive type = sm.getAsJsonPrimitive("type");
		if (type != null) {
			return type.getAsString().toLowerCase();
		} else {
			LOG.info("Can NOT find the service type in the Catalog, set the service type using the service name!");
			return serviceName.toLowerCase();
		}
	}
}
