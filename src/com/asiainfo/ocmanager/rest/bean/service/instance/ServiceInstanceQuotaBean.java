package com.asiainfo.ocmanager.rest.bean.service.instance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;
import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public abstract class ServiceInstanceQuotaBean {

	private static Logger logger = LoggerFactory.getLogger(ServiceInstanceQuotaBean.class);

	protected String serviceType;

	/**
	 * 
	 * @param backingServiceName
	 * @param tenantId
	 * @return
	 */
	public abstract ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId,
			JsonObject parameters);

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	public static ServiceInstanceQuotaBean createServiceInstance(String service) {

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.HDFS)) {
			return new HdfsServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.HBASE)) {
			return new HbaseServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.HIVE)) {
			return new HiveServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.MAPREDUCE)) {
			return new MapreduceServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.SPARK)) {
			return new SparkServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.KAFKA)) {
			return new KafkaServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.REDIS)) {
			return new RedisServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals(ServiceInstanceQuotaConst.STORM)) {
			return new StormServiceInstanceQuotaBean();
		}

		return null;

	}
}
