package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.model.ProductType;
import it.polito.ezshop.data.model.SaleTransaction;
import it.polito.ezshop.data.model.TicketEntry;

public class TicketEntryTest {



    @Test
    public void testNegativeSetAmount() {
    	 TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        int amount = ticket.getAmount();
        ticket.setAmount(-5);
        assertEquals(amount, ticket.getAmount());
        // boundary
       
        assertEquals(amount, ticket.getAmount());
    }

    @Test
    public void testPositiveSetQuantity() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
    	 ticket.setAmount(20);
        int amount = 20;
        assertEquals(amount, ticket.getAmount());
        // boundary
        ticket.setAmount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, ticket.getAmount(),1);


    }

    @Test
    public void testInvalidSetQuantity() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        int amount = ticket.getAmount();
        ticket.setAmount(0);
        assertEquals(amount, ticket.getAmount());
    }

    @Test
    public void testNegativeSetPricePerUnit() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        Double price = Double.valueOf(ticket.getPricePerUnit());
        ticket.setPricePerUnit(-0.50);
        assertEquals(price, Double.valueOf(ticket.getPricePerUnit()));
        // boundary
        ticket.setPricePerUnit(-0.0001);
        assertEquals(price, Double.valueOf(ticket.getPricePerUnit()));
    }

    @Test
    public void testInvalidSetPricePerUnit() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
	        Double price = ticket.getPricePerUnit();
	        ticket.setPricePerUnit(0.0);
	        assertEquals(price, Double.valueOf(ticket.getPricePerUnit()));
	       
	   
    }

    @Test
    public void testPositiveSetPricePerUnit() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
	       
	        ticket.setPricePerUnit(2);
	        Double price = Double.valueOf(2);
	        assertEquals(price,Double.valueOf(ticket.getPricePerUnit()));
	        
	        //boundary
	        ticket.setPricePerUnit(0.0001);
	        Double priceunit = 0.0001;
	        assertEquals(priceunit, Double.valueOf(ticket.getPricePerUnit()));
	 
    }

    @Test
    public void testInvalidSetBarCode() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        String barcode = ticket.getBarCode();
        ticket.setBarCode("");
        assertEquals(barcode, ticket.getBarCode());
    }

    @Test
    public void testNullSetBarCode() {

    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        String barcode = ticket.getBarCode();
        ticket.setBarCode(null);
        assertEquals(barcode, ticket.getBarCode());
    }

    @Test
    public void testPositiveSetBarCode() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        ticket.setBarCode("78515420");
        assertEquals("78515420", ticket.getBarCode());
    }

    @Test
    public void testInvalidSetProductDescription() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        String description = ticket.getProductDescription();
        ticket.setProductDescription("");
        assertEquals(description, ticket.getProductDescription());
    }

    @Test
    public void testNullSetProductDescription() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        String description = ticket.getProductDescription();
        ticket.setProductDescription(null);
        assertEquals(description, ticket.getProductDescription());
    }

    @Test
    public void testPositiveSetProductDescription() {
    	TicketEntry ticket = new TicketEntry("9330462119319","description", 20, 10, 2.0);
        ticket.setProductDescription("yellow");
        assertEquals("yellow", ticket.getProductDescription());
    }

	
    @Test
    public void testDBMethods() throws SQLException {
    	Integer failedId = -1;
    	//Store ticket entry
    	Integer id = it.polito.ezshop.data.model.TicketEntry.storeTicketEntryInDb(22, "9330462119319", 2.0, 5, 10, "description");
    	assertNotEquals(id, failedId);
    	
    	//update amount
    	boolean update =it.polito.ezshop.data.model.TicketEntry.updateAmountInDb(22, "9330462119319",100);
    	assertTrue(update);
    	
    
    }
  
    


}
