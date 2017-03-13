package com.yly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yly.dao.UserDao;
import com.yly.entity.User;
import com.yly.util.DBUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public int register(User u) {
		String username = u.getUser_name();
		// we need to check whether the user_name has already used
		if (null == search_userinfo(username)) {

			// SQL插入语句,user_id will auto increase
			String sql = "insert into userinfo (user_name,password) values(?,?);";

			// 实例化数据库工具类
			DBUtil util = new DBUtil();

			// 获得数据库连接
			Connection conn = util.openConnection();

			try {
				// 创建预定义语句
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// set sql parameter
				pstmt.setString(1, u.getUser_name());
				pstmt.setString(2, u.getPassword());

				int rs = pstmt.executeUpdate();

				return rs;
			} catch (SQLException e) {
				e.printStackTrace();
				return -2;
			} finally {
				util.closeConn(conn);
			}
		} else { // if the user_name has already used than return -1
			return -1;
		}
	}

	@Override
	public User login(String user_name, String password) {
		// SQL查询语句
		String sql = "select * from userinfo where user_name=? and password=?";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setString(1, user_name);
			pstmt.setString(2, password);

			// 结果集
			ResultSet rs = pstmt.executeQuery();

			// 判断该用户是否存在
			if (rs.next()) {
				// 获得用户ID
				int user_id = rs.getInt("user_id");

				// 实例化User
				User u = new User();

				// 设置User属性
				u.setUser_id(user_id);
				u.setUser_name(user_name);

				return u;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;

	}


	/**
	 * search_userInfo just like login function, but it does not need to enter
	 * user's password
	 */
	@Override
	public User search_userinfo(String user_name) {
		// SQL查询语句
		String sql = "select * from userinfo where user_name=?";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setString(1, user_name);

			// 结果集
			ResultSet rs = pstmt.executeQuery();

			// 判断该用户是否存在
			if (rs.next()) {
				// 获得用户ID
				int user_id = rs.getInt("user_id");

				// 实例化User
				User u = new User();

				// 设置User属性
				u.setUser_id(user_id);
				u.setUser_name(user_name);

				return u;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;
	}

	@Override
	public int search_userid(String user_name) {
		// SQL查询语句
		String sql = "select user_id from userinfo where user_name=?";
		// 实例化数据库工具类
		DBUtil util = new DBUtil();
		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setString(1, user_name);

			// 结果集
			ResultSet rs = pstmt.executeQuery();

			// 判断该用户是否存在
			if (rs.next()) {
				return rs.getInt("user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return -1;
	}

	public static void main(String args[]) {
		User u = new User("yly", "123");
		UserDaoImpl impl = new UserDaoImpl();
		int r_register = impl.register(u);
		System.out.println("r_register=" + r_register);
		User u_1 = impl.login("yly", "123");
		System.out.println(u_1.getUser_name() + " login");
		u_1 = impl.search_userinfo("yly");
		System.out.println(u_1.getUser_name() + " searched");
		System.out.println(impl.search_userid("yly"));
	}
}
