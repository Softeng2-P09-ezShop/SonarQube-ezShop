package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.polito.ezshop.util.DatabaseConnection;

public class CustomerCard {

	public static String storeCardInDb() throws SQLException{
		long number=0;
		number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		String sql = "INSERT INTO customercard (customer_card, points) VALUES (?, 0)";  
        Connection connection = null; 
        PreparedStatement statement = null;
	        
        try {
        	connection = DatabaseConnection.connect();
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, Long.toString(number));
	        
			if(statement.executeUpdate() > 0) 
				return Long.toString(number);
			else 
				return null;
        }finally { 
            statement.close();
    		connection.close();
		}
	}

	public static boolean modifyPointsOnCardInDb(String customerCard, int pointsToBeAdded) throws SQLException{
		String sql = "UPDATE customercard SET points = CASE WHEN points+? >= 0 THEN points+? END WHERE customer_card=?";  
        boolean result = false;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
	    	statement = connection.prepareStatement(sql);  
	    	statement.setInt(1, pointsToBeAdded);
	    	statement.setInt(2, pointsToBeAdded);
	    	statement.setString(3, customerCard);
	        statement.toString();
			int rs = statement.executeUpdate();
			if (rs > 0) 
				result=true;
        } finally { 
            statement.close();
    		connection.close();
		}
        return result;
	}
	
	public static boolean resetCustomerCards() throws SQLException {  
		String sql = "DELETE FROM customercard";  
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
