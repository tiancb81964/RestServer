package com.asiainfo.ocmanager.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Service;
import com.asiainfo.ocmanager.persistence.model.ServiceRolePermission;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.ServicePersistenceWrapper;
import com.asiainfo.ocmanager.rest.resource.persistence.ServiceRolePermissionWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServicesSynchronizer {

	private static final Logger logger = LoggerFactory.getLogger(ServicesSynchronizer.class);

	public static void syncUp() {
		cleanServiceRolePermission();
		syncServices();
		buildServiceRolePermission();
	}

	private static void cleanServiceRolePermission() {
		logger.info("ServicesSynchronizer : delete all record in service role permission");
		ServiceRolePermissionWrapper.deleteAll();
	}

	private static void buildServiceRolePermission() {
		List<Service> services = ServicePersistenceWrapper
				.getServicesByServiceType(Arrays.asList("hdfs", "hbase", "hive", "mapreduce", "spark", "kafka"));
		List<String> roles = Arrays.asList("a12a84d0-524a-11e7-9dbb-fa163ed7d0ae",
				"a13dd087-524a-11e7-9dbb-fa163ed7d0ae");

		for (String str : roles) {
			for (Service s : services) {
				ServiceRolePermission srp = new ServiceRolePermission();
				srp.setServiceName(s.getServicename());
				srp.setRoleId(str);

				switch (s.getServiceType().toLowerCase()) {
				case "hdfs":
					srp.setServicePermission("read,write,execute");
					break;
				case "hbase":
					srp.setServicePermission("read,write,create,admin");
					break;
				case "hive":
					srp.setServicePermission("select,update,create,drop,alter,index,lock");
					break;
				case "mapreduce":
					srp.setServicePermission("submit-app,admin-queue");
					break;
				case "spark":
					srp.setServicePermission("submit-app,admin-queue");
					break;
				case "kafka":
					srp.setServicePermission("publish,consume,configure,describe,create,delete,kafka_admin");
					break;
				default:
					logger.error("ServicesSynchronizer : service {} is not set correct service type",
							s.getServicename());
					// srp.setServicePermission("");
				}

				logger.info("ServicesSynchronizer : insert service: {} in service role permission", s.getServicename());
				ServiceRolePermissionWrapper.addServiceRolePermission(srp);
			}
		}
	}

	private static void syncServices() {
		ArrayList<Service> servicesInDB = (ArrayList<Service>) ServicePersistenceWrapper.getAllServices();

		ArrayList<Service> servicesInDf = new ArrayList<Service>();
		JsonArray items = Catalog.getInstance().getJson().getAsJsonArray("items");
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				JsonObject metadata = items.get(i).getAsJsonObject().getAsJsonObject("metadata");
				JsonObject spec = items.get(i).getAsJsonObject().getAsJsonObject("spec");
				JsonObject specMetadata = spec.getAsJsonObject("metadata");
				String name = spec.get("name").getAsString();
				String id = spec.get("id").getAsString();
				String description = spec.get("description").getAsString();
				String origin = metadata.getAsJsonObject("labels").get("asiainfo.io/servicebroker").getAsString();
				String type = parseServiceType(specMetadata, name);
				String category = parseServiceCategory(specMetadata, name);

				servicesInDf.add(new Service(id, name, description, origin, type, category));
			}
		}

		// intersection should update DB
		ArrayList<Service> servicesIntersection = intersectDBAndDF((ArrayList<Service>) servicesInDB.clone(),
				(ArrayList<Service>) servicesInDf.clone());

		for (Service s : servicesIntersection) {
			logger.info("ServicesSynchronizer : update service: {}", s.getServicename());
			ServicePersistenceWrapper.updateService(s);
		}

		// df subtract db, this will add services which are in df not in db
		ArrayList<Service> servicesDFSubDB = DFSubtractDB((ArrayList<Service>) servicesInDB.clone(),
				(ArrayList<Service>) servicesInDf.clone());

		for (Service s : servicesDFSubDB) {
			logger.info("ServicesSynchronizer : add service: {}", s.getServicename());
			ServicePersistenceWrapper.addService(s);
		}

		// db subtract df, this will delete services which are in db not in df
		ArrayList<Service> servicesDBSubDF = DBSubtractDF((ArrayList<Service>) servicesInDB.clone(),
				(ArrayList<Service>) servicesInDf.clone());
		// TODO delete the record from DB, should check the bsi
	}

	private static ArrayList<Service> DFSubtractDB(ArrayList<Service> db, ArrayList<Service> df) {
		df.removeAll(db);
		return df;
	}

	private static ArrayList<Service> DBSubtractDF(ArrayList<Service> db, ArrayList<Service> df) {
		db.removeAll(df);
		return db;
	}

	private static ArrayList<Service> intersectDBAndDF(ArrayList<Service> db, ArrayList<Service> df) {
		df.retainAll(db);
		return df;
	}

	private static String parseServiceType(JsonObject specMetadata, String serviceName) {
		JsonElement typeJE = specMetadata.get("type");
		if (typeJE == null) {
			logger.debug("The service {} is not have spec:metadata:type, please check with admin.", serviceName);
			// if the service did not have the type return the service name
			return serviceName;
		} else {
			String serviceType = specMetadata.get("type").getAsString();
			return serviceType;
		}
	}

	private static String parseServiceCategory(JsonObject specMetadata, String serviceName) {
		JsonElement categoryJE = specMetadata.get("category");
		if (categoryJE == null) {
			logger.debug("The service {} is not have category, please check with admin.", serviceName);
			// if the service did not have the category return the default value
			// service
			return Constant.SERVICE;
		} else {
			String serviceCategory = specMetadata.get("category").getAsString();
			return serviceCategory;
		}
	}

	public static void main(String[] args) {
		cleanServiceRolePermission();
		syncServices();
		buildServiceRolePermission();
	}

}
