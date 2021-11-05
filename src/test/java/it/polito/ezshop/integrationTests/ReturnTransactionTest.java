package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.BalanceOperation;
import it.polito.ezshop.data.model.ReturnTransaction;

public class ReturnTransactionTest {
	

	
	@Test
	  public void testReturnTransactionConstructor() {
	    List<BalanceOperation> operations = new ArrayList<BalanceOperation>();
	    BalanceOperation balance = new ReturnTransaction(1, 1, 1,false, "UNPAID", 10.00, BalanceOperation.STATUS_UNPAID);
	    operations.add(balance);
	    
	    assertNotNull(balance);
	    assertNotEquals(operations.size(), 0);
	    assertEquals(balance.getBalanceId(), operations.get(0).getBalanceId());
	    assertEquals(balance.getDate(), operations.get(0).getDate());
	    assertEquals(balance.getMoney(), operations.get(0).getMoney(), 0);
	    assertEquals(balance.getType(), operations.get(0).getType());
	    assertEquals(balance.getStatus(), operations.get(0).getStatus());
	  }
    
    
}