package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.User;

/**
 * 
 * @author zhaoyim
 *
 */

public interface UserMapper {

	/**
	 * 
	 * @return
	 */
	public List<User> selectAllUsers();

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public User selectUserById(@Param("id") String userId);

	/**
	 * 
	 * @param username
	 * @return
	 */
	public User selectUserByName(@Param("username") String userName);

	/**
	 * 
	 * @param user
	 * @return
	 */
	public void insertUser(User user);

	/**
	 * 
	 * @param user
	 */
	public void updateUser(User user);

	/**
	 * 
	 * @param userId
	 * @param password
	 */
	public void updateUserPassword(@Param("id") String userId, @Param("password") String password);

	/**
	 * 
	 * @param userName
	 * @param password
	 */
	public void updateUserPasswordByName(@Param("username") String userName, @Param("password") String password);

	/**
	 * 
	 * @param userId
	 */
	public void deleteUser(@Param("id") String userId);

	/**
	 * 
	 * @param userName
	 */
	public void deleteUserByName(@Param("username") String userName);

	/**
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public User selectUserByIdAndPwd(@Param("id") String id, @Param("password") String password);

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public User selectUserByNameAndPwd(@Param("username") String userName, @Param("password") String password);

}
