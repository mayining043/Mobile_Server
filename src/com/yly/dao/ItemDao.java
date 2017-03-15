package com.yly.dao;

import com.yly.entity.Item;

public interface ItemDao {
	/**
	 * search item_name by item_id
	 * 
	 * @param item_id
	 * @return item_name
	 */
	public String search_itemName(int item_id);

	/**
	 * add user's feedback to database
	 * 
	 * @return if success return a number>=0, -1 means other exception
	 */
	public int addUserFeedback(int user_id, int item_id, double rating, double latitude, double longitude, int time,
			int daytype, int weather, int season);

	/**
	 * add item information to database
	 * 
	 * @return if success return a number>=0, -1 means the item has already
	 *         added,-2 means other exception
	 */
	public int addItem(Item i);
}
