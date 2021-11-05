package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.polito.ezshop.data.model.CreditCardCircuit;

public class CreditCardCircuitTest {
	
	@Test
	public void testNullLoadFile() {
		CreditCardCircuit creditCardCircuit = new CreditCardCircuit(null);
		assertNull(creditCardCircuit.getFileName());
		assertEquals(creditCardCircuit.getCreditCardMoney(), new HashMap<>());
	}
	
	@Test
	public void testEmptyLoadFile() {
		CreditCardCircuit creditCardCircuit = new CreditCardCircuit("");
		assertEquals(creditCardCircuit.getFileName(), "");
		assertEquals(creditCardCircuit.getCreditCardMoney(), new HashMap<>());
	}
	
	@Test
	public void testValidLoadFile() {
		Map<String, Double> creditCardMoney = new HashMap<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(CreditCardCircuit.CREDIT_CARD_FILE));
			String line = reader.readLine();
			while (line != null) {
				if (line.charAt(0) != '#') {
					String[] card = line.split(";");
					if (card.length == 2)
						creditCardMoney.put(card[0], Double.valueOf(card[1]));
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException ignored) {
		}
		
		CreditCardCircuit creditCardCircuit = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
		assertEquals(creditCardCircuit.getFileName(), CreditCardCircuit.CREDIT_CARD_FILE);
		assertEquals(creditCardCircuit.getCreditCardMoney(), creditCardMoney);
	}

    @Test
    public void testPositiveValidateCreditCard() {
        CreditCardCircuit card = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertTrue(card.validateCreditCard("4485370086510891"));
    }

    @Test
    public void testNegativeValidateCreditCard() {
        CreditCardCircuit card = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.validateCreditCard(null));
    }

    @Test
    public void testEmptyValidateCreditCard() {
        CreditCardCircuit creditCardCircuit = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(creditCardCircuit.validateCreditCard(""));
    }

    @Test
    public void testInvalidLengthValidateCreditCard() {
        CreditCardCircuit creditCardCircuit = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(creditCardCircuit.validateCreditCard("1"));
    }
    
    @Test
    public void testNullIsValid() {
        CreditCardCircuit card = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.isValid(null));
    }
	
    @Test
    public void testEmptyIsValid() {
        CreditCardCircuit card = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.isValid(""));
    }
	
    @Test
    public void testNegativeIsValid() {
        CreditCardCircuit card = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.isValid("4485370086510890"));
    }

    @Test
    public void testPositiveIsValid() {
        CreditCardCircuit card =new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertTrue(card.isValid("4485370086510891"));
    }

    @Test
    public void testPositivePay() {
        CreditCardCircuit card =new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertTrue(card.pay("4485370086510891", 50.50));

    }

    @Test
    public void testNegativePay() {
        CreditCardCircuit card =new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.pay("4485370086510891", 200));
    }

    @Test
    public void testInvalidPay() {
        CreditCardCircuit card =new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertFalse(card.pay(null, 60));
    }

    @Test
    public void testInvalidMoneyPay() {
        CreditCardCircuit card =new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
        assertTrue(card.pay("4485370086510891", -58.2));
    }
}
