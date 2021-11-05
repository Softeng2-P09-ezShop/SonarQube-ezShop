package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import it.polito.ezshop.data.model.Customer;

public class CustomerTest {
	
    @Test
    public void testInvalidSetCustomername() {
    	
        Customer customer= new Customer("Takla","customerCard",2,20);
        String customerName= customer.getCustomerName();
        customer.setCustomerName("");
        assertEquals(customerName, customer.getCustomerName());
    }
    
    @Test
    public void testNullSetCustomername() {
    
        Customer customer= new Customer("Takla","customerCard",2,20);
        String customerName= customer.getCustomerName();
        customer.setCustomerName(null);
        assertEquals(customerName, customer.getCustomerName());
    }

    @Test
    public void testCreateAndReadCustomerStoreUserInDb() throws SQLException {
		Customer.resetCustomer();
		Integer id = it.polito.ezshop.data.model.Customer.storeCustomerInDb("holaaa");
		Customer customer = null;		
		customer = it.polito.ezshop.data.model.Customer.getCustomerFromDb(id);
		assertEquals(id,customer.getId());
    }
    
 	@Test
 	public void testReadAllCustomer() throws SQLException {
 		List<it.polito.ezshop.data.Customer> customers= null;
		Customer.resetCustomer();
		it.polito.ezshop.data.model.Customer.storeCustomerInDb("c1");
		it.polito.ezshop.data.model.Customer.storeCustomerInDb("c2");
		it.polito.ezshop.data.model.Customer.storeCustomerInDb("holaaa");
		customers = it.polito.ezshop.data.model.Customer.getAllCustomersFromDb();
		assertEquals(3, customers.size());
 		assertTrue(customers.get(0).getCustomerName().equals("c1"));
 	    assertTrue(customers.get(1).getCustomerName().equals("c2"));
 	}

 	@Test
 	public void testUpdateCustomer() throws SQLException {
 		boolean result= false ;
		Customer.resetCustomer();
		int id = it.polito.ezshop.data.model.Customer.storeCustomerInDb("c1");
		result = it.polito.ezshop.data.model.Customer.modifyCustomerInDb(id,"c4","");
 		if (result) {
 			it.polito.ezshop.data.Customer customer=null;
			customer = it.polito.ezshop.data.model.Customer.getCustomerFromDb(id);
	 		assertTrue(customer.getCustomerName().equals("c4"));
 		}
	}
 	
 	
 	
	// class variable
	final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	

	final java.util.Random rand = new java.util.Random();
	

	// consider using a Map<String,Boolean> to say whether the identifier is being used or not 
	final Set<String> identifiers = new HashSet<String>();


	public String randomIdentifier() {
	    StringBuilder builder = new StringBuilder();
	    while(builder.toString().length() == 0) {
	        int length = rand.nextInt(5)+5;
	        for(int i = 0; i < length; i++) {
	            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
	        }
	        if(identifiers.contains(builder.toString())) {
	            builder = new StringBuilder();
	        }
	    }
	    return builder.toString();
	}

 	
 	
    @Test
    public void testDBMethods() throws SQLException {
    	
		
		List<it.polito.ezshop.data.Customer> customers = new ArrayList<>();
		customers = it.polito.ezshop.data.model.Customer.getAllCustomersFromDb();
		for(it.polito.ezshop.data.Customer c : customers) {
			assertNotEquals(c, null);
		}
		

		it.polito.ezshop.data.Customer cust = null;
		cust = it.polito.ezshop.data.model.Customer.getCustomerFromDb(8);
		assertNull(cust);
		if(cust==null) {
			int id = -1;
			id = it.polito.ezshop.data.model.Customer.storeCustomerInDb(randomIdentifier());
			int failedId = -1;
			assertNotEquals(id, failedId);
		
			boolean attached = it.polito.ezshop.data.model.Customer.attachCardToCustomerInDb("3886061237", id);
			assertTrue(attached);
		
			Customer c = it.polito.ezshop.data.model.Customer.getCustomerFromDb(id);
			if(c!=null) {
					boolean update3 = it.polito.ezshop.data.model.Customer.deleteCustomerFromDb(c.getId());
					assertTrue(update3);
			}
		
			boolean result=false;
			result = it.polito.ezshop.data.model.Customer.modifyCustomerInDb(id,c.getCustomerName(),"1234567845");
		}
//		else {
//			boolean update = false;
//			
//			update = it.polito.ezshop.model.Customer.modifyCustomerInDb(cust.getId(), randomIdentifier(), "5082775062");
//			assertTrue(update);
//		
//			boolean update3 = it.polito.ezshop.model.Customer.deleteCustomerFromDb(cust.getId());
//			assertTrue(update3);
//		}
    }
}
