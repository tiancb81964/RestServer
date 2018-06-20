package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.Broker;

/**
 * 
 * @author zhaoyim
 *
 */

public interface BrokerMapper {

	public void insert(Broker broker);
	
	public void updateURL(@Param("broker_name")String broker_name, @Param("broker_url")String broker_url);
	
	public List<Broker> getBrokers();
	
	public void updateStatus(@Param("broker_name")String broker_name, @Param("latest_status")String latest_status);
	
	public Broker getBrokerByName(@Param("broker_name")String broker_name);
	
}
