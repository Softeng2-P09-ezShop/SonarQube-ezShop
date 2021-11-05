package it.polito.ezshop;

import java.sql.SQLException;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.util.DatabaseConnection;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args) {
    	try {
			DatabaseConnection.createNewDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);
    }
}
