package it.polito.ezshop.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
	
	public static Connection connect() {
		Connection connection = null;
        try {
            // create a connection to the database
        	if(connection == null) connection = DriverManager.getConnection(Constants.DATABASE_URL);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
	}
	
	public static void createNewDatabase() throws SQLException {  
		Connection connection = null;
		PreparedStatement statement = null;
        try {  
            connection = DriverManager.getConnection(Constants.DATABASE_URL);  
            if (connection != null) {  
                DatabaseMetaData meta = connection.getMetaData();  
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("A new database has been created.");  
                
                String sql = "CREATE TABLE IF NOT EXISTS user (id integer PRIMARY KEY AUTOINCREMENT, username text NOT NULL, password text NOT NULL, role text NOT NULL);";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  
                
                sql = "CREATE TABLE IF NOT EXISTS balance_operation (balance_id integer PRIMARY KEY, date text NOT NULL, money double NOT NULL, type text NOT NULL);";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  
                
                sql = "CREATE TABLE IF NOT EXISTS customer (id integer PRIMARY KEY AUTOINCREMENT, customer_name text NOT NULL UNIQUE, customer_card text DEFAULT '');";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  
                
                sql = "CREATE TABLE IF NOT EXISTS customercard (customer_card text PRIMARY KEY, points integer NOT NULL);";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  

                sql = "CREATE TABLE IF NOT EXISTS product_type (id integer PRIMARY KEY AUTOINCREMENT, bar_code text UNIQUE NOT NULL, price_per_unit double NOT NULL, product_description text NOT NULL, note text NOT NULL, location text , quantity integer DEFAULT 0);";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  
                
                sql = "CREATE TABLE IF NOT EXISTS sale_transaction (ticket_number integer PRIMARY KEY , discount_rate double NOT NULL, price double NOT NULL,status TEXT NOT NULL);";
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate();  
                
                sql = "CREATE TABLE IF NOT EXISTS orders (order_id integer PRIMARY KEY AUTOINCREMENT, balance_id integer NOT NULL, product_code text NOT NULL, status text NOT NULL, quantity integer NOT NULL, price_per_unit double NOT NULL);";  
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate(); 
                
                sql = "CREATE TABLE IF NOT EXISTS ticket_entry (id integer PRIMARY KEY AUTOINCREMENT, ticket_number integer NOT NULL, bar_code text NOT NULL, discount_rate double NOT NULL, amount integer NOT NULL, price_per_unit double NOT NULL, product_description text NOT NULL);";  
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate(); 
                
                sql = "CREATE TABLE IF NOT EXISTS return_transaction (id integer PRIMARY KEY AUTOINCREMENT, ticket_number integer NOT NULL, products text NOT NULL, balance_id integer NOT NULL, status text NOT NULL, committed integer NOT NULL);";  
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate(); 
                
                sql = "CREATE TABLE IF NOT EXISTS products (id integer PRIMARY KEY AUTOINCREMENT, bar_code text NOT NULL, rfid text NOT NULL);";  
                statement = connection.prepareStatement(sql);  
                statement.executeUpdate(); 
                
//                sql = "INSERT INTO user (username, password, role) VALUES ('takla', '123', 'Administrator')";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
//                
//                sql = "INSERT INTO user (username, password, role) VALUES ('ehsan', '123', 'Administrator')";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
//                
//                sql = "INSERT INTO product_type (bar_code, price_per_unit, product_description, note) VALUES ('9330462119318', 10.00, 'test description', 'test note')";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
//                
//                sql = "INSERT INTO customer (customer_name) VALUES ('customer1')";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
//                
//                sql = "INSERT INTO sale_transaction (discount_rate, price, status) VALUES (0.00, 1230.00, 'CLOSED')";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
//                
//                sql = "INSERT INTO orders (balance_id, product_code, status, quantity, price_per_unit) VALUES (1, '9330462119318', 'PAID', 12, 10.00)";
//                statement = connection.prepareStatement(sql);  
//                statement.executeUpdate(); 
            }  
   
        } catch (SQLException e) {  
            System.out.println("Error: " + e.getMessage());  
        } finally {
            statement.close();
			connection.close();
		}
    }
}
