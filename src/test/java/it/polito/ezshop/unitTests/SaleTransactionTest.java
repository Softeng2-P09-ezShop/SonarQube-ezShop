package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import it.polito.ezshop.data.model.SaleTransaction;

public class SaleTransactionTest {
	
	@Test
	public void testNegativeSetTicketNumber() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		Integer id = saleTransaction.getTicketNumber();
		saleTransaction.setTicketNumber(-1);
	    assertEquals(id, saleTransaction.getTicketNumber());
	    // boundary
	    saleTransaction.setTicketNumber(0);
	    assertEquals(id, saleTransaction.getTicketNumber());
	}
	
	@Test
	public void testPositiveSetTicketNumber() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		Integer id = 1;
		saleTransaction.setTicketNumber(1);
	    assertEquals(id, saleTransaction.getTicketNumber());
	    // boundary
	    saleTransaction.setTicketNumber(Integer.MAX_VALUE);
	    assertEquals(Integer.MAX_VALUE, saleTransaction.getTicketNumber(), 1);
	}
	
	@Test
	public void testNullSetTicketNumber() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		Integer id = saleTransaction.getTicketNumber();
	    saleTransaction.setTicketNumber(null);
	    assertEquals(id, saleTransaction.getTicketNumber());
	}
	
	@Test
	public void testNegativeSetBalanceId() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		Integer balance_id = saleTransaction.getBalanceId();
		saleTransaction.setBalanceId(-3);
		assertEquals(balance_id, saleTransaction.getBalanceId());
  	}
	  
	@Test
	public void testPositiveSetBalanceId() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		Integer balance_id = 1;
        saleTransaction.setBalanceId(1);
        assertEquals(balance_id, saleTransaction.getBalanceId());
        // boundary
        saleTransaction.setBalanceId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, saleTransaction.getBalanceId(), 1);
	}
    
	@Test
	public void testValidSetStatus() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		String status = saleTransaction.getStatus();
		saleTransaction.setStatus(SaleTransaction.STATUS_OPENED);
		assertEquals(status, saleTransaction.getStatus());
	}
	
	@Test
	public void testInvalidSetStatus() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		String status = saleTransaction.getStatus();
		saleTransaction.setStatus("AAA");
		assertEquals(status, saleTransaction.getStatus());
	}
    
	@Test
	public void testNullSetStatus() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		String status = saleTransaction.getStatus();
		saleTransaction.setStatus(null);
		assertEquals(status, saleTransaction.getStatus());
	}

    @Test
    public void testNegativeSetPrice() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, -1.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		double price = saleTransaction.getPrice();
        saleTransaction.setPrice(-1.00);
        assertEquals(price, saleTransaction.getPrice(), 0);
    }

    @Test
    public void testPositiveSetPrice() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 1.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
    	saleTransaction.setPrice(1.00);
        double price = 1.00;
        assertEquals(price, saleTransaction.getPrice(), 0);
        //boundary
        saleTransaction.setPrice(0.0001);
        price = 0.0001;
        assertEquals(price, saleTransaction.getPrice(), 0);
    }

    @Test
    public void testInvalidSetDiscountRate() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 1.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		double discountRate = saleTransaction.getDiscountRate();
        saleTransaction.setDiscountRate(-1.00);
        assertEquals(discountRate, saleTransaction.getDiscountRate(), 0);
    }
    
    @Test
    public void testInvalid2SetDiscountRate() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.50, SaleTransaction.STATUS_OPENED, 1.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
    	double discountRate = saleTransaction.getDiscountRate();
    	saleTransaction.setDiscountRate(1.00);
    	assertEquals(discountRate, saleTransaction.getDiscountRate(), 0);
    }

    @Test
    public void testValidSetDiscountRate() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, null, 0.00, SaleTransaction.STATUS_OPENED, 1.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
    	saleTransaction.setDiscountRate(0.50);
        double discountRate = 0.50;
        assertEquals(discountRate, saleTransaction.getDiscountRate(), 0);
        //boundary
        saleTransaction.setDiscountRate(0.01);
        discountRate = 0.01;
        assertEquals(discountRate, saleTransaction.getDiscountRate(), 0);
    }

    @Test
    public void testDBMethods() throws SQLException {
		int id = it.polito.ezshop.data.model.SaleTransaction.storeSaleInDb(100, 0.00, 100.00, SaleTransaction.STATUS_OPENED);
		int failedId = -1;
		assertNotEquals(id, failedId);
		
		boolean update = it.polito.ezshop.data.model.SaleTransaction.updateSaleStatusInDb(id, SaleTransaction.STATUS_PAYED);
		assertTrue(update);
		
		SaleTransaction saleTransaction = it.polito.ezshop.data.model.SaleTransaction.getSaleTransactionFromDb(id);
    	assertNotEquals(saleTransaction, null);
    	
    	id = it.polito.ezshop.data.model.SaleTransaction.getLastIdFromSaleTransactions();
    	assertNotEquals(id, failedId);
    
		boolean deleted = it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(id);
    	assertTrue(deleted);
    	
    	id = it.polito.ezshop.data.model.SaleTransaction.storeSaleInDb(100, 0.00, 100.00, SaleTransaction.STATUS_OPENED);
		assertNotEquals(id, failedId);
		
    	boolean reset = it.polito.ezshop.data.model.SaleTransaction.resetSaleTransaction();
    	assertTrue(reset);
    }
    
    @Test 
    public void testComputePointsForSale() {
    	SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		assertEquals(saleTransaction.computePointsForSale(), 1);
    }
}
