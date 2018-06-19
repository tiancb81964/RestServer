package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.Service;

/**
 * 
 * @author zhaoyim
 *
 */

public interface ServiceMapper {

	/**
	 * 
	 * @return
	 */
	public List<Service> selectAllServices();

	/**
	 * 
	 * @param originList
	 * @return
	 */
	public List<Service> selectServicesByOrigin(@Param("originList") List<String> originList);

	/**
	 * 
	 * @param serviceId
	 * @return
	 */
	public Service selectServiceById(String serviceId);

	/**
	 * 
	 * @param service
	 * @return
	 */
	public void insertService(Service service);

}
