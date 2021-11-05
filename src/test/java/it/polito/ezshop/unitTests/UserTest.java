package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.Customer;
import it.polito.ezshop.data.model.User;

public class UserTest {

    @Test
    public void testUserInvalidSetUsername() {
        User user= new User(34,"sara","password","Cashier");
        String username= user.getUsername();
        user.setUsername("");
        assertEquals(username, user.getUsername());
    }
    
    @Test
    public void testUserNullSetUsername() {
        User user= new User(34,"sara","password","Cashier");
        String username= user.getUsername();
        user.setUsername(null);
        assertEquals(username, user.getUsername());
    }
    
    @Test
    public void testUserPositiveSetUsername() {
        User user= new User(34,"sara","password","Cashier");
        user.setUsername("saraR");
        assertEquals("saraR", user.getUsername());
    }
    
    @Test
    public void testUserInvalidSetPassword() {
        User user= new User(34,"sara","password","Cashier");
        String password= user.getPassword();
        user.setPassword("");
        assertEquals(password, user.getPassword());
    }
    
    @Test
    public void testUserNullSetPassword() {
        User user= new User(34,"sara","password","Cashier");
        String password= user.getPassword();
        user.setPassword(null);
        assertEquals(password, user.getPassword());
    }
    
    @Test
    public void testUserPositiveSetPassword() {
        User user= new User(34,"sara","password","Cashier");
        user.setPassword("passWORD");
        assertEquals("passWORD", user.getPassword());
    }

    @Test
    public void testUserNegativeSetId() {
        User user= new User(34,"sara","password","Cashier");
        Integer id = user.getId();
        user.setId(-5);
        assertEquals(id,user.getId());
        // boundary
        user.setId(-1);
        assertEquals(id,user.getId());
    }

    @Test
    public void testUserPositiveSetId() {
        User user= new User(34,"sara","password","Cashier");
        Integer id = 5;
        user.setId(5);
        assertEquals(id,user.getId());
        // boundary
        user.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, user.getId() ,1);
    }
    
    @Test
    public void testUserExistenceSetRole() {
        User user= new User(34,"sara","password","Cashier");
        String role= user.getRole();
        user.setRole("");
        assertEquals(role, user.getRole());
    }
    
    @Test
    public void testUserNullSetRole() {
        User user= new User(34,"sara","password","Cashier");
        String role= user.getRole();
        user.setRole(null);
        assertEquals(role, user.getRole());
    }
    
    @Test
    public void testUserInvalidSetRole() {
        User user= new User(34,"sara","password","Cashier");
        String role= user.getRole();
        user.setRole("Manager");
        assertEquals(role, user.getRole());
    }
    
    @Test
    public void testUserValidSetRole() {
        User user= new User(2,"sara","password","Cashier");
        user.setRole("ShopManager");
        assertEquals("Cashier", user.getRole());
        user.setRole("Administrtor");
        assertEquals("Cashier", user.getRole());
        user.setRole("Cashier");
        assertEquals("Cashier", user.getRole());
    }
    
    @Test
    public void testStoreUserInDb() throws SQLException {
    	int id = it.polito.ezshop.data.model.User.storeUserInDb("sara","password","Cashier");
		User user;
		user = it.polito.ezshop.data.model.User.getUserFromDb(id);
		assertEquals("sara",user.getUsername());
    }
    
    
    
    
    @Test
    public void testDBMethods() throws SQLException {
    	
    	//store user
    	Integer failedId1 = -1;
    	Integer id = it.polito.ezshop.data.model.User.storeUserInDb("username", "1234", User.ROLE_ADMINISTRATOR);
    	assertNotEquals(id, failedId1);
    	if(id!=-1) {
		    boolean delete = false;
		    delete = it.polito.ezshop.data.model.User.deleteUserFromDb(id);
		    assertTrue(delete);
    	}
    	
    	List<it.polito.ezshop.data.User>  users = new ArrayList<>();
    	users = it.polito.ezshop.data.model.User.getAllUsersFromDb();
    	for(it.polito.ezshop.data.User u : users) {
			assertNotEquals(u, null);
		}
    	
    	id = -1;
    	id = it.polito.ezshop.data.model.User.storeUserInDb("username", "1234", User.ROLE_ADMINISTRATOR);
    	assertNotEquals(id, failedId1);
    	if(id!= -1) {
    	boolean update = false;
    	update = it.polito.ezshop.data.model.User.updateUserRightsInDb(id, User.ROLE_CASHIER);
    	assertTrue(update);
    	}
    	
    	//Autenticate
    	User user = null;
    	user = it.polito.ezshop.data.model.User.authenticateFromDb("username", "1234");
    	assertNotEquals(user,null);
    }
}
