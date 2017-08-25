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
	 * 
	 * @param tenantId
	 * @param instanceName
	 * @param quota
	 */
	public void updateInstanceQuota(@Param("tenantId") String tenantId, @Param("instanceName") String instanceName,
			@Param("quota") String quota);
	
	/**
	 * Get service instance by tenantID and serviceInstanceName
	 * @param tenantId
	 * @param instanceName
	 * @return
	 */
	public ServiceInstance getServiceInstance(@Param("tenantId") String tenantId, @Param("instanceName") String instanceName);

}
