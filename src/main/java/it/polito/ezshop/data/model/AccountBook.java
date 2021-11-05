package it.polito.ezshop.data.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountBook {
	
    private double balance;
    public List<BalanceOperation> balanceOperations;

    public AccountBook() {
        balanceOperations = new ArrayList<BalanceOperation>();
        balance = 0.0;
    }
    
    public boolean addBalanceOperation(BalanceOperation balance) {
    	if(balance != null) {
    		balanceOperations.add(balance);
    		if(balance.getStatus().equalsIgnoreCase(BalanceOperation.STATUS_PAID) 
    				|| balance.getStatus().equalsIgnoreCase(Order.STATUS_PAID)) {
    			try {
					BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), balance.getType());
				} catch (SQLException e) {
			    	return false;
		    	}
    		}
    		return true;
    	}
    	return false;
    }
    
    public boolean removeBalanceOperation(BalanceOperation balance) {
    	if(balance != null) {
    		balanceOperations.remove(balance);
    		if(balance.getStatus().equalsIgnoreCase(BalanceOperation.STATUS_PAID)) {
    			try {
    				BalanceOperation.deleteBalanceFromDb(balance.getBalanceId());
    			} catch (SQLException e) {
    				return false;
    			}
    		}
    		return true;
    	}
    	return false;
    }

    public boolean recordBalanceUpdate(double toBeAdded) {
    	if(balance + toBeAdded >= 0) {
    		balance = balance + toBeAdded;
    		return true;
    	}
    	return false;
    }
    
    public List<it.polito.ezshop.data.BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) {
    	List<it.polito.ezshop.data.BalanceOperation> filteredOperations = new ArrayList<>();
    	if(balanceOperations != null) {
	//    	considering the first load
	    	if(from == null && to == null) {
				try {
					balanceOperations = BalanceOperation.getCreditsAndDebitsFromDb();
					computeBalance(balanceOperations);
				} catch (SQLException e) {
				}
	    	}
	    	for(BalanceOperation balance : balanceOperations) {
	    		if(balance.getStatus().equalsIgnoreCase(BalanceOperation.STATUS_PAID)) {
	    			if((from == null || balance.getDate().compareTo(from) >= 0) && (to == null || balance.getDate().compareTo(to) < 0)) 
	    				filteredOperations.add(balance);
	    		}
	    	}
    	}
    	return filteredOperations;
    }
    
    public boolean computeBalance(List<BalanceOperation> operations) {
    	double balance = 0.0;
    	if(operations != null) {
    		for(BalanceOperation operation : operations) {
	    		if(operation.getStatus().equalsIgnoreCase(BalanceOperation.STATUS_PAID)) {
	    			if(operation.getType().equalsIgnoreCase(BalanceOperation.TYPE_ORDER) || operation.getType().equalsIgnoreCase(BalanceOperation.TYPE_RETURN) 
	    					|| operation.getType().equalsIgnoreCase(BalanceOperation.TYPE_DEBIT)) 
	    				balance -= operation.getMoney();
	        		else if(operation.getType().equalsIgnoreCase(BalanceOperation.TYPE_CREDIT) || operation.getType().equalsIgnoreCase(BalanceOperation.TYPE_SALE))
	        			balance +=operation.getMoney();
	    		}
    		}
        	setBalance(balance);
        	return true;
    	}
    	return false;
    }
    
    public void resetBalance() {
    	this.balance = 0.0;
    }

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<BalanceOperation> getBalanceOperations() {
		return balanceOperations;
	}

	public void setBalanceOperations(List<BalanceOperation> balanceOperations) {
		if(balanceOperations != null)
			this.balanceOperations = balanceOperations;
	}
}
