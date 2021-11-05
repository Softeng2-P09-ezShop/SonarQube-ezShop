package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.data.model.Order;
import it.polito.ezshop.data.model.ProductType;
import it.polito.ezshop.data.model.ReturnTransaction;
import it.polito.ezshop.data.model.User;
import it.polito.ezshop.exceptions.InvalidLocationException;


public class OrderTest {

	  @Test
	  public void testNullIssueOrderInDb() throws SQLException {	
		  int id = it.polito.ezshop.data.model.Order.issueOrderInDb("010003004", 100, 5,4, "ORDER");
		  
		  Order order= null;
		  
		  for (it.polito.ezshop.data.Order o : it.polito.ezshop.data.model.Order.getAllOrdersFromDb())
			  if(o.getOrderId()== id)
				  order = (Order) o;
		  assertEquals("010003004",order.getProductCode());
	  }	
	  
	  
	  @Test
	  public void testNegativeSetBalanceId() {
		Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
		Integer balance_id = order.getBalanceId();
		order.setBalanceId(-3);
		assertEquals(balance_id, order.getBalanceId());
	  }
	  
	  @Test
	  public void testPositiveSetBalanceId() {
		
		Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
        Integer balance_id = 5;
        order.setBalanceId(5);
        assertEquals(balance_id, order.getBalanceId());
        // boundary
       order.setBalanceId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, order.getBalanceId() ,1);
	  }
		
		
		  
     @Test
	  public void testInvalidSetProductCode() {
			Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
			String productCode = order.getProductCode();
			order.setProductCode("");
			assertEquals(productCode, order.getProductCode());
		  
	  }
     
     @Test
	  public void testNullSetProductCode() {
    		Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
			String productCode = order.getProductCode();
			order.setProductCode(null);
			assertEquals(productCode, order.getProductCode());
		  
	  }
     
     
     @Test
	  public void testInvalidSetPricePerUnit() {
			
		
			
			Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);

	        Double price = Double.valueOf(order.getPricePerUnit());
	        order.setPricePerUnit(-0.50);
	        assertEquals(price, Double.valueOf(order.getPricePerUnit()));
	        // boundary
	        order.setPricePerUnit(-0.0001);
	        assertEquals(price, Double.valueOf(order.getPricePerUnit()));
	    }
		  
	  
     
     @Test
	  public void testNullSetPricePerUnit() {
    		Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);

	        Double price = Double.valueOf(order.getPricePerUnit());
	        order.setPricePerUnit(0.0);
	        assertEquals(price, Double.valueOf(order.getPricePerUnit()));
	        // boundary
	        order.setPricePerUnit(-0.0001);
	        assertEquals(price, Double.valueOf(order.getPricePerUnit()));
		  
	  }

     
     @Test
	  public void testNegativeSetQuantity() {
    	 
    	 Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);

         Integer quantity = Integer.valueOf(order.getQuantity());
         order.setQuantity(-5);
         assertEquals(quantity, Integer.valueOf(order.getQuantity()));
       
		  
	  }
     
     @Test
	  public void testNullSetQuantity() {
    	 Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);

         Integer quantity = Integer.valueOf(order.getQuantity());
         order.setQuantity(0);
         assertEquals(quantity, Integer.valueOf(order.getQuantity()));
		  
	  }
     
     @Test
	  public void testPositiveSetQuantity() {
			Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
			Integer balance_id = order.getBalanceId();
			order.setQuantity(20);
			assertEquals(balance_id, order.getBalanceId());
		  
	  }
     
     @Test
	  public void testInvalidSetStatus() {
    	 Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
			String status = order.getStatus();
			order.setStatus("Ehsan");
			assertEquals(status, order.getStatus());
		  
	  }
     
     @Test
	  public void testNullSetStatus() {
    	 Order order = new Order(22, 10,"048001208025", "PAYED", 20, 5, "DEBIT", 200);
			String status = order.getStatus();
			order.setStatus(null);
			assertEquals(status, order.getStatus());
		  
	  }
     
     
     
     
     @Test
     public void testDBMethods() throws SQLException, InvalidLocationException {
    	 
    	 boolean update = false;
    	 update = it.polito.ezshop.data.model.Order.resetOrders();
    	 
    	 assertTrue(update);
    	 
    	
    	 int id = it.polito.ezshop.data.model.Order.issueOrderInDb("9330462119319",100, 5, 6,  Order.STATUS_ISSUED);
    	 int failedId = -1;
    	 assertNotEquals(id, failedId);
    	 Order order = null;
    	 order = it.polito.ezshop.data.model.Order.payOrderInDb(id);
    	 assertEquals(order.getStatus(), Order.STATUS_PAID);
 		
    	 update = false;
 		
 		 update = it.polito.ezshop.data.model.Order.updateOrderInDb(id,Order.STATUS_ISSUED);
 		
 		 assertNotEquals(update, failedId);
 		 update = false;
 		
 		 update = it.polito.ezshop.data.model.Order.recordOrderArrivalInDb(id, null);
 		 assertNotEquals(update, failedId);	
     }
}
