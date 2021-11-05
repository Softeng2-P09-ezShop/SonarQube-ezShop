package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.polito.ezshop.data.model.ReturnTransaction;

public class ReturnTransactionTest {

	@Test
    public void testNegativeSetId() {
        ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer id = returnTransaction.getId();
		returnTransaction.setId(-1);
        assertEquals(id, returnTransaction.getId());
        // boundary
        returnTransaction.setId(0);
        assertEquals(id, returnTransaction.getId());
    }

    @Test
    public void testPositiveSetId() {
    	ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
        Integer id = 5;
        returnTransaction.setId(5);
        assertEquals(id, returnTransaction.getId());
        // boundary
        returnTransaction.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, returnTransaction.getId(), 1);
    }

    @Test
    public void testNullSetId() {
    	ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
        Integer id = returnTransaction.getId();
        returnTransaction.setId(null);
        assertEquals(id, returnTransaction.getId());
    }

	@Test
	public void testNegativeSetTicketNumber() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer id = returnTransaction.getTicketNumber();
		returnTransaction.setTicketNumber(-1);
	    assertEquals(id, returnTransaction.getTicketNumber());
	    // boundary
	    returnTransaction.setTicketNumber(0);
	    assertEquals(id, returnTransaction.getTicketNumber());
	}
	
	@Test
	public void testPositiveSetTicketNumber() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer id = 1;
		returnTransaction.setTicketNumber(1);
	    assertEquals(id, returnTransaction.getTicketNumber());
	    // boundary
	    returnTransaction.setTicketNumber(Integer.MAX_VALUE);
	    assertEquals(Integer.MAX_VALUE, returnTransaction.getTicketNumber(), 1);
	}
	
	@Test
	public void testNullSetTicketNumber() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer id = returnTransaction.getTicketNumber();
		returnTransaction.setTicketNumber(null);
	    assertEquals(id, returnTransaction.getTicketNumber());
	}
	
	@Test
	public void testNegativeSetBalanceId() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer balance_id = returnTransaction.getBalanceId();
		returnTransaction.setBalanceId(-3);
		assertEquals(balance_id, returnTransaction.getBalanceId());
  	}
	  
	@Test
	public void testPositiveSetBalanceId() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Integer balance_id = 1;
		returnTransaction.setBalanceId(1);
        assertEquals(balance_id, returnTransaction.getBalanceId());
        // boundary
        returnTransaction.setBalanceId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, returnTransaction.getBalanceId(), 1);
	}
	  
	@Test
	public void testValidSetProducts() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.setProducts(products);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testInvalidSetProducts() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.setProducts(products);
		returnTransaction.setProducts(null);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testNullCodeReturnProduct() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.returnProduct("test", 1);
		returnTransaction.returnProduct(null, 1);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testEmptyCodeReturnProduct() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.returnProduct("test", 1);
		returnTransaction.returnProduct("", 1);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testInvalidAmountReturnProduct() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.returnProduct("test", 1);
		returnTransaction.returnProduct("test", 0);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testValidReturnProduct() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		Map<String, Integer> products = new HashMap<>();
		products.put("test", 1);
		returnTransaction.returnProduct("test", 1);
		assertEquals(products, returnTransaction.getProducts());	
	}
	
	@Test
	public void testTrueIsCommitted() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		boolean isCommitted = true;
		returnTransaction.setCommitted(true);
		assertEquals(isCommitted, returnTransaction.isCommitted());	
	}
	
	@Test
	public void testFalseIsCommitted() {
		ReturnTransaction returnTransaction = new ReturnTransaction(1, 1, 1, true, ReturnTransaction.STATUS_UNPAID, 1.00, ReturnTransaction.STATUS_PAID);
		boolean isCommitted = false;
		returnTransaction.setCommitted(false);
		assertEquals(isCommitted, returnTransaction.isCommitted());	
	}

    @Test
    public void testDBMethods() throws SQLException {
		int id = it.polito.ezshop.data.model.ReturnTransaction.storeReturnInDb(1, "", 0, 1,  ReturnTransaction.STATUS_UNPAID);
		int failedId = -1;
		assertNotEquals(id, failedId);
		
		boolean update = it.polito.ezshop.data.model.ReturnTransaction.updateReturnStatusInDb(id, "", false, ReturnTransaction.STATUS_UNPAID);
		assertTrue(update);
		
		boolean deleted = it.polito.ezshop.data.model.ReturnTransaction.deleteReturnTransactionFromDb(id);
    	assertTrue(deleted);

		id = it.polito.ezshop.data.model.ReturnTransaction.storeReturnInDb(1, "", 0, 1, ReturnTransaction.STATUS_UNPAID);
		assertNotEquals(id, failedId);
		
    	boolean reset = it.polito.ezshop.data.model.ReturnTransaction.resetReturnTransaction();
    	assertTrue(reset);
    }
}
