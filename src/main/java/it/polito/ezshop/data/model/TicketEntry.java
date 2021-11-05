package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class TicketEntry implements it.polito.ezshop.data.TicketEntry {
	private String barcode;
	private String productDescription;
	private int amount;
	private double discountRate;
	private double pricePerUnit;

	public TicketEntry() {
		
	}
	
	public TicketEntry(String barcode, String productDescription, int amount, double pricePerUnit, double discountRate) {
		this.barcode = barcode;
		this.productDescription = productDescription;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.discountRate = discountRate;
	}
	
	@Override
	public String getBarCode() {
		// TODO Auto-generated method stub
		return barcode;
	}

	@Override
	public void setBarCode(String barCode) {
		if (barCode != null && !barCode.isEmpty())
		// TODO Auto-generated method stub
		this.barcode = barCode;

	}

	@Override
	public String getProductDescription() {
		// TODO Auto-generated method stub
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		 if (productDescription != null && !productDescription.isEmpty())
		// TODO Auto-generated method stub
		this.productDescription = productDescription;

	}

	@Override
	public int getAmount() {
		// TODO Auto-generated method stub
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		if(amount != 0 && amount >= 0)
		// TODO Auto-generated method stub
		this.amount = amount;

	}

	@Override
	public double getPricePerUnit() {
		// TODO Auto-generated method stub
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		
		// TODO Auto-generated method stub
		if(pricePerUnit > 0) {
		this.pricePerUnit = pricePerUnit;
		}
	}

	@Override
	public double getDiscountRate() {
		// TODO Auto-generated method stub
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		// TODO Auto-generated method stub
		this.discountRate = discountRate;
	}

	public static Integer storeTicketEntryInDb(Integer ticketNumber, String barCode, double discountRate, double amount, double pricePerUnit, String description) throws SQLException {  
		String sql = "INSERT INTO ticket_entry (ticket_number, bar_code, discount_rate, amount, price_per_unit, product_description) VALUES (?, ?, ?, ?, ?, ?)";  
        Integer ticketEntryId = -1;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
	    	statement = connection.prepareStatement(sql);  
	    	statement.setInt(1, ticketNumber);
	        statement.setString(2, barCode);
	        statement.setDouble(3, discountRate);
	        statement.setDouble(4, amount);
	        statement.setDouble(5, pricePerUnit);
	        statement.setString(6, description);
	        
			statement.executeUpdate();
			ticketEntryId = statement.getGeneratedKeys().getInt(1);
        } finally { 
            statement.close();
    		connection.close();
		}
    
        return ticketEntryId;
    }
	
	public static List<it.polito.ezshop.data.TicketEntry> getTicketsFromDb(Integer ticketNumber) throws SQLException {
		List<it.polito.ezshop.data.TicketEntry> tickets = new ArrayList<it.polito.ezshop.data.TicketEntry>();
		  
        String sql = "SELECT * FROM ticket_entry where ticket_number = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
			statement.setInt(1, ticketNumber);
		    
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				tickets.add(new TicketEntry(result.getString("bar_code"), result.getString("product_description"), result.getInt("amount"), 
						result.getDouble("price_per_unit"), result.getDouble("discount_rate")));
			}
		} finally { 
            statement.close();
            connection.close(); 
        }
        
		return tickets;
	}

	public static boolean updateAmountInDb(Integer ticketNumber, String barCode, int amount) throws SQLException {

		String sql = "UPDATE ticket_entry SET amount = ? where ticket_number = ? and bar_code = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, amount);
			statement.setInt(2, ticketNumber);
			statement.setString(3, barCode);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}
	
	public static boolean resetTicketEntries() throws SQLException {  
		String sql = "DELETE FROM ticket_entry";  
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
	
	public static boolean deleteTicketsBySaleFromDb(Integer ticketNumber) throws SQLException {
		String sql = "DELETE FROM ticket_entry WHERE ticket_number = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, ticketNumber);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}
}
