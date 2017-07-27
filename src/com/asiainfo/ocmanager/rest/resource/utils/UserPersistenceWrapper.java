package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.DBConnectorFactory;
import com.asiainfo.ocmanager.persistence.mapper.UserMapper;
import com.asiainfo.ocmanager.persistence.model.User;
import com.asiainfo.ocmanager.rest.utils.UUIDFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class UserPersistenceWrapper {

	/**
	 * 
	 * @return
	 */
	public static List<User> getUsers() {
		SqlSession session = DBConnectorFactory.getSession();
		List<User> users = new ArrayList<User>();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			users = mapper.selectAllUsers();

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return users;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public static User getUserById(String userId) {
		SqlSession session = DBConnectorFactory.getSession();
		User user = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			user = mapper.selectUserById(userId);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static User getUserByName(String userName) {
		SqlSession session = DBConnectorFactory.getSession();
		User user = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			user = mapper.selectUserByName(userName);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static User createUser(User user) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			String uuid = UUIDFactory.getUUID();
			user.setId(uuid);
			mapper.insertUser(user);

			user = mapper.selectUserById(uuid);
			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static User updateUser(User user) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateUser(user);

			user = mapper.selectUserById(user.getId());
			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

	/**
	 * 
	 * @param userId
	 * @param password
	 */
	public static void updateUserPasswordById(String userId, String password) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateUserPassword(userId, password);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 */
	public static void updateUserPasswordByName(String userName, String password) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.updateUserPasswordByName(userName, password);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 * @param userId
	 */
	public static void deleteUser(String userId) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.deleteUser(userId);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	/**
	 * 
	 * @param userName
	 */
	public static void deleteUserByName(String userName) {
		SqlSession session = DBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.deleteUserByName(userName);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static User getUserByNameAndPwd(String userName, String password) {
		SqlSession session = DBConnectorFactory.getSession();
		User user = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			user = mapper.selectUserByNameAndPwd(userName, password);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

	/**
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	public static User getUserByIdAndPwd(String userId, String password) {
		SqlSession session = DBConnectorFactory.getSession();
		User user = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			user = mapper.selectUserByIdAndPwd(userId, password);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return user;
	}

}
