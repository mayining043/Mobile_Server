package com.yly.dao;

import com.yly.entity.User;

public interface UserDao {
	/**
	 * user register function
	 * 
	 * @return if success return a number>=0, -1 means the user_name has been
	 *         registered, -2 means other exception
	 */
	public int register(User user);

	/**
	 * user login function
	 * 
	 * @return User entity
	 */
	public User login(String user_name, String password);

	/**
	 * search user's information by user_name
	 * 
	 * @return User
	 */
	public User search_userinfo(String user_name);

	/**
	 * search user_id by user_name
	 * 
	 * @param user_name
	 * @return user_id
	 */
	public int search_userid(String user_name);
}
