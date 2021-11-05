package it.polito.ezshop.data.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreditCardCircuit {
	
	public static final String CREDIT_CARD_FILE = "src/main/java/it/polito/ezshop/util/creditcards.txt";
	private Map<String, Double> creditCardMoney;
	private String fileName;

	

	public CreditCardCircuit(String fileName) {
		this.fileName = fileName;
		creditCardMoney = new HashMap<>();

		if (fileName != null && !fileName.isEmpty()) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(fileName));
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
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Double> getCreditCardMoney() {
		return creditCardMoney;
	}

	public void setCreditCardMoney(Map<String, Double> creditCardMoney) {
		this.creditCardMoney = creditCardMoney;
	}





	public boolean isValid(String creditCard) {
		if (creditCard != null && !creditCard.isEmpty()) {
			return creditCardMoney.containsKey(creditCard);
		}
		return false;
	}

	
	public boolean pay(String creditCard, double money) {
		if (isValid(creditCard)) {
			double amount = creditCardMoney.get(creditCard);
			if (amount >= money) {
				creditCardMoney.put(creditCard, amount - money);
				
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean validateCreditCard(String creditCard) {
		if (creditCard == null || creditCard.isEmpty())
			return false;
		int sum = 0;
		boolean oddIndex = false;
		for (int i = creditCard.length() - 1; i >= 0; i--) {
			int d = creditCard.charAt(i) - '0';
			if (oddIndex)
				d = d * 2;
			sum += d / 10;
			sum += d % 10;

			oddIndex = !oddIndex;
		}
		return (sum % 10 == 0);
	}


}
