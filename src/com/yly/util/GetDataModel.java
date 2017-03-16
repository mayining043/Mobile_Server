package com.yly.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class GetDataModel {
	MysqlDataSource dataSource = new MysqlDataSource();
	Properties prop = new Properties();
	String server = null;
	String username = null;
	String password = null;
	JDBCDataModel dataModel = null;
	DataModel model = null;

	public GetDataModel() throws FileNotFoundException, IOException {
		prop.load(this.getClass().getClassLoader().getResourceAsStream("DBConfig.properties"));

		username = prop.getProperty("username");
		password = prop.getProperty("password");
		server = prop.getProperty("server");

		dataSource.setServerName(server);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setDatabaseName("mobilerecommenderdb");

		this.dataModel = new MySQLJDBCDataModel(dataSource, "ratings", "user_id", "item_id", "rating", null);
		this.model = dataModel;
	}

	public DataModel getDataModel() throws FileNotFoundException, IOException {

		return model;
	}

}
