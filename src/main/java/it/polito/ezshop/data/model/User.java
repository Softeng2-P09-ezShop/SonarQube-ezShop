package it.polito.ezshop.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.util.DatabaseConnection;

public class User implements it.polito.ezshop.data.User {
	
	public static final String ROLE_ADMINISTRATOR = "Administrator";
	public static final String ROLE_CASHIER = "Cashier";
	public static final String ROLE_SHOPMANAGER = "ShopManager";
	private Integer id;
	private String username;
	private String password;
	private String role;

	public User() { }
	
	public User(Integer id, String username, String password, String role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public User(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public User(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		 if (id >= 0)
		this.id = id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setUsername(String username) {
		if (username != null && username.length() > 0) 
		this.username = username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setPassword(String password) {
		 if (password != null && password.length() > 0) 
		this.password = password;
	}

	@Override
	public String getRole() {
		return this.role;
	}

	@Override
	public void setRole(String role) {
		updateUserRights(this.role);
	}

	public static Integer storeUserInDb(String username, String password, String role) throws SQLException {  
        String sql = "INSERT INTO USER (username, password, role) VALUES (?, ?, ?)";  
        Integer userId = -1;
        Connection connection = null; 
        PreparedStatement statement = null;
        try {
        	connection = DatabaseConnection.connect();  
	    	statement = connection.prepareStatement(sql);  
	    	statement.setString(1, username);
	        statement.setString(2, password);
	        statement.setString(3, role);
	        
			statement.executeUpdate();
			userId = statement.getGeneratedKeys().getInt(1);
        } finally { 
            statement.close();
    		connection.close();
		}
    
        return userId;
    }

	public static boolean deleteUserFromDb(Integer id) throws SQLException {  
        String sql = "DELETE FROM USER WHERE id = ?";  
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

	public static List<it.polito.ezshop.data.User> getAllUsersFromDb() throws SQLException {
		List<it.polito.ezshop.data.User> users = new ArrayList<>();
		  
        String sql = "SELECT * FROM USER";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
		    
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				users.add(new User(result.getInt("id"), result.getString("username"), result.getString("password"), result.getString("role")));
			}
		} finally { 
            statement.close();
            connection.close(); 
        }
        
		return users;
	}
	
	public static User getUserFromDb(Integer id) throws SQLException {
		User user = null;
		  
        String sql = "SELECT * FROM USER where id = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
		    
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				user = new User(result.getInt("id"), result.getString("username"), result.getString("password"), result.getString("role"));
			}
		} finally { 
            statement.close();
            connection.close(); 
        }
        
		return user;
	}
	
	public static boolean updateUserRightsInDb(Integer id, String role) throws SQLException {
    	
        String sql = "UPDATE USER SET role = ? where id = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
			statement.setString(1, role);
			statement.setInt(2, id);
		    
			return statement.executeUpdate() > 0 ? true : false;
		} finally { 
            statement.close();
            connection.close(); 
        }
	}
	
	public static User authenticateFromDb(String username, String password) throws SQLException {
		User user = null;
		  
        String sql = "SELECT * FROM USER where username = ? and password = ?";  
        Connection connection = null; 
        PreparedStatement statement = null;
        
        try {
        	connection = DatabaseConnection.connect();  
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
		    
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				user = new User(result.getInt("id"), result.getString("username"), result.getString("password"), result.getString("role"));
			}
		} finally { 
            statement.close();
            connection.close(); 
        }
        
		return user;
	}
	
	public void updateUserRights(String role){
		if (role != null && role.length() > 0 && (role.equals("Administrator") || role.equals("Cashier") || role.equals("ShopManager"))) {
			this.role = role;
		}
	}
	
	public static boolean resetUsers() throws SQLException {  
		String sql = "DELETE FROM user";  
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
