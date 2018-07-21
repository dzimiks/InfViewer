package models;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import metaschema.Deserializer;
import models.tree.Node;

@SuppressWarnings("serial")
public class Warehouse extends Node {

	private static Warehouse instance;

	private String description;
	private String metaschemaString;
	private String location;
	private String type;
	private Connection dbConnection;

	private Warehouse(String name) {
		super(name);
	}

	public void loadWarehouse(String metaschemaString) throws Exception {
		this.metaschemaString = metaschemaString;
		Deserializer deserializer = new Deserializer(metaschemaString, this);
		deserializer.deserialize(metaschemaString, this);
	}
	
	public static Warehouse getInstance() {
		if (instance == null) {
			instance = new Warehouse("Warehouse");
		}
		
		return instance;
	}
	
	public void buildConnection() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String[] parts = location.split("@");
		String loc = parts[1];
		String[] unpw = parts[0].split(":");
		String username = unpw[0];
		String password = unpw[1];
		
		try {
			dbConnection = DriverManager.getConnection(loc, username, password);
			
			if (dbConnection == null) {
				throw new Exception("Couldn't establish a connection: " + loc + " " + username + " " + password);
			}

			DatabaseMetaData dbMetadata = dbConnection.getMetaData();
			String[] dbTypes = {"TABLE"};
			ResultSet rsTables = dbMetadata.getTables(null, null, null, dbTypes);
			
			while (rsTables.next()) {
				String tableName = rsTables.getString("TABLE_NAME");
				System.out.println(tableName);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getDbConnection() {
		return dbConnection;
	}

	public void remove(int index){
		super.getChildren().remove(index);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMetaschemaString() {
		return metaschemaString;
	}

	public void setMetaschemaString(String metaschemaString) {
		this.metaschemaString = metaschemaString;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}