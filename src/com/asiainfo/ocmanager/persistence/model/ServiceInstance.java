package com.asiainfo.ocmanager.persistence.model;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstance {

	private String id;
	private String instanceName;
	private String tenantId;
	private String serviceId;
	private String serviceName;
	private String quota;
	private String status;
	private String cuzBsiName;
	private String attributes;
	private String serviceType;
	private String category;

	public ServiceInstance() {

	}

	public ServiceInstance(String id, String instanceName, String tenantId, String serviceId, String serviceName,
			String quota, String status, String cuzBsiName, String attributes, String serviceType, String category) {
		this.id = id;
		this.instanceName = instanceName;
		this.tenantId = tenantId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.quota = quota;
		this.status = status;
		this.cuzBsiName = cuzBsiName;
		this.attributes = attributes;
		this.serviceType = serviceType;
		this.category = category;
	}

	public ServiceInstance(String id, String instanceName, String tenantId, String serviceId, String serviceName,
			String quota, String status, String cuzBsiName, String attributes, String serviceType) {
		this.id = id;
		this.instanceName = instanceName;
		this.tenantId = tenantId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.quota = quota;
		this.status = status;
		this.cuzBsiName = cuzBsiName;
		this.attributes = attributes;
		this.serviceType = serviceType;
	}

	public ServiceInstance(String id, String instanceName, String tenantId, String serviceId, String serviceName,
			String quota, String status) {
		this.id = id;
		this.instanceName = instanceName;
		this.tenantId = tenantId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.quota = quota;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCuzBsiName() {
		return cuzBsiName;
	}

	public void setCuzBsiName(String cuzBsiName) {
		this.cuzBsiName = cuzBsiName;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
