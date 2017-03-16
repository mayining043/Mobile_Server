package com.yly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.yly.dao.DataDao;
import com.yly.util.DBUtil;

import net.librec.math.structure.SparseMatrix;

public class DataDaoImpl implements DataDao {

	// user/item {raw id, inner id} map
	private BiMap<Integer, Integer> userIds, itemIds;

	// inverse views of userIds, itemIds
	private BiMap<Integer, Integer> idUsers, idItems;

	public DataDaoImpl() {

		userIds = HashBiMap.create();
		itemIds = HashBiMap.create();
	}

	@Override
	public SparseMatrix readData() {
		// Table {row-id, col-id, rate}
		Table<Integer, Integer, Double> dataTable = HashBasedTable.create();

		// Map {col-id, multiple row-id}: used to fast build a rating matrix
		Multimap<Integer, Integer> colMap = HashMultimap.create();

		// SQL查询语句
		String sql = "select * from ratings ";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 结果集
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int user_id = rs.getInt("user_id");
				int item_id = rs.getInt("item_id");
				Double rate = rs.getDouble("rating");
				// inner id starting from 0
				int row = userIds.containsKey(user_id) ? userIds.get(user_id) : userIds.size();
				userIds.put(user_id, row);

				int col = itemIds.containsKey(item_id) ? itemIds.get(item_id) : itemIds.size();
				itemIds.put(item_id, col);
				dataTable.put(row, col, rate);
				colMap.put(col, row);
			}
			// build rating matrix
			SparseMatrix rateMatrix = new SparseMatrix(userIds.size(), itemIds.size(), dataTable, colMap);
			return rateMatrix;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			util.closeConn(conn);
		}
	}

	@Override
	public SparseMatrix[] readContextData() {

		// SQL查询语句
		String sql = "select * from ratings ";

		// 实例化数据库工具类
		DBUtil util = new DBUtil();

		// 获得数据库连接
		Connection conn = util.openConnection();

		try {
			// 创建预定义语句
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 结果集
			ResultSet rs = pstmt.executeQuery();
			// 获取总列数
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			// 上下文个数
			int numContext = columnCount - 5;
			// Map {col-id, multiple row-id}: used to fast build a context
			// matrix
			Multimap<Integer, Integer> colMap = HashMultimap.create();

			SparseMatrix[] contextMatrix = new SparseMatrix[numContext];
			// Table{row-id,col-id,context}
			@SuppressWarnings("unchecked")
			Table<Integer, Integer, Integer> contextTable[] = new Table[numContext];
			for (int i = 0; i < numContext; i++) {
				if (contextTable[i] == null)
					contextTable[i] = HashBasedTable.create();
			}
			while (rs.next()) {
				int user_id = rs.getInt("user_id");
				int item_id = rs.getInt("item_id");

				for (int i = 0; i < numContext; i++) {
					// inner id starting from 0
					int row = userIds.containsKey(user_id) ? userIds.get(user_id) : userIds.size();
					userIds.put(user_id, row);

					int col = itemIds.containsKey(item_id) ? itemIds.get(item_id) : itemIds.size();
					itemIds.put(item_id, col);

					int context = Integer.parseInt(rs.getString(i + 6));
					contextTable[i].put(row, col, context);
					colMap.put(col, row);
				}
			}
			// build rating matrix
			for (int i = 0; i < numContext; i++) {
				SparseMatrix temp = new SparseMatrix(userIds.size(), itemIds.size(), contextTable[i], colMap);
				contextMatrix[i] = temp;
			}
			return contextMatrix;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			util.closeConn(conn);
		}
	}

	public int getRawUserId(int innerId) {

		if (idUsers == null)
			idUsers = userIds.inverse();

		return idUsers.get(innerId);
	}

	public int getRawItemId(int innerId) {

		if (idItems == null)
			idItems = itemIds.inverse();

		return idItems.get(innerId);
	}

	@Override
	public int getInnerUserId(int user_id) {
		return userIds.get(user_id);
	}

	@Override
	public int getInnderItemId(int item_id) {
		if(itemIds.get(item_id)==null)
			return 0;
		return itemIds.get(item_id);
	}

}