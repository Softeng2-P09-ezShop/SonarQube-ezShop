package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.util.DatabaseConnection;

public class ReturnTransaction extends BalanceOperation {
	
	public static final String NAME = "RETURN";
    private Integer id;
    private Integer ticketNumber;
    private Map<String, Integer> products; 
    private boolean committed;
    private Integer balanceId;
    private String status;

    public ReturnTransaction() { }
    
    public ReturnTransaction(Integer id, Integer ticketNumber, Integer balanceId, boolean committed, String status, double price, String superStatus) {
		super(balanceId, LocalDate.now(), price, NAME, superStatus);
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.committed = committed;
        this.balanceId = balanceId;
        this.products = new HashMap<String, Integer>();
        this.status = status;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		if(id != null && id > 0)
			this.id = id;
	}

	public boolean isCommitted() {
		return committed;
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
	
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Integer ticketNumber) {
		if(ticketNumber != null && ticketNumber > 0)
			this.ticketNumber = ticketNumber;
	}

	public Integer getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Integer balanceId) {
		if(balanceId > 0)
			this.balanceId = balanceId;
	}
	
	public Map<String, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<String, Integer> products) {
		if(products != null)
			this.products = products;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void returnProduct(String productCode, int amount) {
		if(productCode != null && !productCode.isEmpty() && amount > 0)
			products.put(productCode, amount);
	}

	public static Integer storeReturnInDb(int ticketNumber, String products, int committed, int balanceId, String status)
			throws SQLException {
		String sql = "INSERT INTO return_transaction (ticket_number, products, committed, balance_id, status) VALUES (?, ?, ?, ?, ?)";
		Integer returnId = -1;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, ticketNumber);
			statement.setString(2, products);
			statement.setInt(3, committed);
			statement.setInt(4, balanceId);
			statement.setString(5, status);
			statement.executeUpdate();
			returnId = statement.getGeneratedKeys().getInt(1);
		} finally {
			statement.close();
			connection.close();
		}
		return returnId;
	}

	public static boolean deleteReturnTransactionFromDb(Integer returnId) throws SQLException {  
        String sql = "DELETE FROM return_transaction where id = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);  
			statement.setInt(1, returnId);   
			return statement.executeUpdate() > 0 ? true : false;
        } finally { 
            statement.close();
    		connection.close();
		}
    }

	public static boolean updateReturnStatusInDb(Integer id, String products, boolean committed, String status) throws SQLException {

		String sql = "UPDATE return_transaction SET committed = ?, products = ?, status = ? where id = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, committed ? 1 : 0);
			statement.setString(2, products);
			statement.setString(3, status);
			statement.setInt(4, id);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}
	
	public static boolean resetReturnTransaction() throws SQLException {  
		String sql = "DELETE FROM return_transaction";  
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
