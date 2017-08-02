package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.UserMapper;
import com.asiainfo.ocmanager.persistence.model.User;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 
 * @author zhaoyim
 *
 */
public class TestUser {
	public static void main(String[] args) {
		SqlSession session = TestDBConnectorFactory.getSession();
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);

			System.out.println("=== Insert user ===");
			mapper.insertUser(new User("user001", "username001", "password001", "email001", "phone001",
					"description001", null));
			session.commit();

//			System.out.println("=== Insert user again ===");
//			mapper.insertUser(new User("user201", "username201", "password201", "email201", "phone201",
//					"description201", null));
//			session.commit();
//
//			System.out.println("=== All users ===");
//			List<User> users = mapper.selectAllUsers();
//			for (User u : users) {
//				System.out.println(u.toString());
//			}
//
//			System.out.println("=== Get User by id ===");
//			User user1 = mapper.selectUserById("user001");
//			System.out.println(user1.toString());
//
//			System.out.println("=== Update user ===");
//			mapper.updateUser(new User("user001", "username002", "email002", "phone002", "description002", null));
//			session.commit();
//
//			System.out.println("=== Get User by id ===");
//			User user2 = mapper.selectUserById("user001");
//			System.out.println(user2.toString());
//
//			System.out.println("=== Update user password by id ===");
//			mapper.updateUserPassword("user001", "123");
//			session.commit();
//
//			System.out.println("=== Get User by name ===");
//			User user3 = mapper.selectUserByName("username001");
//			System.out.println(user3.toString());
//
//			System.out.println("=== Update user password by name ===");
//			mapper.updateUserPasswordByName("username201", "123");
//			session.commit();
//
//			System.out.println("=== Get User by name ===");
//			User user4 = mapper.selectUserByName("username201");
//			System.out.println(user4.toString());
//
//			System.out.println("=== Delete user ===");
//			mapper.deleteUser("user001");
//			session.commit();
//
//			System.out.println("=== Delete user ===");
//			mapper.deleteUserByName("username201");
//			session.commit();
//			
//			System.out.println("=== check user by id and password ===");
//			User user5 = mapper.selectUserByIdAndPwd("2ef26018-003d-4b2b-b786-0481d4ee9fa8", "admin");
//			System.out.println(user5.toString());
//			
//			System.out.println("=== check user by name and password ===");
//			User user6 = mapper.selectUserByNameAndPwd("admin", "admin");
//			System.out.println(user6.toString());
			
			session.commit();
			
		} catch (Exception e) {
			if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
				MySQLIntegrityConstraintViolationException aa = (MySQLIntegrityConstraintViolationException) e.getCause();
			int zz = aa.getErrorCode();
				String xx = "a";
			}
			session.rollback();
		} finally {
			session.close();
		}

	}
}
