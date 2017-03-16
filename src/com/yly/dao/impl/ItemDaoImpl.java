package com.yly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.yly.dao.ItemDao;
import com.yly.entity.Item;
import com.yly.util.DBUtil;

public class ItemDaoImpl implements ItemDao {

	@Override
	public String search_itemName(int item_id) {
		// SQL查询语句
		String sql = "select item_name from iteminfo where item_id=?";
		// 实例化数据库工具类
		DBUtil util = new DBUtil();
		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setInt(1, item_id);

			// 结果集
			ResultSet rs = pstmt.executeQuery();

			// 判断该用户是否存在
			if (rs.next()) {
				return rs.getString("item_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;
	}

	@Override
	public int addItem(Item i) {
		int item_id = i.getItem_id();
		// we need to check whether the item has already added
		if (null == search_itemName(item_id)) {

			// SQL插入语句,user_id will auto increase
			String sql = "insert into iteminfo (item_id,item_name,latitude,longitude,item_address,item_type,avg_price,avg_rating) values(?,?,?,?,?,?,?,?);";

			// 实例化数据库工具类
			DBUtil util = new DBUtil();

			// 获得数据库连接
			Connection conn = util.openConnection();

			try {
				// 创建预定义语句
				PreparedStatement pstmt = conn.prepareStatement(sql);

				// set sql parameter
				pstmt.setInt(1, i.getItem_id());
				pstmt.setString(2, i.getItem_name());
				pstmt.setDouble(3, i.getLatitude());
				pstmt.setDouble(4, i.getLongitude());
				pstmt.setString(5, i.getItem_address());
				pstmt.setString(6, i.getItem_type());
				pstmt.setDouble(7, i.getAvg_price());
				pstmt.setDouble(8, i.getAvg_rating());

				int rs = pstmt.executeUpdate();

				return rs;
			} catch (SQLException e) {
				e.printStackTrace();
				return -2;
			} finally {
				util.closeConn(conn);
			}
		} else { // if the item has already added than return -1
			return -1;
		}
	}

	@Override
	public int addUserFeedback(int user_id, int item_id, double rating, double latitude, double longitude, int time,
			int daytype, int weather, int season) {

		// SQL插入语句,user_id will auto increase
		String sql = "insert into ratings(user_id,item_id,rating,latitude,longitude,time,daytype,weather,season) values(?,?,?,?,?,?,?,?,?);";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// set sql parameter
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, item_id);
			pstmt.setDouble(3, rating);
			pstmt.setDouble(4, latitude);
			pstmt.setDouble(5, longitude);
			pstmt.setInt(6, time);
			pstmt.setInt(7, daytype);
			pstmt.setInt(8, weather);
			pstmt.setInt(9, season);

			int rs = pstmt.executeUpdate();

			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			util.closeConn(conn);
		}
	}

	@Override
	public Item search_itemInfo(int item_id) {
		// SQL查询语句
		String sql = "select * from iteminfo where item_id=?";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setInt(1, item_id);

			// 结果集
			ResultSet rs = pstmt.executeQuery();

			// 判断物品是否存在
			if (rs.next()) {
				// 实例化Item
				Item i = new Item();
				String item_name = rs.getString("item_name");
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");
				Double avg_price = rs.getDouble("avg_price");
				Double avg_rating = rs.getDouble("avg_rating");
				String item_type = rs.getString("item_type");
				String item_address = rs.getString("item_address");
				// 设置Item属性
				i.setItem_id(item_id);
				i.setItem_name(item_name);
				i.setAvg_price(avg_price);
				i.setAvg_rating(avg_rating);
				i.setItem_address(item_address);
				i.setItem_type(item_type);
				i.setLatitude(latitude);
				i.setLongitude(longitude);

				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;
	}

	@Override
	public ArrayList<Integer> getAllItem() {
		ArrayList<Integer> item_list = new ArrayList<>();
		// SQL查询语句
		String sql = "select item_id from iteminfo";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				item_list.add(rs.getInt("item_id"));
			}
			return item_list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}
		return null;
	}

	@Override
	public ArrayList<Integer> getItemWithinDistance(double latitude, double longitude, int radius) {
		// SQL查询语句
		String sql = "select item_id from iteminfo where (ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-latitude*PI()/180)/2),2)+COS(?*PI()/180)*COS(latitude*PI()/180)*POW(SIN((?*PI()/180-longitude*PI()/180)/2),2)))*1000))/1000<=?";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// 设置查询参数
			pstmt.setDouble(1, latitude);
			pstmt.setDouble(2, latitude);
			pstmt.setDouble(3, longitude);
			pstmt.setInt(4, radius);

			// 结果集
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Integer> item_list = new ArrayList<>();
			// 判断该物品是否存在
			while (rs.next()) {
				// 获得物品编号
				item_list.add(rs.getInt("item_id"));
			}
			return item_list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			util.closeConn(conn);
		}
	}
}
