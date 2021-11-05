package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.AccountBook;
import it.polito.ezshop.data.model.BalanceOperation;

public class AccountBookTest {
    
	@Test 
	public void testValidUNPAIDAddBalanceOperation() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_UNPAID);
		AccountBook accountBook = new AccountBook();
		assertTrue(accountBook.addBalanceOperation(balance));
    }
    
	@Test 
	public void testValidPAIDAddBalanceOperation() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_PAID);
		AccountBook accountBook = new AccountBook();
		assertTrue(accountBook.addBalanceOperation(balance));
    }

	@Test 
	public void testNullAddBalanceOperation() {
		BalanceOperation balance = null;
		AccountBook accountBook = new AccountBook();
		assertFalse(accountBook.addBalanceOperation(balance));
    }

	@Test 
	public void testValidUNPAIDRemoveBalanceOperation() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_UNPAID);
		AccountBook accountBook = new AccountBook();
		accountBook.addBalanceOperation(balance);        
		assertTrue(accountBook.removeBalanceOperation(balance));
	}
	
	@Test 
	public void testValidPAIDRemoveBalanceOperation() {
		BalanceOperation balance = new BalanceOperation(1, LocalDate.now(), 0.0, "RETURN", BalanceOperation.STATUS_PAID);
		AccountBook accountBook = new AccountBook();
		accountBook.addBalanceOperation(balance);        
		assertTrue(accountBook.removeBalanceOperation(balance));
	}

	@Test 
	public void testNullRemoveBalanceOperation() {
		BalanceOperation balance = null;
		AccountBook accountBook = new AccountBook();
		assertFalse(accountBook.removeBalanceOperation(balance));        
	}
	
	@Test
	public void testNullComputeBalance() {
		AccountBook accountBook = new AccountBook();
		assertFalse(accountBook.computeBalance(null));
	}
	
	@Test
	public void testValidComputeBalance() {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations.add(new BalanceOperation(1, LocalDate.now(), 1000.00, BalanceOperation.TYPE_SALE, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(2, LocalDate.now(), 1000.00, BalanceOperation.TYPE_CREDIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(3, LocalDate.now(), 100.00, BalanceOperation.TYPE_ORDER, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(4, LocalDate.now(), 100.00, BalanceOperation.TYPE_DEBIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(5, LocalDate.now(), 100.00, BalanceOperation.TYPE_RETURN, BalanceOperation.STATUS_PAID));
 		accountBook.setBalanceOperations(operations);
		assertTrue(accountBook.computeBalance(accountBook.getBalanceOperations()));
		assertEquals(1000.00+1000.00-100.00-100.00-100.00, accountBook.getBalance(), 0.0);
	}
	
	@Test 
	public void testNullSetBalanceOperations() {
		AccountBook accountBook = new AccountBook();
		accountBook.setBalanceOperations(null);
		assertEquals(accountBook.getBalanceOperations(), new ArrayList<BalanceOperation>());
	}
	
	@Test 
	public void testValidSetBalanceOperations() {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations.add(new BalanceOperation(1, LocalDate.now(), 1000.00, BalanceOperation.TYPE_SALE, BalanceOperation.STATUS_PAID));
		accountBook.setBalanceOperations(operations);
		assertEquals(accountBook.getBalanceOperations(), operations);
	}
	
	@Test 
	public void testNullFROMTOGetCreditsAndDebits() throws SQLException {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations = BalanceOperation.getCreditsAndDebitsFromDb();
 		accountBook.setBalanceOperations(operations);
 		assertEquals(accountBook.getCreditsAndDebits(null, null).size(), operations.size());
	}
	
	@Test 
	public void testNullFROMGetCreditsAndDebits() {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations.add(new BalanceOperation(1, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_SALE, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(2, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_CREDIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(3, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_ORDER, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(4, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_DEBIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(5, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_RETURN, BalanceOperation.STATUS_PAID));
		accountBook.setBalanceOperations(operations);
		assertEquals(accountBook.getCreditsAndDebits(null, LocalDate.parse("2021-05-23")), operations);
	}
	
	@Test 
	public void testNullTOGetCreditsAndDebits() {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations.add(new BalanceOperation(1, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_SALE, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(2, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_CREDIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(3, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_ORDER, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(4, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_DEBIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(5, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_RETURN, BalanceOperation.STATUS_PAID));
		accountBook.setBalanceOperations(operations);
		assertEquals(accountBook.getCreditsAndDebits(LocalDate.parse("2021-05-20"), null), operations);
	}
	
	@Test 
	public void testFROMTOGetCreditsAndDebits() {
		AccountBook accountBook = new AccountBook();
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		operations.add(new BalanceOperation(1, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_SALE, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(2, LocalDate.parse("2021-05-22"), 1000.00, BalanceOperation.TYPE_CREDIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(3, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_ORDER, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(4, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_DEBIT, BalanceOperation.STATUS_PAID));
		operations.add(new BalanceOperation(5, LocalDate.parse("2021-05-22"), 100.00, BalanceOperation.TYPE_RETURN, BalanceOperation.STATUS_PAID));
		accountBook.setBalanceOperations(operations);
		assertEquals(accountBook.getCreditsAndDebits(LocalDate.parse("2021-05-20"), LocalDate.parse("2021-05-23")), operations);
	}
}
