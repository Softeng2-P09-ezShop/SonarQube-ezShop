package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.BalanceOperation;
import it.polito.ezshop.data.model.Order;
import it.polito.ezshop.data.model.ProductType;
import it.polito.ezshop.exceptions.InvalidLocationException;

public class OrderTest {
    
    @Test
    public void testUNPAIDOrderArrival() throws SQLException, InvalidLocationException {
    	
    	it.polito.ezshop.data.ProductType product = null;
    	boolean update = false;
   	 	boolean reset = false;
   	 	int productId = -1;
   	 	int orderId = -1;
		int failedId = -1;
   	 	
//		cannot assert that its true, because if the database is already empty reset will be equal to false
   	 	reset = it.polito.ezshop.data.model.Order.resetOrders();

//		cannot assert that its true, because if the database is already empty reset will be equal to false
		reset = ProductType.resetProductTypes();
		
		productId = ProductType.storeProductTypeInDb("test product", "036000241457", 10.00, "This is a test note.");
		assertNotEquals(productId, failedId);				
			
		update = ProductType.updatePositionInDb(productId, "123-abc-123");
		assertTrue(update);
		
		product = ProductType.getProductTypeByBarCodeFromDb("036000241457");
		assertNotNull(product);
		assertEquals(product.getLocation(), "123-abc-123");
		
		if(product != null) {
			orderId = it.polito.ezshop.data.model.Order.issueOrderInDb(product.getBarCode(), 100, product.getPricePerUnit(), 6, Order.STATUS_ISSUED);
			assertNotEquals(orderId, failedId);				
			
			if(orderId != -1) {
				update = it.polito.ezshop.data.model.Order.recordOrderArrivalInDb(orderId, null);
				assertFalse(update); //because order is not paid yet

    	    	reset = it.polito.ezshop.data.model.Order.resetOrders();
    	    	assertTrue(reset);
			}
	    	reset = ProductType.resetProductTypes();
	    	assertTrue(reset);
		}
 	}
    
    @Test
    public void testPAIDOrderArrival() throws SQLException, InvalidLocationException {
    	
    	it.polito.ezshop.data.ProductType product = null;
    	boolean update = false;
    	boolean reset = false;
    	int productId = -1;
    	int orderId = -1;
    	int failedId = -1;
    	
//		cannot assert that its true, because if the database is already empty reset will be equal to false
    	reset = it.polito.ezshop.data.model.Order.resetOrders();

//		cannot assert that its true, because if the database is already empty reset will be equal to false
    	reset = ProductType.resetProductTypes();
    	
		productId = ProductType.storeProductTypeInDb("test product", "036000241457", 10.00, "This is a test note.");
		assertNotEquals(productId, failedId);				
		
//    			update location to be able to update products quantity on arrival 
		update = ProductType.updatePositionInDb(productId, "123-abc-123");
		assertTrue(update);
		
//    			make sure that the product is added to the database and location is correct
		product = ProductType.getProductTypeByBarCodeFromDb("036000241457");
		assertNotNull(product);
		assertEquals(product.getLocation(), "123-abc-123");
		
		if(product != null) {
			orderId = it.polito.ezshop.data.model.Order.issueOrderInDb(product.getBarCode(), 100, product.getPricePerUnit(), 6, Order.STATUS_ISSUED);
			assertNotEquals(orderId, failedId);				
			
			if(orderId != -1) {
			
				update = Order.updateOrderInDb(orderId, Order.STATUS_PAID);
				assertTrue(update);
				
				update = it.polito.ezshop.data.model.Order.recordOrderArrivalInDb(orderId, null);
				assertTrue(update); //because order is paid

// 						if order if paid 
// 							update products quantity
// 							set order status to completed 
				product = ProductType.getProductTypeByBarCodeFromDb("036000241457");
    			assertNotNull(product);
    			if(product != null) {
    				assertEquals(product.getQuantity(), 100, 0);
    				
    				List<it.polito.ezshop.data.Order> orders = Order.getAllOrdersFromDb();
    				assertNotNull(orders);
    				if(orders != null) {
    					for(it.polito.ezshop.data.Order order : orders) {
    						if(order.getOrderId() == orderId) {
    							assertEquals(order.getStatus(), Order.STATUS_COMPLETED);
    							break;
    						}
    					}
    				}
    			}
    	    	
    	    	reset = it.polito.ezshop.data.model.Order.resetOrders();
    	    	assertTrue(reset);
			}
	    	reset = ProductType.resetProductTypes();
	    	assertTrue(reset);
		}
    }
    
    @Test
    public void testBalanceAndOrderDependency() throws SQLException {
    	List<BalanceOperation> balanceOperations = new ArrayList<>();
    	it.polito.ezshop.data.BalanceOperation balance = null;
    	it.polito.ezshop.data.Order order = null;
    	it.polito.ezshop.data.ProductType product = null;
    	int productId = -1;
    	int orderId = -1;
    	int failedId = -1;
    	boolean reset = false;
    	boolean update = false;
    	
//		cannot assert that its true, because if the database is already empty reset will be equal to false
    	reset = it.polito.ezshop.data.model.Order.resetOrders();

//		cannot assert that its true, because if the database is already empty reset will be equal to false
    	reset = ProductType.resetProductTypes();
    	
//		cannot assert that its true, because if the database is already empty reset will be equal to false
    	reset = BalanceOperation.resetBalanceOperations();
    	
    	productId = ProductType.storeProductTypeInDb("test product", "036000241457", 10.00, "This is a test note.");
		assertNotEquals(productId, failedId);				
			
		update = ProductType.updatePositionInDb(productId, "123-abc-123");
		assertTrue(update);
		
		product = ProductType.getProductTypeByBarCodeFromDb("036000241457");
		assertNotNull(product);
		assertEquals(product.getLocation(), "123-abc-123");
		
		if(product != null) {

			int balanceId = it.polito.ezshop.data.model.BalanceOperation.incrementCounter();
			orderId = it.polito.ezshop.data.model.Order.issueOrderInDb(product.getBarCode(), 100, product.getPricePerUnit(), balanceId, Order.STATUS_PAID);
			assertNotEquals(orderId, failedId);	
			
			if(orderId != -1) {
				balanceOperations.add(new Order(orderId, balanceId, product.getBarCode(), Order.STATUS_PAID, 100, product.getPricePerUnit(),
						BalanceOperation.STATUS_PAID, 100*product.getPricePerUnit()));
				
				List<it.polito.ezshop.data.Order> orders = Order.getAllOrdersFromDb();
				assertNotNull(orders);
				if(orders != null) {
					for(it.polito.ezshop.data.Order o : orders) {
						if(o.getOrderId() == orderId) {
							order = o;
							break;
						}
					}
				}
				assertNotNull(order);

				if(order != null) {
					for(BalanceOperation op : balanceOperations) {
						if(op.getBalanceId() == order.getBalanceId()) {
							balance = op;
							break;
						}
					}
					
					assertNotNull(balance);
					if(balance != null) assertEquals(balance.getType(), Order.NAME);
					reset = it.polito.ezshop.data.model.Order.resetOrders();
			    	assertTrue(reset);
				}
			}
			reset = ProductType.resetProductTypes();
			assertTrue(reset);
		}
    }
 
}