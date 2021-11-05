package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.BalanceOperation;
import it.polito.ezshop.data.model.SaleTransaction;
import it.polito.ezshop.data.model.TicketEntry;

public class SaleTransactionTest {
    
	@Test
	public void testBalanceOperationSaleTransactionConstructor() {
		List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
		BalanceOperation balance = new SaleTransaction(1, 1, null, 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		operations.add(balance);
		
		assertNotNull(balance);
		assertNotEquals(operations.size(), 0);
		assertEquals(balance.getBalanceId(), operations.get(0).getBalanceId());
		assertEquals(balance.getDate(), operations.get(0).getDate());
		assertEquals(balance.getMoney(), operations.get(0).getMoney(), 0);
		assertEquals(balance.getType(), operations.get(0).getType());
		assertEquals(balance.getStatus(), operations.get(0).getStatus());
	}
	
	@Test
	public void testNullSetEntries() {
		List<it.polito.ezshop.data.TicketEntry> tickets = new ArrayList<>();
		tickets.add(new TicketEntry("036000241457", "test product", 1, 10.00, 0.00));
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, tickets, 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.setEntries(null);
		assertNotNull(saleTransaction.getEntries());
		assertEquals(saleTransaction.getEntries(), tickets);
	}
	
	@Test
	public void testEmptyListSetEntries() {
		List<it.polito.ezshop.data.TicketEntry> emptytickets = new ArrayList<>();
		List<it.polito.ezshop.data.TicketEntry> tickets = new ArrayList<>();
		tickets.add(new TicketEntry("036000241457", "test product", 1, 10.00, 0.00));
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, tickets, 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.setEntries(emptytickets);
		assertNotEquals(saleTransaction.getEntries().size(), 0);
		assertEquals(saleTransaction.getEntries(), tickets);
	}
	
	@Test
	public void testValidSetEntries() {
		List<it.polito.ezshop.data.TicketEntry> tickets = new ArrayList<>();
		tickets.add(new TicketEntry("036000241457", "test product", 1, 10.00, 0.00));
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, tickets, 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.setEntries(tickets);
		assertNotEquals(saleTransaction.getEntries().size(), 0);
		assertEquals(saleTransaction.getEntries(), tickets);
	}
	
	@Test
	public void testNullBarcodeAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale(null, "test product", 10.00, 1);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}

	@Test
	public void testNullDescriptionAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("036000241457", null, 10.00, 1);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}
	
	@Test
	public void testZeroPricePerUnitAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("036000241457", "test product", 0, 1);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}
	
	@Test
	public void testZeroAmountAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("036000241457", "test product", 10.00, 0);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}
	
	@Test
	public void testInvalidBarcodeAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("", "test product", 10.00, 1);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}
	
	@Test
	public void testInvalidDescriptionAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("036000241457", "", 10.00, 1);
		assertEquals(saleTransaction.getEntries().size(), 0);
	}
	
	@Test
	public void testValidAddProductToSale() {
		SaleTransaction saleTransaction = new SaleTransaction(1, 1, new ArrayList<it.polito.ezshop.data.TicketEntry>(), 0.00,
				SaleTransaction.STATUS_OPENED, 10.00, SaleTransaction.TYPE_SALE, SaleTransaction.STATUS_UNPAID);
		saleTransaction.addProductToSale("036000241457", "test product", 10.00, 1);
		assertEquals(saleTransaction.getEntries().size(), 1);
		assertEquals(saleTransaction.getEntries().get(0).getAmount(), 1);
		assertEquals(saleTransaction.getEntries().get(0).getBarCode(), "036000241457");
		assertEquals(saleTransaction.getEntries().get(0).getPricePerUnit(), 10.00, 0);
		assertEquals(saleTransaction.getEntries().get(0).getProductDescription(), "test product");
	}
}