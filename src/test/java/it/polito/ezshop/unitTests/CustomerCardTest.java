package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

public class CustomerCardTest {

    @Test
    public void testDBMethods() throws SQLException {
    	
		String cardNumber = it.polito.ezshop.data.model.CustomerCard.storeCardInDb();
		assertNotNull(cardNumber);
		
		boolean update = it.polito.ezshop.data.model.CustomerCard.modifyPointsOnCardInDb(cardNumber, 10000);
		assertTrue(update);
    }
}
