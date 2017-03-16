package com.yly.dao;

import net.librec.math.structure.SparseMatrix;

/**
 * A data access object (DAO) to database
 * 
 * 
 */
public interface DataDao {
	/**
	 * @return trainMatrix and testMatrix
	 */
	public SparseMatrix readData();

	/**
	 * @return contextMatrix
	 */
	public SparseMatrix[] readContextData();

	/**
	 * @param innerId
	 * 
	 * @return raw user id
	 */
	public int getRawUserId(int innerId);

	/**
	 * @param user_id
	 * 
	 * @return inner user id
	 */
	public int getInnerUserId(int user_id);

	/**
	 * @param innerId
	 * 
	 * @return raw item id
	 */
	public int getRawItemId(int innerId);

	/**
	 * @param item_id
	 * 
	 * @return inner item id
	 */
	public int getInnderItemId(int item_id);

}
