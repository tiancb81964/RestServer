package com.asiainfo.ocmanager.persistence.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.asiainfo.ocmanager.persistence.mapper.UserMapper;
import com.asiainfo.ocmanager.persistence.model.User;

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
			mapper.insertUser(new User("id3", "username", "password", "email", "123", "description", "aaa"));
			session.commit();
			System.out.println("=== Update user ===");
			mapper.updateUser(new User("id3", "username4", "password4", "email4", "1234", "description4"));
			session.commit();
			List<User> users = mapper.selectAllUsers();

			System.out.println("=== All users ===");
			for (User u : users) {
				System.out.println(u.getId());
				System.out.println(u.getUsername());
				System.out.println(u.getEmail());
				System.out.println(u.getDescription());
			}

			System.out.println("=== User by id ===");
			User user = mapper.selectUserById("id3");
			System.out.println(user.getId());
			System.out.println(user.getUsername());
			System.out.println(user.getEmail());
			System.out.println(user.getDescription());

			System.out.println("=== Delete user ===");
			mapper.deleteUser("id3");

			List<User> usersAfterD = mapper.selectAllUsers();

			System.out.println("=== All users after delete ===");
			for (User u : usersAfterD) {
				System.out.println(u.getId());
				System.out.println(u.getUsername());
				System.out.println(u.getEmail());
				System.out.println(u.getDescription());
			}

			session.commit();

		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}

	}
}
