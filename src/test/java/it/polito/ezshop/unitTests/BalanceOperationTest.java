package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.BalanceOperation;

public class BalanceOperationTest {

	@Test 
	public void testPositiveSetBalanceId() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_UNPAID);
        Integer id = 1;
        balance.setBalanceId(1);
        assertEquals(id, balance.getBalanceId());
        // boundary
        balance.setBalanceId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, balance.getBalanceId(), 1);
    }
	
	@Test 
	public void testNegativeSetBalanceId() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_UNPAID);
        Integer mId = balance.getBalanceId();
        balance.setBalanceId(-1);
        assertEquals(mId, balance.getBalanceId());
        // boundary
        balance.setBalanceId(0);
        assertEquals(mId, balance.getBalanceId());
    }
	
	@Test
	public void testNullSetDate(){
	    BalanceOperation balance = new BalanceOperation(5, LocalDate.now(), 0.0, "SALE", "UNPAID");
	    LocalDate date= balance.getDate();
	    balance.setDate(null);
	    assertEquals(date,balance.getDate());
	}
	
	@Test
	public void testValidSetDate(){
	    BalanceOperation balance = new BalanceOperation(5, LocalDate.parse("2021-05-22"), 0.0, "SALE", "UNPAID");
	    balance.setDate(LocalDate.parse("2021-05-22"));
	    assertEquals(LocalDate.parse("2021-05-22"),balance.getDate());
	}
	
	@Test
	public void testNullSetType(){
	    BalanceOperation balance = new BalanceOperation(5,LocalDate.now(), 0.0, "SALE", "UNPAID");
	    String type= balance.getType();
	    balance.setType(null);
	    assertEquals(type, balance.getType());
	}
	
	@Test
	public void testInvalidSetType(){
	    BalanceOperation balance = new BalanceOperation(5,LocalDate.now(), 0.0, "SALE", "UNPAID");
	    String type= balance.getType();
	    balance.setType(" ");
	    assertEquals(type, balance.getType());
	}
	
	@Test
	public void testPositiveSetType(){
	    BalanceOperation balance = new BalanceOperation(5,LocalDate.now(), 0.0, "SALE", "UNPAID");
	    balance.setType("SALE");
	    assertEquals("SALE", balance.getType());
	    balance.setType("CREDIT");
	    assertEquals("CREDIT", balance.getType());
	    balance.setType("DEBIT");
	    assertEquals("DEBIT", balance.getType());
	    balance.setType("ORDER");
	    assertEquals("ORDER", balance.getType());
	    balance.setType("RETURN");
	    assertEquals("RETURN", balance.getType());
	}
	
	@Test
	public void testNullSetStatus(){
	    BalanceOperation balance = new BalanceOperation(5,LocalDate.now(), 0.0, "SALE", "UNPAID");
	    String status= balance.getStatus();
	    balance.setType(null);
	    assertEquals(status, balance.getStatus());
	}
	
	@Test
	public void testInvalidSetStatus(){
	    BalanceOperation balance = new BalanceOperation(5,LocalDate.now(), 0.0, "SALE", "UNPAID");
	    String status= balance.getStatus();
	    balance.setType(" ");
	    assertEquals(status, balance.getStatus());
	}
	
	@Test
	public void testSetStatus(){
	    BalanceOperation balanceOperation = new BalanceOperation(1, LocalDate.now(), 0.0, "SALE", "UNPAID");
	    balanceOperation.setStatus("UNPAID");
	    assertEquals("UNPAID", balanceOperation.getStatus());
	    balanceOperation.setStatus("PAID");
	    assertEquals("PAID", balanceOperation.getStatus());
	    balanceOperation.setStatus("");
	    assertEquals("PAID", balanceOperation.getStatus());
	    balanceOperation.setStatus(null);
	    assertEquals("PAID", balanceOperation.getStatus());
	}
	
	@Test
	public void testPositiveSetStatus(){
	    BalanceOperation balance = new BalanceOperation(5, LocalDate.now(), 0.0, "SALE", "UNPAID");
	    balance.setStatus("UNPAID");
	    assertEquals("UNPAID", balance.getStatus());
	    balance.setStatus("PAID");
	    assertEquals("PAID", balance.getStatus());
	}
	
	@Test
	public void testSetMoney(){
	    BalanceOperation balanceOperation = new BalanceOperation(1, LocalDate.now(), 0.0, "SALE", "UNPAID");
	    balanceOperation.setMoney(-Double.MAX_VALUE);
	    assertEquals(-Double.MAX_VALUE, balanceOperation.getMoney(), 0.1);
	    balanceOperation.setMoney(0);
	    assertEquals(0, balanceOperation.getMoney(), 0.1);
	    balanceOperation.setMoney(Double.MAX_VALUE);
	    assertEquals(Double.MAX_VALUE, balanceOperation.getMoney(), 0.1);
	}	
	
    @Test
    public void testDBMethods() throws SQLException {
    	
    	int id = -1;
    	int failedId = -1;
    	id = it.polito.ezshop.data.model.BalanceOperation.getLastIdFromBalanceOperations();
    	id += 1;
    	if(id != -1) {
	    	id = it.polito.ezshop.data.model.BalanceOperation.storeBalanceInDb(id, 100.00, BalanceOperation.TYPE_SALE);
			assertNotEquals(id, failedId);
    	}
		
		List<BalanceOperation> operations = it.polito.ezshop.data.model.BalanceOperation.getCreditsAndDebitsFromDb();
		assertNotEquals(operations.size(), 0);
		
		boolean deleted = it.polito.ezshop.data.model.BalanceOperation.deleteBalanceFromDb(id);
    	assertTrue(deleted);

    	id += 1;
		id = it.polito.ezshop.data.model.BalanceOperation.storeBalanceInDb(id, 100.00, BalanceOperation.TYPE_SALE);
		assertNotEquals(id, failedId);
		
    	boolean reset = it.polito.ezshop.data.model.BalanceOperation.resetBalanceOperations();
    	assertTrue(reset);
    	
    
    }
}
