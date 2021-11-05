package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class BalanceOperation implements it.polito.ezshop.data.BalanceOperation {
		
	public static final String TYPE_CREDIT = "CREDIT";
	public static final String TYPE_DEBIT = "DEBIT";
	public static final String TYPE_ORDER = "ORDER";
	public static final String TYPE_SALE = "SALE";
	public static final String TYPE_RETURN = "RETURN";
	
	public static final String STATUS_PAID = "PAID";
	public static final String STATUS_UNPAID = "UNPAID";
	
	private Integer balanceId;
	private LocalDate date;
	private double money;
	private String type;
	private String status;

	public BalanceOperation() { }

	public BalanceOperation(int balanceId, LocalDate date, double money, String type, String status) {
		this.balanceId = balanceId;
		this.date = date;
		this.money = money;
		this.type = type;
		this.status = status;
	}

	public static int incrementCounter() throws SQLException {
		int counter = getLastIdFromBalanceOperations();
		return counter+1;
	}
	
	@Override
	public Integer getBalanceId() {
		return balanceId;
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		if(balanceId > 0)
			this.balanceId = balanceId;
	}

	@Override
	public LocalDate getDate() {
		return date;
	}

	@Override
	public void setDate(LocalDate date) {
		  if (date != null)
		this.date = date;
	}

	@Override
	public double getMoney() {
		return money;
	}

	@Override
	public void setMoney(double money) {
		this.money = money;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		if(type != null && (type.equals("SALE") || type.equals("ORDER") || type.equals("RETURN") || type.equals("DEBIT") || type.equals("CREDIT")))
		this.type = type;
	}

	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		if(status != null && (status.equals("OPENED") || status.equals("CLOSED") || status.equals("PAID")))
		this.status = status;
	}

	public static int getLastIdFromBalanceOperations() throws SQLException {
		Integer saleId = -1;
		String sql = "SELECT max(balance_id) as balance_id FROM balance_operation";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			saleId = result.getInt("balance_id");
		} finally {
			statement.close();
			connection.close();
		}
		return saleId;
	}

	public static Integer storeBalanceInDb(int id, double money, String type) throws SQLException {  
        String sql = "INSERT INTO balance_operation (balance_id, date, money, type) VALUES (?, ?, ?, ?)";  
        Integer balanceId = -1;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
	    	statement = connection.prepareStatement(sql);  
	    	statement.setInt(1, id);
	    	statement.setString(2, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	        statement.setDouble(3, money);
	        statement.setString(4, type);
	        
			statement.executeUpdate();
			balanceId = statement.getGeneratedKeys().getInt(1);
        } finally { 
            statement.close();
    		connection.close();
		}
    
        return balanceId;
    }

	public static List<it.polito.ezshop.data.model.BalanceOperation> getCreditsAndDebitsFromDb() throws SQLException {
		List<it.polito.ezshop.data.model.BalanceOperation> balances = new ArrayList<>();
		  
        String sql = "SELECT * FROM balance_operation";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
		    
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				balances.add(new BalanceOperation(result.getInt("balance_id"), LocalDate.parse(result.getString("date")), result.getDouble("money"), result.getString("type"), BalanceOperation.STATUS_PAID));
			}
		} finally { 
            statement.close();
            connection.close(); 
        }
        
		return balances;
	}

	public static boolean deleteBalanceFromDb(Integer id) throws SQLException {  
        String sql = "DELETE FROM balance_operation WHERE balance_id = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);  
			statement.setInt(1, id);
		    
			return statement.executeUpdate() > 0 ? true : false;
        } finally { 
            statement.close();
            connection.close(); 
        }
    }

	public static boolean resetBalanceOperations() throws SQLException {  
        String sql = "DELETE FROM balance_operation";  
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
