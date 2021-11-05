package it.polito.ezshop.data.model;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class Product {
	
	private int id;
	private String barCode;
	private String rfid;

	public Product() {
		
	}
	
	public Product(int id, String barCode, String rfid) {
		this.id = id;
		this.barCode = barCode;
		this.rfid = rfid;
	}
	
	public Product(String barCode, String rfid) {
		this.barCode = barCode;
		this.rfid = rfid;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public static Integer insertProduct(String barCode, String rfid) throws SQLException {
		String sql = "INSERT INTO products (bar_code, rfid) VALUES (?,?)";
		Integer productId = -1;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, barCode);
			statement.setString(2, new DecimalFormat("000000000000").format(Integer.parseInt(rfid)));

			statement.executeUpdate();
			productId = statement.getGeneratedKeys().getInt(1);
		} finally {
			statement.close();
			connection.close();
		}
		return productId;
	}
	
	public static Product getProductByRFID(String rfid) throws SQLException {
		String sql = "SELECT * FROM products where rfid = ?";
		Product product = null;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, rfid);

			ResultSet result = statement.executeQuery();
			while(result.next()) {
				product = new Product(result.getInt("id"), result.getString("bar_code"), result.getString("rfid"));
			}
		} finally {
			statement.close();
			connection.close();
		}
		return product;
	}
	
	public static List<Product> getProductsByBarcode(String barCode) throws SQLException {
		String sql = "SELECT * FROM products where bar_code = ?";
		List<Product> products = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, barCode);
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				products.add(new Product(result.getInt("id"), result.getString("bar_code"), result.getString("rfid")));
			}
		} finally {
			statement.close();
			connection.close();
		}
		return products;
	}
	
	public static boolean deleteProductByRFID(String rfid) throws SQLException {  
        String sql = "DELETE FROM products where rfid = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
			statement.setString(1, rfid);
		    
			return statement.executeUpdate() > 0 ? true : false;
        } finally { 
            statement.close();
            connection.close(); 
        }
    }
	
	public static boolean resetProducts() throws SQLException {  
		String sql = "DELETE FROM products";  
		Connection connection = null; 
		PreparedStatement statement = null;
		
		try {
			connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);  
			
			return statement.executeUpdate() > 0 ? true : false;
		} finally { 
			statement.close();
			connection.close(); 
		}
	}
}
