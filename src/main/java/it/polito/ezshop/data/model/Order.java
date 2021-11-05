package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.util.DatabaseConnection;

public class Order extends BalanceOperation implements it.polito.ezshop.data.Order {
	
	public static final String NAME = "ORDER";
	public static final String STATUS_ISSUED = "ISSUED";
	public static final String STATUS_ORDERED = "ORDERED";
	public static final String STATUS_PAID = "PAYED";
	public static final String STATUS_COMPLETED = "COMPLETED";

	private Integer balanceId;
	private String productCode;
	private double pricePerUnit;
	private int quantity;
	private String status;
	private Integer orderId;

	public Order() { }

	public Order(Integer orderId, Integer balanceId, String productCode, String status, int quantity , double pricePerUnit, String superStatus, double money) {
		super(balanceId, LocalDate.now(), money, NAME, superStatus);
		this.balanceId = balanceId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
		this.orderId = orderId;
	}

	@Override
	public Integer getBalanceId() {
		return balanceId;
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		if(balanceId>0)
		this.balanceId = balanceId;
	}

	@Override
	public String getProductCode() {
		return productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		if (productCode != null && !productCode.isEmpty())
		this.productCode = productCode;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		if(pricePerUnit > 0.0)
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		if(quantity > 0)
		this.quantity = quantity;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		if(status != null && (status.equalsIgnoreCase(STATUS_COMPLETED) || status.equalsIgnoreCase(STATUS_PAID) || status.equalsIgnoreCase(STATUS_ORDERED)|| status.equalsIgnoreCase(STATUS_ISSUED)))
		this.status = status;
	}

	@Override
	public Integer getOrderId() {
		return orderId;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public static Integer issueOrderInDb(String productCode, int quantity, double pricePerUnit,Integer balanceId, String status) throws SQLException {

		String sql = "INSERT INTO orders (balance_id, product_code, status, quantity, price_per_unit) VALUES (?,?,?,?,?)";
		Integer orderId = -1;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, balanceId);
			statement.setString(2, productCode);
			statement.setString(3, status);
			statement.setInt(4, quantity);
			statement.setDouble(5, pricePerUnit);

			statement.executeUpdate();
			orderId = statement.getGeneratedKeys().getInt(1);
		} finally {
			statement.close();
			connection.close();
		}
		return orderId;
	}

	public static Order payOrderInDb(Integer orderId) throws SQLException {

		Order order = null;
		String sql = "SELECT * FROM orders where order_id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, orderId);

			ResultSet result = statement.executeQuery();
			while(result.next()) {
				order = new Order(result.getInt("order_id"), result.getInt("balance_Id"), result.getString("product_code"),
					result.getString("status"), result.getInt("quantity"), result.getDouble("price_per_unit"), BalanceOperation.STATUS_UNPAID, 0.0);
			}
			
			if (order != null && (order.getStatus().equals(STATUS_ISSUED) || order.getStatus().equals(STATUS_ORDERED))) {

				sql = "UPDATE orders SET status = ? where order_id = ?";

				try {
					statement = connection.prepareStatement(sql);
					statement.setString(1, STATUS_PAID);
					statement.setInt(2, order.getOrderId());
					if(statement.executeUpdate() > 0) {
						order.setStatus(STATUS_PAID);
						return order;
					}
				} finally {
					statement.close();
					connection.close();
				}
			} else
				return null;
		} finally {
			statement.close();
			connection.close();
		}
		return null;
	}

	public static boolean recordOrderArrivalInDb(Integer orderId, String RFIDFrom) throws SQLException, InvalidLocationException{

		it.polito.ezshop.data.ProductType product = null;
		Order order = null;
		boolean r = false;
		
		String sql = "SELECT * FROM orders where order_id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, orderId);

			ResultSet result = statement.executeQuery();
			while(result.next()) 
				order = new Order(result.getInt("order_id"), result.getInt("balance_Id"), result.getString("product_code"),
					result.getString("status"), result.getInt("quantity"), result.getDouble("price_per_unit"), BalanceOperation.STATUS_PAID, 0.0);

		} finally {
			statement.close();
			connection.close();
		}
		if(order != null) {
			if (order.getStatus().equalsIgnoreCase(STATUS_PAID)) {
				product = ProductType.getProductTypeByBarCodeFromDb(order.getProductCode());
				if(product != null) {
					if (product.getLocation() == null || product.getLocation().isEmpty())
						throw new InvalidLocationException();
					product.setQuantity(product.getQuantity() + order.getQuantity());
					order.setStatus(STATUS_COMPLETED);
	
					r = it.polito.ezshop.data.model.ProductType.updateQuantityInDb(product.getId(), product.getQuantity());
					r = updateOrderInDb(order.getOrderId(), order.getStatus());	
					if(RFIDFrom != null && !RFIDFrom.isEmpty()) {
						r = false;
						try {
							for(int i = 0; i < order.getQuantity(); i++) {
								Product.insertProduct(order.getProductCode(), String.valueOf(new Integer(RFIDFrom) + i) );
							}
							r = true;
						} catch (Exception e) { 
							return r; 
						}
					}
				}
				return r;
			} 
			else if(order.getStatus().equalsIgnoreCase(Order.STATUS_COMPLETED))
				return true;
			else 
				return false;
		}
		else return false;
	}

	//NEW METHOD
	public static boolean updateOrderInDb(Integer orderId, String status) throws SQLException {

		String sql = "UPDATE orders SET status = ? where order_id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, status);
			statement.setInt(2, orderId);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}

	public static List<it.polito.ezshop.data.Order> getAllOrdersFromDb() throws SQLException {
		List<it.polito.ezshop.data.Order> orders = new ArrayList<>();

		String sql = "SELECT * FROM orders";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				orders.add(new Order(result.getInt("order_id"), result.getInt("balance_Id"), result.getString("product_code"),
						result.getString("status"), result.getInt("quantity"), result.getDouble("price_per_unit"), "", 0.0));
			}
		} finally {
			statement.close();
			connection.close();
		}

		return orders;
	}

	public static boolean resetOrders() throws SQLException {  
        String sql = "DELETE FROM orders";  
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
