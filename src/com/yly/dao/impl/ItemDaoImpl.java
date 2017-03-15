package com.yly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
