package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.ServiceInstance;

/**
 * 
 * @author zhaoyim
 *
 */
public interface ServiceInstanceMapper {

	/**
	 *
	 * @param tenantId
	 * @return
	 */
	public List<ServiceInstance> selectServiceInstancesByTenant(@Param("tenantId") String tenantId);

	/**
	 *
	 * @param serviceInstance
	 */
	public void insertServiceInstance(ServiceInstance serviceInstance);

	/**
	 *
	 * @param tenantId
	 * @param instanceName
	 */
	public void deleteServiceInstance(@Param("tenantId") String tenantId, @Param("instanceName") String instanceName);

	/**
	 * 
	 * @return
	 */
	public List<ServiceInstance> selectAllServiceInstances();

	/**
	 * Update instance quota
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param quota
	 */
	public void updateInstanceQuota(@Param("tenantId") String tenantId, @Param("instanceName") String instanceName,
			@Param("quota") String quota);

	/**
	 * Update instance attributes
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param attributes
	 */
	public void updateInstanceAttributes(@Param("tenantId") String tenantId, @Param("instanceName") String instanceName,
			@Param("attributes") String attributes);

	/**
	 * Get service instance by tenantID and serviceInstanceName
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @return
	 */
	public ServiceInstance getServiceInstance(@Param("tenantId") String tenantId,
			@Param("instanceName") String instanceName);

	/**
	 * Get service instance by serviceInstanceName
	 * 
	 * @param instanceName
	 * @return
	 */
	public ServiceInstance selectServiceInstanceByCuzBsiName(@Param("cuzBsiName") String cuzBsiName,
			@Param("serviceName") String serviceName);

	/**
	 * Get service instance by service name
	 * 
	 * @param tenantId
	 * @param serviceType
	 * @return
	 */
	public List<ServiceInstance> selectServiceInstanceByServiceName(@Param("tenantId") String tenantId,
			@Param("serviceName") String serviceName);

	/**
	 * 
	 * @param category
	 * @return
	 */
	public List<ServiceInstance> selectAllInstancesByCategory(@Param("category") String category);

	/**
	 * 
	 * @param tenantId
	 * @param category
	 * @return
	 */
	public List<ServiceInstance> selectInstancesByTenantAndCategory(@Param("tenantId") String tenantId,
			@Param("category") String category);

}
