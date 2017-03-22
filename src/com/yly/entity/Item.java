package com.yly.entity;

public class Item {
	private int item_id;
	private String item_name;
	private double longitude;
	private double latitude;
	private String item_address;


	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getItem_address() {
		return item_address;
	}

	public void setItem_address(String item_address) {
		this.item_address = item_address;
	}

}
