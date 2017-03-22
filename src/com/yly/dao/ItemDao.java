package com.yly.dao;

import java.util.ArrayList;

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
	 * search item information by item_id
	 * 
	 * @return Item
	 */
	public Item search_itemInfo(int item_id);
	/**
	 * search item_id by item_name
	 * 
	 * @param item_name
	 * @return item_id
	 */
	public int search_itemId(String item_name);

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

	/**
	 * return all item_id
	 * 
	 * @return a list of item_id store in ArrayList<Integer>
	 */
	public ArrayList<Integer> getAllItem();
	/**
	 * return item_id which item's distance within the round with certain radius
	 * 
	 * @return a list of item_id store in ArrayList<Integer>
	 */
	public ArrayList<Integer> getItemWithinDistance(double latitude,double longitude,int radius);
	
}
