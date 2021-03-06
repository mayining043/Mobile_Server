package com.yly.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * get inner context condition id
	 * 
	 * @param condtion_id,
	 *            context means in which context
	 * 
	 * @return inner item id
	 */
	public int getInnderContextId(int condition_id, int context);

	/**
	 * get number of each context's condition
	 */
	public int[] getNumContextCondition();

	/**
	 * sort HashMap value in decrease order*/
	public List<Map.Entry<Integer, Double>> sortHashMapByValue(HashMap<Integer, Double> init_list);
}
