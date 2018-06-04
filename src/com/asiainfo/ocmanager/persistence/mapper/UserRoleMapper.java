package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.UserRoleView;

public interface UserRoleMapper {

	/**
	 * 
	 * @param tenantId
	 * @return
	 */
	public List<UserRoleView> selectUsersRolesInTenant(String tenantId);

	/**
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	public UserRoleView selectRoleBasedOnUserAndTenant(@Param("userName") String userName,
			@Param("tenantId") String tenantId);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRoleView> selectTenantAndRoleBasedOnUserId(@Param("userId") String userId);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<UserRoleView> selectTenantAndRoleBasedOnUserName(@Param("userName") String userName);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<UserRoleView> selectTURBasedOnUserNameAndRoleId(@Param("userName") String userName,
			@Param("roleId") String roleId);

	/**
	 * 
	 * @param roleName
	 * @param tenantId
	 * @return
	 */
	public List<UserRoleView> selectUserBasedOnRoleNameAndTenantId(@Param("roleName") String roleName,
			@Param("tenantId") String tenantId);

}
