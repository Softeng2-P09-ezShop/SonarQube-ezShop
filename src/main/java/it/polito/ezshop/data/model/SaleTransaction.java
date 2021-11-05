package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.util.DatabaseConnection;

public class SaleTransaction extends BalanceOperation implements it.polito.ezshop.data.SaleTransaction {

	public static final String NAME = "SALE";

	public static final String STATUS_OPENED = "OPENED";
	public static final String STATUS_PAYED = "PAYED";
	public static final String STATUS_UNPAID = "UNPAID";
	public static final String STATUS_CLOSED = "CLOSED";

	private Integer ticketNumber;
	private List<it.polito.ezshop.data.TicketEntry> entries = new ArrayList<>();
	private double discountRate;
	private double price;
	private String status;
	private Integer balanceId;

	public SaleTransaction() { }

	public SaleTransaction(Integer ticketNumber, Integer balanceId, List<TicketEntry> entries, double discountRate,
			String status, double price, String type, String superStatus) {
		super(balanceId, LocalDate.now(), price, NAME, superStatus);
		this.ticketNumber = ticketNumber;
		this.entries = entries;
		this.discountRate = discountRate;
		this.price = price;
		this.balanceId = balanceId;
		this.status = status;
	}

	public SaleTransaction(Integer ticketNumber, double discountRate, String status, double price) {
		this.ticketNumber = ticketNumber;
		this.entries = new ArrayList<TicketEntry>();
		this.discountRate = discountRate;
		this.price = price;
		this.status = status;
	}

	@Override
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		if(ticketNumber != null && ticketNumber > 0)
			this.ticketNumber = ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return entries;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		if(entries != null && entries.size() > 0)
			this.entries = entries;
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		if (discountRate >= 0.0 && discountRate < 1.0)
			this.discountRate = discountRate;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		if (price > 0)
			this.price = price;
	}

	public boolean addProductToSale(String barcode, String productDescription, double pricePerUnit, int amount) {
		if(barcode != null && barcode.length() > 0 
				&& productDescription != null && productDescription.length() > 0 
				&& pricePerUnit > 0 && amount > 0) 
			return entries.add(new it.polito.ezshop.data.model.TicketEntry(barcode, productDescription, amount, pricePerUnit, 0.0));
		return false;
	}

	/**
	 * Return the number of points of a sale transaction. Every 10€ the number of
	 * points is increased by 1 (i.e. 19.99€ returns 1 point, 20.00€ returns 2
	 * points).
	 *
	 * @return number of points
	 */
	public int computePointsForSale() {
		return (int) Math.floor(getPrice() / 10.0);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(status != null && (status.equalsIgnoreCase(STATUS_CLOSED) || status.equalsIgnoreCase(STATUS_OPENED) || status.equalsIgnoreCase(STATUS_PAYED)|| status.equalsIgnoreCase(STATUS_UNPAID)))
			this.status = status;
	}

	public Integer getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Integer balanceId) {
		if(balanceId > 0)
			this.balanceId = balanceId;
	}

	public static int getLastIdFromSaleTransactions() throws SQLException {
		Integer saleId = -1;
		String sql = "SELECT max(ticket_number) as ticket_number FROM sale_transaction";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			saleId = result.getInt("ticket_number");
		} finally {
			statement.close();
			connection.close();
		}
		return saleId;
	}

	public static SaleTransaction getSaleTransactionFromDb(Integer transactionId) throws SQLException {
		SaleTransaction transaction = null;
		String sql = "SELECT * FROM sale_transaction where ticket_number = ? "; // and status='CLOSED'
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, transactionId);

			ResultSet result = statement.executeQuery();
			while(result.next()) {
				transaction = new SaleTransaction(result.getInt("ticket_number"), result.getDouble("discount_rate"), result.getString("status"), result.getDouble("price"));
				transaction.setEntries(it.polito.ezshop.data.model.TicketEntry.getTicketsFromDb(transaction.getTicketNumber()));
			}
		} finally {
			statement.close();
			connection.close();
		}
		return transaction;
	}

	public static Integer storeSaleInDb(Integer transactionId, double discountRate, double price, String status) throws SQLException {
		String sql = "INSERT INTO sale_transaction (ticket_number, discount_rate, price, status) VALUES (?, ?, ?, ?)";
		Integer saleId = -1;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, transactionId);
			statement.setDouble(2, discountRate);
			statement.setDouble(3, price);
			statement.setString(4, status);

			statement.executeUpdate();
			return transactionId;
			//saleId = statement.getGeneratedKeys().getInt(1);
		} finally {
			statement.close();
			connection.close();
		}
		//return saleId;
	}

	public static boolean updateSaleStatusInDb(Integer ticketNumber, String status) throws SQLException {
		String sql = "UPDATE sale_transaction SET status = ? where ticket_number = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, status);
			statement.setInt(2, ticketNumber);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}

	public static boolean deleteSaleTransactionFromDb(Integer ticketNumber) throws SQLException {
		String sql = "DELETE FROM sale_transaction WHERE ticket_number = ?";
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

	public static boolean resetSaleTransaction() throws SQLException {  
        String sql = "DELETE FROM sale_transaction";  
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
