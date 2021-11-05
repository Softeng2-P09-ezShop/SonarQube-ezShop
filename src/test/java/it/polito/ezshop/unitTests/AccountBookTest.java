package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.model.AccountBook;

public class AccountBookTest {

	@Test 
	public void testPositiveRecordBalanceUpdate() {
		AccountBook accountBook = new AccountBook();
		accountBook.setBalance(10.00);
		assertTrue(accountBook.recordBalanceUpdate(10.00));
		assertEquals(accountBook.getBalance(), 10.00+10.00, 0);
	}
	
	@Test 
	public void testNegativeRecordBalanceUpdate() {
		AccountBook accountBook = new AccountBook();
		accountBook.setBalance(10.00);
		assertTrue(accountBook.recordBalanceUpdate(-2.00));
		assertEquals(accountBook.getBalance(), 10.00-2.00, 0);
	}
	
	@Test 
	public void testInvalidRecordBalanceUpdate() {
		AccountBook accountBook = new AccountBook();
		accountBook.setBalance(10.00);
		assertFalse(accountBook.recordBalanceUpdate(-20.00));
	}
	
	@Test 
	public void testResetBalance() {
		AccountBook accountBook = new AccountBook();
		accountBook.setBalance(100.00);
		assertEquals(accountBook.getBalance(), 100.00, 0);
		accountBook.resetBalance();
		assertEquals(accountBook.getBalance(), 0.00, 0);
	}
}
