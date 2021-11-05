package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class ProductType implements it.polito.ezshop.data.ProductType {

	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;

	public ProductType() { }

	public ProductType(Integer quantity, String location, String note, String productDescription, String barCode,
					   Double pricePerUnit, Integer id) {
		this.quantity = quantity;
		this.location = location;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
		this.id = id;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		if(quantity != null && quantity >= 0)
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		if (note != null && !note.isEmpty())
		this.note = note;
	}

	@Override
	public String getProductDescription() {
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		 if (productDescription != null && !productDescription.isEmpty())
		this.productDescription = productDescription;
	}

	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		if (barCode != null && !barCode.isEmpty())
		this.barCode = barCode;
	}

	@Override
	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		if(pricePerUnit != null && pricePerUnit >= 0)
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		if(id != null && id > 0)
		this.id = id;
	}

	public static Integer storeProductTypeInDb(String description, String productCode, double pricePerUnit, String note)
			throws SQLException {
		String sql = "INSERT INTO product_type (product_description, bar_code, price_per_unit, note) VALUES (?, ?, ?, ?)";
		Integer productId = -1;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, description);
			statement.setString(2, productCode);
			statement.setDouble(3, pricePerUnit);
			statement.setString(4, note);

			statement.executeUpdate();
			productId = statement.getGeneratedKeys().getInt(1);
		} finally {
			statement.close();
			connection.close();
		}

		return productId;
	}

	public static boolean updateProductTypeInDb(Integer id, String newDescription, String newCode, double newPrice,
												String newNote) throws SQLException {

		String sql = "UPDATE product_type SET product_description = ?, bar_code = ?, price_per_unit = ?, note = ? where id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, newDescription);
			statement.setString(2, newCode);
			statement.setDouble(3, newPrice);
			statement.setString(4, newNote);
			statement.setInt(5, id);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}

	public static boolean deleteProductTypeFromDb(Integer id) throws SQLException {
		String sql = "DELETE FROM product_type WHERE id = ?";
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

	public static List<it.polito.ezshop.data.ProductType> getAllProductTypesFromDb() throws SQLException {
		List<it.polito.ezshop.data.ProductType> products = new ArrayList<>();

		String sql = "SELECT * FROM product_type";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				products.add(new ProductType(result.getInt("quantity"), result.getString("location"),
						result.getString("note"), result.getString("product_description"), result.getString("bar_code"),
						result.getDouble("price_per_unit"), result.getInt("id")));
			}
		} finally {
			statement.close();
			connection.close();
		}

		return products;
	}

	public static it.polito.ezshop.data.ProductType getProductTypeByBarCodeFromDb(String barCode) throws SQLException {
		it.polito.ezshop.data.ProductType product = null;

		String sql = "SELECT * FROM product_type where bar_code = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, barCode);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				product = new ProductType(result.getInt("quantity"), result.getString("location"),
						result.getString("note"), result.getString("product_description"), result.getString("bar_code"),
						result.getDouble("price_per_unit"), result.getInt("id"));
			}
		} finally {
			statement.close();
			connection.close();
		}

		return product;
	}

	public static List<it.polito.ezshop.data.ProductType> getProductTypesByDescriptionFromDb(String description)
			throws SQLException {
		List<it.polito.ezshop.data.ProductType> products = new ArrayList<>();

		String sql = "SELECT * FROM product_type where product_description like ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + description + "%");

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				products.add(new ProductType(result.getInt("quantity"), result.getString("location"),
						result.getString("note"), result.getString("product_description"), result.getString("bar_code"),
						result.getDouble("price_per_unit"), result.getInt("id")));
			}
		} finally {
			statement.close();
			connection.close();
		}

		return products;
	}

	public static boolean updateQuantityInDb(Integer productId, int toBeAdded) throws SQLException {

		String sql = "UPDATE product_type SET quantity = quantity + ? where id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, toBeAdded);
			statement.setInt(2, productId);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}

	public static boolean updatePositionInDb(Integer productId, String newPos) throws SQLException {

		String sql = "UPDATE product_type SET location = ? where id = ?";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DatabaseConnection.connect();
			statement = connection.prepareStatement(sql);
			statement.setString(1, newPos);
			statement.setInt(2, productId);
			return statement.executeUpdate() > 0 ? true : false;
		} finally {
			statement.close();
			connection.close();
		}
	}

	public static boolean resetProductTypes() throws SQLException {  
        String sql = "DELETE FROM product_type";  
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
