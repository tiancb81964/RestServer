package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.PlatformRole;

/**
 * 
 * @author zhaoyim
 *
 */
public interface PlatformRoleMapper {

	/**
	 * 
	 * @return
	 */
	public List<PlatformRole> selectAllPlatformRoles();

	/**
	 * 
	 * @param platformRoleId
	 * @return
	 */
	public PlatformRole selectPlatformRoleById(@Param("id") int platformRoleId);
}
