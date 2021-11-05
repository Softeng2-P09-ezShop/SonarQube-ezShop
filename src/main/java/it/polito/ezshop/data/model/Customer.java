package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class Customer implements it.polito.ezshop.data.Customer {

	private String customerName;
	private String customerCard;
	private Integer id;
	private Integer points;
	
	public Customer() { }
	
	public Customer(String customerName, String customerCard, Integer id, Integer points) {
		this.customerName = customerName;
		this.customerCard = customerCard;
		this.id = id;
		this.points = points;
	}

	@Override
	public String getCustomerName() {
		return customerName;
	}
	
	@Override
	public void setCustomerName(String customerName) {
		if (customerName != null && customerName.length() > 0) 
		this.customerName = customerName;
	}
	
	@Override
	public String getCustomerCard() {
		return customerCard;
	}
	
	@Override
	public void setCustomerCard(String customerCard) {
		this.customerCard = customerCard;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public Integer getPoints() {
		return points;
	}

	@Override
	public void setPoints(Integer points) {
		this.points = points;
	}
	
	
	public static Integer storeCustomerInDb(String customerName) throws SQLException {  
        String sql = "INSERT INTO customer (customer_name, customer_card) VALUES (?, ?)";  
        Integer customerId = -1;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
        	System.out.println("connection after: " + connection.toString());
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, customerName);
	    	statement.setString(2, "");
	        
			int num=statement.executeUpdate();
			System.out.println(statement.toString());
			if (num!=0)
				customerId = statement.getGeneratedKeys().getInt(1);
        } finally { 
            statement.close();
    		connection.close();
		}
    
        return customerId;
    }
	
	
	public static boolean modifyCustomerInDb(Integer id, String newCustomerName, String newCustomerCard) throws SQLException {  
        String sql = "UPDATE customercard set points= "
        		+ "CASE WHEN (select points from customercard where customer_card=(select customer_card from customer where id= ?)) IS  NULL "
        		+ "THEN 0 "
        		+ "ELSE (select points from customercard where customer_card=(select customer_card from customer where id= ?)) "
        		+ "END where customer_card=?; ";  
        boolean result = false;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();
	    	statement = connection.prepareStatement(sql);  
	    	statement.setInt(1, id);
	    	statement.setInt(2, id);
	    	statement.setString(3, newCustomerCard);
	        
			int num = statement.executeUpdate();
			if (num > 0) {
				sql = "UPDATE customer SET customer_name = ?, customer_card=? WHERE id=?";
				statement = connection.prepareStatement(sql);  
		    	statement.setString(1, newCustomerName);
		    	statement.setString(2, newCustomerCard);
		    	statement.setInt(3, id);

				return statement.executeUpdate() > 0 ? true : false;
			}
				
        } finally { 
            statement.close();
    		connection.close();
		}
        return result;
    }
	
	public static boolean deleteCustomerFromDb(Integer id) throws SQLException {  
        String sql = "DELETE FROM customer WHERE id=?";  
        boolean result = false;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
        	System.out.println("connection after: " + connection.toString());
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, id.toString());
	        
			int num=statement.executeUpdate();
			System.out.println(statement.toString());
			if (num!=0)
				result=true;
        } finally { 
            statement.close();
    		connection.close();
		}
        return result;
    }
	
	public static Customer getCustomerFromDb(Integer id) throws SQLException {  
        String sql = "select * from customer c left join  customercard cc on c.customer_card = cc.customer_card where c.id = ?";  
        Customer customer = null;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
        	System.out.println("connection after: " + connection.toString());
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, id.toString());
	        
			ResultSet rs = statement.executeQuery();
			System.out.println(statement.toString());
			while(rs.next()) {    
				customer = new Customer(rs.getString("customer_name"), rs.getString("customer_card"), rs.getInt("id"), rs.getInt("points"));
			} 
        } finally { 
            statement.close();
    		connection.close();
		}
        
        return customer;
    }
	
	public static List<it.polito.ezshop.data.Customer> getAllCustomersFromDb() throws SQLException {  
        String sql = "SELECT * FROM customer c left join customercard cc on c.customer_card = cc.customer_card"; //FULL OUTER JOIN customercard ON customer.customer_card=customercard.customer_card";  
        List<it.polito.ezshop.data.Customer> customers = new ArrayList<it.polito.ezshop.data.Customer>();
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
        	System.out.println("connection after: " + connection.toString());
	    	statement = connection.prepareStatement(sql);  
	        
			ResultSet rs = statement.executeQuery();
			System.out.println(statement.toString());
			while(rs.next()) {
				customers.add(new Customer(rs.getString("customer_name"), rs.getString("customer_card"), rs.getInt("id"), rs.getInt("points")));
			}
        } finally { 
            statement.close();
    		connection.close();
		}
        
        return customers;
    }
	
	public static boolean attachCardToCustomerInDb(String customerCard, Integer customerId) throws SQLException {  
        String sql = "UPDATE customer SET customer_card = ? WHERE id=?";  
        boolean result = false;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
        	System.out.println("connection after: " + connection.toString());
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, customerCard);
	    	statement.setString(2, customerId.toString());
	        
			int num = statement.executeUpdate();
			System.out.println(statement.toString());
			if (num!=0)
				result=true;
        } finally { 
            statement.close();
    		connection.close();
		}
        return result;
    }

	public static boolean resetCustomer() throws SQLException {  
        String sql = "DELETE FROM customer";  
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
