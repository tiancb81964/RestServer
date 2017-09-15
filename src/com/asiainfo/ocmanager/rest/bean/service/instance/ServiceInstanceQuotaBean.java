package com.asiainfo.ocmanager.rest.bean.service.instance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.resource.utils.model.ServiceInstanceQuotaCheckerResponse;

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
	public abstract ServiceInstanceQuotaCheckerResponse checkCanChangeInst(String backingServiceName, String tenantId);

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

		if (service.toLowerCase().equals("hdfs")) {
			return new HdfsServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals("hbase")) {
			return new HbaseServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals("hive")) {
			return new HiveServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals("mapreduce")) {
			return new MapreduceServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals("spark")) {
			return new SparkServiceInstanceQuotaBean();
		}

		if (service.toLowerCase().equals("kafka")) {
			return new KafkaServiceInstanceQuotaBean();
		}

		return null;

	}
}
