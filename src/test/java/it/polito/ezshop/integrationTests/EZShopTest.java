package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.data.model.Product;
import it.polito.ezshop.data.model.ReturnTransaction;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EZShopTest {

	private EZShop ezShop;

	@Before
	public void clearDatabaseBefore() throws SQLException {

		ezShop = new EZShop();
		it.polito.ezshop.data.model.ProductType.resetProductTypes();
		it.polito.ezshop.data.model.SaleTransaction.resetSaleTransaction();
		it.polito.ezshop.data.model.BalanceOperation.resetBalanceOperations();
		it.polito.ezshop.data.model.Order.resetOrders();
		it.polito.ezshop.data.model.ReturnTransaction.resetReturnTransaction();
		it.polito.ezshop.data.model.User.resetUsers();
		it.polito.ezshop.data.model.Customer.resetCustomer();
		it.polito.ezshop.data.model.CustomerCard.resetCustomerCards();
		it.polito.ezshop.data.model.TicketEntry.resetTicketEntries();
		it.polito.ezshop.data.model.Product.resetProducts();
	}

	@After
	public void clearDatabaseAfter() throws SQLException {

		ezShop = new EZShop();
		it.polito.ezshop.data.model.ProductType.resetProductTypes();
		it.polito.ezshop.data.model.SaleTransaction.resetSaleTransaction();
		it.polito.ezshop.data.model.BalanceOperation.resetBalanceOperations();
		it.polito.ezshop.data.model.Order.resetOrders();
		it.polito.ezshop.data.model.ReturnTransaction.resetReturnTransaction();
		it.polito.ezshop.data.model.User.resetUsers();
		it.polito.ezshop.data.model.Customer.resetCustomer();
		it.polito.ezshop.data.model.CustomerCard.resetCustomerCards();
		it.polito.ezshop.data.model.TicketEntry.resetTicketEntries();
		it.polito.ezshop.data.model.Product.resetProducts();
	}

//	scenario USER:
//		2.1 - create and define rights 
//		2.2 - delete user
//		2.3 - modify user's rights 
	@Test
	public void testCreateUser()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {

		Integer user = ezShop.createUser("username1", "password", "ShopManager");
		assertNotNull(user);
		assertNotEquals(user, -1, 0);

		login("Administrator");

		List<User> users = ezShop.getAllUsers();
		assertTrue(users.size() > 0);

		boolean available = false;
		for (User u : users) {
			if (u.getId().equals(user)) {
				available = true;
				break;
			}
		}
		assertTrue(available);

		Integer user2 = ezShop.createUser("username1", "password", "Cashier");
		assertEquals(-1, user2, 0);
	}
	
	@Test(expected = InvalidUsernameException.class)
	public void testInvalidUsernameCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		ezShop.createUser(null, "password", "ShopManager");
	}

	@Test
	public void testInvalidPasswordCreateUser() throws InvalidPasswordException {
		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.createUser("username1", null, "ShopManager");
		});
	}
	
	@Test
	public void testInvalidRoleCreateUser() throws InvalidRoleException {
		Assert.assertThrows(InvalidRoleException.class, () -> {
			ezShop.createUser("username1", "password", null);
		});
	}

	@Test
	public void testDeleteUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException,
			InvalidUserIdException, UnauthorizedException {

		login("Administrator");

		Integer user = ezShop.createUser("username1", "password", "Cashier");
		boolean done = ezShop.deleteUser(user);
		assertTrue(done);
		if (done) {
			User u = ezShop.getUser(user);
			assertNull(u);
			done = ezShop.deleteUser(user);
			assertFalse(done);
		}
	}

	@Test
	public void testInvalidIdDeleteUser() throws InvalidUserIdException {
		login("Administrator");

		Assert.assertThrows(InvalidUserIdException.class, () -> {
			ezShop.deleteUser(null);
		});	
	}

	@Test
	public void testUnauthorizedDeleteUser() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteUser(1);
		});	
	}

	@Test
	public void testGetAllUsers()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {

		login("Administrator");

		ezShop.createUser("username1", "p", "Cashier");
		ezShop.createUser("username2", "p", "Administrator");
		ezShop.createUser("username3", "p", "Cashier");
		ezShop.createUser("username4", "p", "Cashier");
		ezShop.createUser("username5", "p", "Administrator");
		ezShop.createUser("username6", "p", "ShopManager");
		ezShop.createUser("username7", "p", "Cashier");

		List<User> allUsers = ezShop.getAllUsers();
		assertEquals(allUsers.size(), 8);
	}
	
	@Test
	public void testUnauthorizedGetAllUsers() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getAllUsers();
		});	
	}

	@Test
	public void testGetUser() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException,
			InvalidUserIdException, UnauthorizedException {
		login("Administrator");

		Integer user = ezShop.createUser("username1", "p", "Cashier");
		assertNotEquals(user, -1, 0);

		User u = ezShop.getUser(user);
		assertNotNull(u);

		assertEquals(user, u.getId());
		assertEquals("username1", u.getUsername());
		assertEquals("p", u.getPassword());
		assertEquals("Cashier", u.getRole());
	}
	
	@Test
	public void testInvalidIdGetUser() throws InvalidUserIdException {
		login("Administrator");

		Assert.assertThrows(InvalidUserIdException.class, () -> {
			ezShop.getUser(null);
		});	
	}
	
	@Test
	public void testUnauthorizedGetUser() throws UnauthorizedException {
		
		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getUser(1);
		});	
	}

	@Test
	public void testUpdateUserRole() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException,
			InvalidUserIdException, UnauthorizedException {
		login("Administrator");
		Integer user = ezShop.createUser("username1", "p", "Cashier");
		assertNotEquals(user, -1, 0);

		boolean done = ezShop.updateUserRights(user, "ShopManager");
		assertTrue(done);

		User user1 = ezShop.getUser(user);
		assertEquals("ShopManager", user1.getRole());

		done = ezShop.updateUserRights(100, "ShopManager");
		assertFalse(done);
	}
	
	@Test
	public void testInvalidIdUpdateUserRole() throws InvalidUserIdException {
		login("Administrator");

		Assert.assertThrows(InvalidUserIdException.class, () -> {
			ezShop.updateUserRights(null, "ShopManager");
		});	
	}
	
	@Test
	public void testInvalidRoleUpdateUserRole() throws InvalidRoleException {
		login("Administrator");

		Assert.assertThrows(InvalidRoleException.class, () -> {
			ezShop.updateUserRights(1, "asdasd");
		});	
	}
	
	@Test
	public void testUnauthorizedUpdateUserRole() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.updateUserRights(1, "ShopManager");
		});	
	}

	private void login(String role) {
		try {
			ezShop.createUser("username", "password", role);
			EZShop.loggedUser = new it.polito.ezshop.data.model.User("username", "password", role);
		} catch (Exception e) {
		}
	}

	@Test
	public void testUsernameNullLogin() throws InvalidPasswordException, InvalidUsernameException {

		Assert.assertThrows(InvalidUsernameException.class, () -> {
			ezShop.login(null, "password");
		});
	}

	@Test
	public void testUsernameEmptyLogin() throws InvalidPasswordException, InvalidUsernameException {
		Assert.assertThrows(InvalidUsernameException.class, () -> {
			ezShop.login("", "password");
		});
	}

	@Test
	public void testPasswordNullLogin() throws InvalidPasswordException, InvalidUsernameException {
		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.login("username", null);
		});
	}

	@Test
	public void testPasswordEmptyLogin() throws InvalidPasswordException, InvalidUsernameException {

		Assert.assertThrows(InvalidPasswordException.class, () -> {
			ezShop.login("username", "");
		});
	}

	@Test
	public void testWrongPasswordLogin()
			throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
		ezShop.createUser("username", "password", "Cashier");

		User login = ezShop.login("username", "wrongPassword");
		assertNull(login);
	}

	@Test
	public void testLogin() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
		Integer userId = ezShop.createUser("username", "password", "Cashier");
		assertNotEquals(userId, -1, 0);

		User user = ezShop.login("username", "password");
		assertNotNull(user);
	}

	@Test
	public void testLogout()
			throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException {

		ezShop.createUser("username", "password", "Cashier");
		ezShop.login("username", "password");
		ezShop.logout();

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.startSaleTransaction();
		});

	}
	
	@Test
	public void testUnauthorizedLogout() throws UnauthorizedException {
		assertFalse(ezShop.logout());
	}

//    scenario PRODUCT:
//    	1.1 - create product type
//    	1.2 - modify product location 
//    	1.3 - Modify product type price per unit    
	@Test
	public void testCreateProduct() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException {
		login("ShopManager");
		Integer productId = ezShop.createProductType("test description", "036000241457", 10.00, "this is a test note");
		assertNotEquals(productId, -1, 0);

		ProductType productType = ezShop.getProductTypeByBarCode("036000241457");
		assertNotNull(productType);
		if (productType != null) {
			assertEquals("test description", productType.getProductDescription());
			assertEquals(10.00, productType.getPricePerUnit(), 0);
			assertEquals("this is a test note", productType.getNote());
		}
		Integer productType2 = ezShop.createProductType("test description", "036000241457", 10.00,
				"this is a test note");
		assertEquals(-1, productType2, 0);
	}
	
	@Test
	public void testInvalidDescriptionCreateProduct() throws InvalidProductDescriptionException {
		login("Administrator");

		Assert.assertThrows(InvalidProductDescriptionException.class, () -> {
			ezShop.createProductType(null, "036000241457", 10.00, "this is a test note");
		});	
	}
	
	@Test
	public void testInvalidPricePerUnitCreateProduct() throws InvalidPricePerUnitException {
		login("Administrator");

		Assert.assertThrows(InvalidPricePerUnitException.class, () -> {
			ezShop.createProductType("test description", "036000241457", -10.00, "this is a test note");
		});	
	}
	
	@Test
	public void testUnauthorizedCreateProduct() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.createProductType("test description", "036000241457", 10.00, "this is a test note");
		});	
	}

	@Test
	public void testUpdateLocation()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {

		login("ShopManager");

		Integer productId = ezShop.createProductType("tesst description", "036000241457", 100.00,
				"this is a test note2");
		assertNotEquals(productId, -1, 0);

		Assert.assertThrows(InvalidProductCodeException.class, () -> {
			ezShop.createProductType("test description1", "123036000241457", 11.00, "this is a test note2");
		});

		if (productId > 0) {
			assertTrue(ezShop.updatePosition(productId, "123-abc-123"));

			assertTrue(ezShop.updatePosition(productId, ""));

			ProductType product = ezShop.getProductTypeByBarCode("036000241457");
			assertNotNull(product);
			if (product != null)
				assertEquals(product.getLocation(), "");

			assertTrue(ezShop.updatePosition(productId, "321-cba-321"));

			product = ezShop.getProductTypeByBarCode("036000241457");
			assertEquals("321-cba-321", product.getLocation());
		}
	}
	
	@Test
	public void testInvalidIdUpdateLocation() throws InvalidProductIdException {
		login("Administrator");

		Assert.assertThrows(InvalidProductIdException.class, () -> {
			ezShop.updatePosition(null, "321-cba-321");
		});	
	}
	
	@Test
	public void testInvalidLocationUpdateLocation() throws InvalidLocationException {
		login("Administrator");

		Assert.assertThrows(InvalidLocationException.class, () -> {
			ezShop.updatePosition(1, "a");
		});	
	}
	
	@Test
	public void testUnauthorizedUpdateLocation() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.updatePosition(1, "123-abc-123");
		});	
	}

	@Test
	public void testUpdateProduct() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
		login("ShopManager");
		Integer productId = ezShop.createProductType("test description", "036000241457", 10.00, "this is a test note");
		assertNotEquals(productId, -1, 0);
		if (productId != -1) {
			assertTrue(ezShop.updateProduct(productId, "description test", "036000241457", 2.99, "note test "));

		}

		ProductType product = ezShop.getProductTypeByBarCode("036000241457");
		assertNotNull(product);
		if (product != null) {
			assertEquals("description test", product.getProductDescription());
			assertEquals("036000241457", product.getBarCode());
			assertEquals(2.99, product.getPricePerUnit(), 0.1);
			assertEquals("note test ", product.getNote());
		}
//      update with same attribute values
		assertTrue(ezShop.updateProduct(productId, "description test", "036000241457", 2.99, "note test "));
//      id not available in table 
		assertFalse(ezShop.updateProduct(100, "banana", "048001208025", 2.99, "note2"));
	}
	
	@Test
	public void testInvalidIdUpdateProduct() throws InvalidProductIdException {
		login("Administrator");

		Assert.assertThrows(InvalidProductIdException.class, () -> {
			ezShop.updateProduct(null, "banana", "048001208025", 2.99, "note2");
		});	
	}
	
	@Test
	public void testInvalidDescriptionUpdateProduct() throws InvalidProductDescriptionException {
		login("Administrator");
		
		Assert.assertThrows(InvalidProductDescriptionException.class, () -> {
			ezShop.updateProduct(100, null, "048001208025", 2.99, "note2");
		});	
	}
	
	@Test
	public void testInvalidPricePerUnitUpdateProduct() throws InvalidPricePerUnitException {
		login("Administrator");
		
		Assert.assertThrows(InvalidPricePerUnitException.class, () -> {
			ezShop.updateProduct(100, "banana", "048001208025", -2.99, "note2");
		});	
	}
	
	@Test
	public void testUnauthorizedUpdateProduct() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.updateProduct(1, "description test", "036000241457", 2.99, "note test ");
		});	
	}

	@Test
	public void testDeleteProductType() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
		login("ShopManager");
		Integer productid = ezShop.createProductType("test description", "036000241457", 10.00, "onote");
		assertNotEquals(productid, -1, 0);
		if (productid != -1) {
			assertTrue(ezShop.deleteProductType(productid));
			ProductType product = ezShop.getProductTypeByBarCode("036000241457");
			assertNull(product);
			assertFalse(ezShop.deleteProductType(productid));
		}
	}
	
	@Test
	public void testUnauthorizedDeleteProductType() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteProductType(1);
		});	
	}

	@Test
	public void testGetAllProductTypes() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException {
		login("ShopManager");
		List<ProductType> products = new ArrayList<ProductType>();
		products = ezShop.getAllProductTypes();
		assertEquals(products.size(), 0);

		Integer productId1 = ezShop.createProductType("test1", "048001208025", 10.50, "this is test product 1");
		Integer productId2 = ezShop.createProductType("test2", "036000241457", 20.00, "this is test product 2");

		assertNotEquals(productId1, -1, 0);
		assertNotEquals(productId2, -1, 0);
		login("Cashier");
		products = ezShop.getAllProductTypes();
		assertEquals(2, products.size());
	}
	
	@Test
	public void testUnauthorizedGetAllProductTypes() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getAllProductTypes();
		});	
	}

	@Test
	public void testGetProductTypesByDescription() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException {
		login("ShopManager");
		ezShop.createProductType("test1", "036000241457", 10.00, "note1");
		ezShop.createProductType("test2", "048001208025", 20.00, "note2");

		List<ProductType> products = ezShop.getProductTypesByDescription(null);
		assertNotNull(products);
		assertEquals(2, products.size());

		products = ezShop.getProductTypesByDescription("");
		assertNotNull(products);
		assertEquals(2, products.size());

		List<ProductType> filteredList = ezShop.getProductTypesByDescription("1");
		assertEquals(1, filteredList.size());
		assertEquals("test1", filteredList.get(0).getProductDescription());

		List<ProductType> filteredList2 = ezShop.getProductTypesByDescription("2");
		assertEquals(1, filteredList2.size());
		assertEquals("test2", filteredList2.get(0).getProductDescription());

		List<ProductType> emptyList = ezShop.getProductTypesByDescription("qqq");
		assertEquals(0, emptyList.size());
	}
	
	@Test
	public void testGetProductTypesByBarCode() throws UnauthorizedException, InvalidProductDescriptionException,
	InvalidPricePerUnitException, InvalidProductCodeException {
		login("ShopManager");
		ezShop.createProductType("test1", "036000241457", 10.00, "note1");
		ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		
		List<ProductType> products = ezShop.getProductTypesByDescription(null);
		assertNotNull(products);
		assertEquals(2, products.size());
		
		ProductType product = ezShop.getProductTypeByBarCode("048001208025");
		assertNotNull(product);
		assertEquals("048001208025", product.getBarCode());
	}
	
	@Test
	public void testUnauthorizedGetProductTypesByDescription() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getProductTypesByDescription("qqq");
		});	
	}

	@Test
	public void testUpdateQuantity()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidProductIdException, InvalidLocationException {
		login("ShopManager");
		Integer productId2 = ezShop.createProductType("test2", "048001208025", 20.00, "note2");

		ezShop.updatePosition(productId2, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId2, 10));
		assertEquals(10, ezShop.getProductTypeByBarCode("048001208025").getQuantity(), 0);
		assertTrue(ezShop.updateQuantity(productId2, 5));
		assertEquals(15, ezShop.getProductTypeByBarCode("048001208025").getQuantity(), 0);

		assertFalse(ezShop.updateQuantity(100, 10));
		assertFalse(ezShop.updateQuantity(productId2, -20));
	}
	
	@Test
	public void testUnauthorizedUpdateQuantity() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.updateQuantity(100, 10);
		});	
	}

//    scenario ORDER:
//    	3.1 - Order of product type X issued
//    	3.2 - Order of product type X payed
//    	3.3 - Record order of product type X arrival
	@Test
	public void testIssueOrder() throws UnauthorizedException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException {
		login("ShopManager");
		ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		Integer orderId = ezShop.issueOrder("048001208025", 10, 20.00);
		assertNotEquals(orderId, -1, 0);

		List<Order> orders = ezShop.getAllOrders();
		assertNotNull(orders);
		assertEquals(1, orders.size());
		assertEquals(it.polito.ezshop.data.model.Order.STATUS_ISSUED, orders.get(0).getStatus());

		Assert.assertThrows(InvalidProductCodeException.class, () -> {
			ezShop.issueOrder("04800120802", 10, 20.00);
		});

		orderId = ezShop.issueOrder("048001208025", 10, 1.99);
		assertNotEquals(orderId, -1, 0);
	}
	
	@Test
	public void testUnauthorizedIssueOrder() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.issueOrder("048001208025", 10, 20.00);
		});	
	}

	@Test
	public void testPayOrderFor() throws UnauthorizedException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException {
		login("ShopManager");

		ezShop.recordBalanceUpdate(100.0);
		ezShop.createProductType("test2", "048001208025", 20.00, "note2");

		Integer orderId = ezShop.payOrderFor("048001208025", 1, 20.00);
		assertNotEquals(orderId, -1, 0);

		assertEquals(100 - 1 * 20.00, ezShop.computeBalance(), 0);
		List<Order> orders = ezShop.getAllOrders();
		assertNotNull(orders);
		assertEquals(1, orders.size());
		assertEquals(it.polito.ezshop.data.model.Order.STATUS_PAID, orders.get(0).getStatus());

		assertEquals(-1, ezShop.payOrderFor("036000241457", 10, 1.99).intValue());
	}
	
	@Test
	public void testUnauthorizedPayOrderFor() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.payOrderFor("048001208025", 1, 20.00);
		});	
	}

	@Test
	public void testPayOrder()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidQuantityException, InvalidOrderIdException {
		login("ShopManager");

		ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		Integer orderId = ezShop.issueOrder("048001208025", 10, 20.00);
		assertNotEquals(orderId, -1, 0);

		List<Order> orders = ezShop.getAllOrders();
		assertNotNull(orders);
		assertEquals(1, orders.size());
		assertEquals(it.polito.ezshop.data.model.Order.STATUS_ISSUED, orders.get(0).getStatus());

		ezShop.recordBalanceUpdate(1000);

		assertTrue(ezShop.payOrder(orderId));
		assertEquals(1000 - 10 * 20.00, ezShop.computeBalance(), 0);
		orders = ezShop.getAllOrders();
		assertEquals(1, orders.size());
		assertEquals(it.polito.ezshop.data.model.Order.STATUS_PAID, orders.get(0).getStatus());

		assertFalse(ezShop.payOrder(100));
	}
	
	@Test
	public void testUnauthorizedPayOrder() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.payOrder(1);
		});	
	}

	@Test
	public void testRecordOrderArrival() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException,
			InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
		login("ShopManager");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertTrue(ezShop.updatePosition(productId, "123-abc-123"));

		ezShop.recordBalanceUpdate(1000);

		Integer orderId = ezShop.payOrderFor("048001208025", 10, 20.00);
		assertNotEquals(orderId, -1, 0);

		boolean done = ezShop.recordOrderArrival(orderId);
		assertTrue(done);
		if (done) {
			List<Order> orders = ezShop.getAllOrders();
			assertNotNull(orders);
			assertEquals(1, orders.size());
			assertEquals(it.polito.ezshop.data.model.Order.STATUS_COMPLETED, orders.get(0).getStatus());

			ProductType product = ezShop.getProductTypeByBarCode("048001208025");
			assertEquals(product.getQuantity(), 10, 0);
		}
	}
	
	@Test
	public void testUnauthorizedRecordOrderArrival() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.recordOrderArrival(1);
		});	
	}

	@Test
	public void testGetAllOrders() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		login("ShopManager");
		
		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertTrue(ezShop.updatePosition(productId, "123-abc-123"));

		ezShop.recordBalanceUpdate(1000);

		Integer orderId = ezShop.payOrderFor("048001208025", 10, 20.00);
		assertNotEquals(orderId, -1, 0);

		List<Order> orders = new ArrayList<Order>();
		orders = ezShop.getAllOrders();
		assertEquals(orders.size(), 1);
	}
	
	@Test
	public void testUnauthorizedGetAllOrders() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getAllOrders();
		});	
	}

	@Test
	public void testReset() throws UnauthorizedException {
		login("Administrator");
		ezShop.reset();
		List<Order> orders = new ArrayList<>();
		List<ProductType> products = new ArrayList<>();
		List<BalanceOperation> operations = new ArrayList<>();
		orders = ezShop.getAllOrders();
		assertEquals(orders.size(), 0);

		products = ezShop.getAllProductTypes();
		assertEquals(products.size(), 0);

		operations = ezShop.getCreditsAndDebits(null, null);
		assertEquals(operations.size(), 0);
	}

	@Test
	public void testDefineCustomer()
			throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		login("Cashier");
		Integer customerId1 = ezShop.defineCustomer("takla");
		Integer customerId2 = ezShop.defineCustomer("ehsan");
		assertNotEquals(customerId1, -1, 0);
		assertNotEquals(customerId2, -1, 0);

		Customer customer1 = ezShop.getCustomer(customerId1);
		Customer customer2 = ezShop.getCustomer(customerId2);
		assertEquals(customer1.getCustomerName(), "takla");
		assertEquals(customer2.getCustomerName(), "ehsan");
	}
	
	@Test
	public void testUnauthorizedDefineCustomer() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.defineCustomer("takla");
		});	
	}

    @Test
    public void testModifyCustomer() throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        login("Cashier");
        Integer customerId1 = ezShop.defineCustomer("takla");
        Integer customerId2 = ezShop.defineCustomer("ehsan");
        assertNotEquals(customerId1, -1, 0);
        assertNotEquals(customerId2, -1, 0);
        
        String card1 = ezShop.createCard();
        
        boolean success = ezShop.modifyCustomer(customerId1, "takla", card1);
        assertTrue(success);
        if(success) {
        	Customer customer = ezShop.getCustomer(customerId1);
        	assertNotNull(customer);
        	
        	assertEquals(customer.getCustomerName(), "takla");
        	assertEquals(customer.getCustomerCard(), card1);
        }
     
    }
	
	@Test
	public void testUnauthorizedModifyCustomer() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.modifyCustomer(1, "takla", ezShop.createCard());
		});	
	}

	@Test
	public void testDeleteCustomer()
			throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		login("Cashier");
		Integer customerId1 = ezShop.defineCustomer("takla");
		assertNotEquals(customerId1, -1, 0);
		boolean success = ezShop.deleteCustomer(customerId1);
		assertTrue(success);
		if (success)
			assertFalse(ezShop.deleteCustomer(customerId1));
	}
	
	@Test
	public void testUnauthorizedDeleteCustomer() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteCustomer(1);
		});	
	}

	@Test
	public void testGetCustomer()
			throws InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		login("Cashier");
		Integer customerId1 = ezShop.defineCustomer("takla");
		assertNotEquals(customerId1, -1, 0);

		Customer customer = ezShop.getCustomer(customerId1);
		assertNotNull(customer);

		assertEquals(customer.getCustomerName(), "takla");
	}
	
	@Test
	public void testUnauthorizedGetCustomer() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getCustomer(1);
		});	
	}

	@Test
	public void testGetAllCustomers() throws InvalidCustomerNameException, UnauthorizedException,
			InvalidCustomerCardException, InvalidCustomerIdException {
		login("Cashier");
		Integer customerId1 = ezShop.defineCustomer("takla");
		Integer customerId2 = ezShop.defineCustomer("ehsan");
		assertNotEquals(customerId1, -1, 0);
		assertNotEquals(customerId2, -1, 0);

		String card1 = ezShop.createCard();

		boolean success = ezShop.modifyCustomer(customerId1, "takla", card1);
		assertTrue(success);
		if (success) {
			List<Customer> customers = ezShop.getAllCustomers();
			assertEquals(2, customers.size());
		}
	}
	
	@Test
	public void testUnauthorizedGetAllCustomers() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getAllCustomers();
		});	
	}

	@Test
	public void testAttachCardToCustomer() throws InvalidCustomerIdException, InvalidCustomerCardException,
			UnauthorizedException, InvalidCustomerNameException {
		login("Administrator");
		Integer customerId1 = ezShop.defineCustomer("takla");
		assertNotEquals(customerId1, -1, 0);

		String card1 = ezShop.createCard();

		boolean success = ezShop.attachCardToCustomer(card1, customerId1);
		assertTrue(success);
	}
	
	@Test
	public void testUnauthorizedAttachCardToCustomer() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.attachCardToCustomer(ezShop.createCard(), 2);
		});	
	}

	@Test
	public void testModifyPointsOnCard() throws InvalidCustomerIdException, InvalidCustomerCardException,
			UnauthorizedException, InvalidCustomerNameException {
		login("Administrator");
		Integer customerId1 = ezShop.defineCustomer("takla");
		assertNotEquals(customerId1, -1, 0);

		String card1 = ezShop.createCard();

		boolean success = ezShop.attachCardToCustomer(card1, customerId1);
		assertTrue(success);

		success = ezShop.modifyPointsOnCard(card1, 10000);
		assertTrue(success);
	}
	
	@Test
	public void testUnauthorizedModifyPointsOnCard() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.modifyPointsOnCard(ezShop.createCard(), 10000);
		});	
	}

	// Scenario Sale transaction
	// 6-1 Sale of product type X completed
	// 6-2 Sale of product type X with product discount
	// 6-3 Sale of product type X with sale discount
	// 6-4 Sale of product type X with Loyalty Card update
	// 6-5 Sale of product type X cancelled
	// 6-6 Sale of product type X completed (Cash)

	@Test
	public void testStartSaleTransaction()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		login("Cashier");

		Integer id1 = ezShop.startSaleTransaction();

		assertNotEquals(-1, id1.intValue(), 0);

	}
	
	@Test
	public void testUnauthorizedStartSaleTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.startSaleTransaction();
		});	
	}

	@Test
	public void testAddProductToSale() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidProductIdException, InvalidLocationException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		assertTrue(ezShop.addProductToSale(saleId, "048001208025", 2));
		it.polito.ezshop.data.ProductType p = null;
		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
		assertEquals("they should be equal", 100 - 2, p.getQuantity().intValue());
		assertTrue(ezShop.addProductToSale(saleId, "048001208025", 2));
		assertEquals(100 - 2 - 2, p.getQuantity().intValue());
	}
	
	@Test
	public void testUnauthorizedAddProductToSale() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.addProductToSale(1, "048001208025", 2);
		});	
	}

	@Test
	public void testDeleteProductFromSale() throws InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
			InvalidTransactionIdException, InvalidQuantityException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		assertTrue(ezShop.addProductToSale(saleId, "048001208025", 2));
		it.polito.ezshop.data.ProductType p = null;
		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
		assertEquals("they should be equal", 100 - 2, p.getQuantity().intValue());

		assertTrue(ezShop.deleteProductFromSale(saleId, "048001208025", 2));

		assertTrue(ezShop.currentSaleTransaction.getEntries().isEmpty());

		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
//		assertEquals("they should be equal", 100, p.getQuantity().intValue());
	}
	
	@Test
	public void testUnauthorizedDeleteProductFromSale() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteProductFromSale(1, "048001208025", 2);
		});	
	}

	@Test
	public void testApplyDiscountRateToProduct()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidDiscountRateException {
		login("ShopManager");
		Integer productType = ezShop.createProductType("p1", "048001208025", 1.99, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 10);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 2);

		assertTrue(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "1234567890128", 0.2));
		assertFalse(ezShop.applyDiscountRateToProduct(100, "048001208025", 0.2));
	}
	
	@Test
	public void testUnauthorizedApplyDiscountRateToProduct() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.applyDiscountRateToProduct(1, "048001208025", 0.2);
		});	
	}

	@Test
	public void testApplyDiscountRateToSale() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException,
			InvalidQuantityException, InvalidTransactionIdException, InvalidLocationException,
			InvalidDiscountRateException, InvalidPaymentException {
		login("Administrator");
		Integer productType = ezShop.createProductType("p1", "048001208025", 1.99, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 10);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 2);

		assertTrue(ezShop.applyDiscountRateToSale(saleId, 0.2));
		assertFalse(ezShop.applyDiscountRateToSale(100, 0.2));
		ezShop.endSaleTransaction(saleId);
		assertTrue(ezShop.applyDiscountRateToSale(saleId, 0.2));
		ezShop.receiveCashPayment(saleId, 100);
		// assertFalse(ezShop.applyDiscountRateToSale(saleId, 0.2));
	}
	
	@Test
	public void testUnauthorizedApplyDiscountRateToSale() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.applyDiscountRateToSale(1, 0.2);
		});	
	}

	@Test
	public void testComputePointsForSale() throws UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException,
			InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException {
		login("ShopManager");
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.89, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertEquals(6, ezShop.computePointsForSale(saleId));
		assertEquals(-1, ezShop.computePointsForSale(100));
	}
	
	@Test
	public void testUnauthorizedComputePointsForSale() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.computePointsForSale(1);
		});	
	}

	@Test
	public void testEndSaleTransaction()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidDiscountRateException, InvalidPaymentException {
		login("ShopManager");

		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);

		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
	}
	
	@Test
	public void testRevertEndSaleTransaction()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidDiscountRateException, InvalidPaymentException, SQLException {
		login("ShopManager");
		
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		
		Integer saleId = ezShop.startSaleTransaction();
		
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);
		it.polito.ezshop.data.model.SaleTransaction.storeSaleInDb(saleId, 0.2, ezShop.currentSaleTransaction.getPrice(),
				it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED);
		assertFalse(ezShop.endSaleTransaction(saleId));
		
		it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(saleId);
		ezShop.products.get(0).setQuantity(5);
		ezShop.currentSaleTransaction.setStatus(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED);
		assertFalse(ezShop.endSaleTransaction(saleId));
	}
	
	@Test
	public void testUnauthorizedEndSaleTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.endSaleTransaction(1);
		});	
	}
	
	@Test
	public void testInvalidIdEndSaleTransaction() throws InvalidTransactionIdException {
		
		Assert.assertThrows(InvalidTransactionIdException.class, () -> {
			ezShop.endSaleTransaction(null);
		});	
	}

	@Test
	public void testDeleteSaleTransaction()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidPaymentException, SQLException {
		login("ShopManager");
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.endSaleTransaction(saleId));
		it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(saleId);
		assertTrue(ezShop.deleteSaleTransaction(saleId));

	}
	
	@Test
	public void testRevertDeleteSaleTransaction()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidPaymentException {
		login("ShopManager");
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertTrue(ezShop.deleteSaleTransaction(saleId));
		
	}
	
	@Test
	public void testUnauthorizedDeleteSaleTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.deleteSaleTransaction(1);
		});	
	}

	@Test
	public void testGetSaleTransaction() throws UnauthorizedException, InvalidLocationException,
			InvalidProductIdException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidQuantityException, InvalidTransactionIdException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertTrue(ezShop.endSaleTransaction(saleId));
		assertNotNull(ezShop.getSaleTransaction(1));
	}
	
	@Test
	public void testUnauthorizedGetSaleTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.getSaleTransaction(1);
		});	
	}

	@Test
	public void testStartReturnTransaction()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidPaymentException, InvalidDiscountRateException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);

	}
	
	@Test
	public void testUnauthorizedStartReturnTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.startReturnTransaction(1);
		});	
	}

	@Test
	public void testReturnProduct() throws InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
			InvalidTransactionIdException, InvalidQuantityException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
	}
	
	@Test
	public void testUnauthorizedReturnProduct() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.returnProduct(1, "048001208025", 10);
		});	
	}

	@Test
	public void testEndReturnTransaction() throws InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
			InvalidTransactionIdException, InvalidQuantityException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, true));

	}
	
	
	@Test
	public void testNoCommitEndReturnTransaction() throws InvalidProductDescriptionException, InvalidProductCodeException,
	InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
	InvalidTransactionIdException, InvalidQuantityException {
		login("Administrator");
		
		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));
		
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		
		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, false));
		
	}
	
	@Test
	public void testUnauthorizedEndReturnTransaction() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.endReturnTransaction(1, true);
		});	
	}

	@Test
	public void testDeleteReturnTransaction() throws InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
			InvalidTransactionIdException, InvalidQuantityException {
		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);

		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, true));
		assertTrue(ezShop.deleteReturnTransaction(returnId));

	}
	
	@Test
	public void testRevertDeleteReturnTransaction() throws InvalidProductDescriptionException, InvalidProductCodeException,
	InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException,
	InvalidTransactionIdException, InvalidQuantityException, SQLException {
		login("Administrator");
		
		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));
		
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		
		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, true));
		ReturnTransaction.deleteReturnTransactionFromDb(returnId);
		assertTrue(ezShop.deleteReturnTransaction(returnId));
	}

	@Test
	public void testReceiveCreditCardPayment()
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException,
			InvalidQuantityException, InvalidDiscountRateException, InvalidCreditCardException {
		login("ShopManager");

		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);

		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));

		assertTrue(ezShop.receiveCreditCardPayment(saleId, "4485370086510891"));

	}
	
	@Test
	public void testUnauthorizedReceiveCreditCardPayment() throws UnauthorizedException {

		Assert.assertThrows(UnauthorizedException.class, () -> {
			ezShop.receiveCreditCardPayment(1, "4485370086510891");
		});	
	}
	
	@Test
	public void testNoEnoughMoneyReceiveCreditCardPayment()
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException,
			InvalidQuantityException, InvalidDiscountRateException, InvalidCreditCardException {
		login("ShopManager");
		
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);
		
		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
		assertFalse(ezShop.receiveCreditCardPayment(saleId, "4716258050958645"));
	}

	@Test
	public void testReceiveCashPayment()
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException,
			InvalidQuantityException, InvalidDiscountRateException, InvalidCreditCardException, InvalidPaymentException {
		login("ShopManager");

		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);
		
		Double money = ezShop.currentSaleTransaction.getPrice();
		assertTrue(ezShop.endSaleTransaction(saleId));
		
		Double payment = ezShop.receiveCashPayment(saleId, 1000.00);
		assertEquals(1000.00 - money, payment, 0);
	}
	
	@Test
	public void testReturnCashPayment()
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException,
			InvalidQuantityException, InvalidDiscountRateException, InvalidCreditCardException {
		login("ShopManager");
		
		Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);
		
		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
		assertTrue(ezShop.receiveCreditCardPayment(saleId, "4485370086510891"));
		
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, true));
		
		Double payment = ezShop.returnCashPayment(returnId);
		Double money = ezShop.currentReturnTransaction.getMoney();
		assertEquals(money, payment);
	}

	@Test
	public void testCreateCard() throws UnauthorizedException {
		login("Cashier");
		String card = ezShop.createCard();
		String card2 = ezShop.createCard();
		assertNotNull(card);
		assertNotNull(card2);
	}

	@Test(expected = UnauthorizedException.class)
	public void testNotLoggedCreateCard() throws UnauthorizedException {
		ezShop.createCard();
	}

	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginModifyPointsOnCard() throws InvalidCustomerCardException, UnauthorizedException {
		login("Cashier");
		String card = ezShop.createCard();

		ezShop.logout();
		ezShop.modifyPointsOnCard(card, 12);
	}

	@Test
	public void testWrongParametersModifyPointsOnCard() {
		login("Cashier");

		try {
			ezShop.modifyPointsOnCard("", 12);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.modifyPointsOnCard("121", 12);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.modifyPointsOnCard(null, 12);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}

	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginAttachCardToCustomer()
			throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
		login("Cashier");
		String card = ezShop.createCard();
		ezShop.logout();
		ezShop.attachCardToCustomer(card, 1);
	}

	@Test
	public void testWrongParametersAttachCardToCustomer() throws UnauthorizedException {
		login("Cashier");
		String card = ezShop.createCard();

		try {
			ezShop.attachCardToCustomer(card, null);
			fail();
		} catch (InvalidCustomerIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.attachCardToCustomer(card, -1);
			fail();
		} catch (InvalidCustomerIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}

		try {
			ezShop.attachCardToCustomer(null, 1);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.attachCardToCustomer("", 1);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.attachCardToCustomer("cliente22299", 1);
			fail();
		} catch (InvalidCustomerCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}

	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginDeleteReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException {
		ezShop.deleteReturnTransaction(1);
	}

	@Test
	public void testWrongParametersDeleteReturnTransaction()
			throws InvalidTransactionIdException, UnauthorizedException {
		login("Administrator");
		assertFalse(ezShop.deleteReturnTransaction(100));
		try {
			ezShop.deleteReturnTransaction(-1);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.deleteReturnTransaction(null);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}

	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginReceiveCashPayment()
			throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		ezShop.receiveCashPayment(100, 100);
	}

	@Test
	public void testWrongParametersReceiveCashPayment()
			throws InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		login("Administrator");
		try {
			ezShop.receiveCashPayment(-1, 0.99);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.receiveCashPayment(null, 0.99);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}

		try {
			ezShop.receiveCashPayment(100, -0.1);
			fail();
		} catch (InvalidPaymentException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}



	@Test
	public void testWrongParametersReceiveCreditCardPayment()
			throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidPaymentException, InvalidCreditCardException {
		login("Administrator");
		try {
			ezShop.receiveCreditCardPayment(-1, "4485370086510891");
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.receiveCreditCardPayment(null, "4485370086510891");
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}

		try {
			ezShop.receiveCreditCardPayment(1, null);
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.receiveCreditCardPayment(1, "");
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.receiveCreditCardPayment(1, "3213213213");
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}

	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginReturnCashPayment() throws InvalidTransactionIdException, UnauthorizedException {
		ezShop.returnCashPayment(1);
	}

	@Test
	public void testWrongParametersReturnCashPayment() {
		login("Administrator");
		try {
			ezShop.returnCashPayment(-1);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.returnCashPayment(null);
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}



	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginReturnCreditCardPayment()
			throws UnauthorizedException, InvalidTransactionIdException, InvalidCreditCardException {
		ezShop.returnCreditCardPayment(1, "4716258050958645");
	}
	
	@Test(expected = UnauthorizedException.class)
	public void testWrongLoginRecordBalanceUpdate() throws UnauthorizedException {
		login("Cashier");
		ezShop.recordBalanceUpdate(10);
	}

	@Test
	public void testWrongParametersReturnCreditCardPayment() {
		login("Administrator");
		try {
			ezShop.returnCreditCardPayment(null, "4716258050958645");
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.returnCreditCardPayment(-1, "4716258050958645");
			fail();
		} catch (InvalidTransactionIdException ignored) {
		} catch (Exception ignored) {
			fail();
		}

		try {
			ezShop.returnCreditCardPayment(1, null);
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.returnCreditCardPayment(1, "");
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
		try {
			ezShop.returnCreditCardPayment(1, "a12213");
			fail();
		} catch (InvalidCreditCardException ignored) {
		} catch (Exception ignored) {
			fail();
		}
	}

    @Test
    public void testReturnCreditCardPayment() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException, InvalidDiscountRateException, InvalidCreditCardException {
        login("Administrator");

        Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
		ezShop.updatePosition(productType, "123-abc-123");
		ezShop.updateQuantity(productType, 20);
		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSale(saleId, "048001208025", 10);
		ezShop.applyDiscountRateToSale(saleId, 0.2);
		ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);

		assertTrue(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(saleId));
		assertFalse(ezShop.endSaleTransaction(100));
		assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
		assertTrue(ezShop.receiveCreditCardPayment(saleId, "4485370086510891"));
		
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
		assertTrue(ezShop.endReturnTransaction(returnId, true));
		Double returned = ezShop.returnCreditCardPayment(returnId, "4485370086510891");
		Double money = ezShop.currentReturnTransaction.getMoney();
		assertEquals(money, returned);
    }
    
    @Test
    public void testNoMEnoughMoneyReturnCreditCardPayment() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException, InvalidDiscountRateException, InvalidCreditCardException {
    	login("Administrator");
    	
    	Integer productType = ezShop.createProductType("p1", "048001208025", 6.78, "note");
    	ezShop.updatePosition(productType, "123-abc-123");
    	ezShop.updateQuantity(productType, 20);
    	Integer saleId = ezShop.startSaleTransaction();
    	ezShop.addProductToSale(saleId, "048001208025", 10);
    	ezShop.applyDiscountRateToSale(saleId, 0.2);
    	ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2);
    	
    	assertTrue(ezShop.endSaleTransaction(saleId));
    	assertFalse(ezShop.endSaleTransaction(saleId));
    	assertFalse(ezShop.endSaleTransaction(100));
    	assertFalse(ezShop.applyDiscountRateToProduct(saleId, "048001208025", 0.2));
    	assertTrue(ezShop.receiveCreditCardPayment(saleId, "4485370086510891"));
    	
    	Integer returnId = -1;
    	returnId = ezShop.startReturnTransaction(saleId);
    	assertNotEquals(-1, returnId, 0);
    	assertTrue(ezShop.returnProduct(returnId, "048001208025", 10));
    	assertTrue(ezShop.endReturnTransaction(returnId, true));
    	Double returned = ezShop.returnCreditCardPayment(returnId, "4716258050958645");
    	Double money = ezShop.currentReturnTransaction.getMoney();
    	assertNotEquals(-1, money, 0);
    }
    
    @Test
    public void testGetCreditsAndDebits() throws UnauthorizedException {
    	login("Administrator");
    	
    	List<BalanceOperation> operations = ezShop.getCreditsAndDebits(null, null);
		assertEquals(operations.size(), 0);
    }
    
	@Test
	public void testRecordOrderArrivalRFID()
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidProductIdException, InvalidQuantityException {

		login("ShopManager");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertTrue(ezShop.updatePosition(productId, "123-abc-123"));

		ezShop.recordBalanceUpdate(1000);

		Integer orderId = ezShop.payOrderFor("048001208025", 10, 20.00);
		assertNotEquals(orderId, -1, 0);

		boolean done = ezShop.recordOrderArrivalRFID(orderId, "000000000100");
		assertTrue(done);
		if (done) {
			List<Order> orders = ezShop.getAllOrders();
			assertNotNull(orders);
			assertEquals(1, orders.size());
			assertEquals(it.polito.ezshop.data.model.Order.STATUS_COMPLETED, orders.get(0).getStatus());

			ProductType product = ezShop.getProductTypeByBarCode("048001208025");
			assertEquals(product.getQuantity(), 10, 0);
		}
	}

	@Test
	public void testAddProductToSaleRFID()
			throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidProductIdException, InvalidLocationException, SQLException {

		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 10));
		Integer id = Product.insertProduct("048001208025", "000000001100");

		Integer saleId = ezShop.startSaleTransaction();

		assertTrue(ezShop.addProductToSaleRFID(saleId,"000000001100"));
		it.polito.ezshop.data.ProductType p = null;
		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
		assertEquals("they should be equal", 10 - 1, p.getQuantity().intValue());
		

	}
    
	@Test
	public void testDeleteProductFromSaleRFID() throws InvalidTransactionIdException, InvalidRFIDException,
			InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, SQLException {

		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));
		Integer id = Product.insertProduct("048001208025", "000000001101");

		Integer saleId = ezShop.startSaleTransaction();
		assertTrue(ezShop.addProductToSaleRFID(saleId,"000000001101"));
		it.polito.ezshop.data.ProductType p = null;
		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
		assertEquals("they should be equal", 100 - 1, p.getQuantity().intValue());

		assertTrue(ezShop.deleteProductFromSaleRFID(saleId,"000000001101"));

		assertTrue(ezShop.currentSaleTransaction.getEntries().isEmpty());

		for (it.polito.ezshop.data.ProductType prod : ezShop.products) {
			if (prod.getBarCode().equalsIgnoreCase("048001208025"))
				p = prod;
		}
	}

	@Test
	public void testReturnProductRFID() throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidProductIdException, InvalidLocationException, InvalidQuantityException {

		login("Administrator");

		Integer productId = ezShop.createProductType("test2", "048001208025", 20.00, "note2");
		assertNotEquals(productId, -1, 0);
		ezShop.updatePosition(productId, "123-abc-321");
		assertTrue(ezShop.updateQuantity(productId, 100));

		Integer saleId = ezShop.startSaleTransaction();
		ezShop.addProductToSaleRFID(saleId, "000000001100");

		assertTrue(ezShop.endSaleTransaction(saleId));
		Integer returnId = -1;
		returnId = ezShop.startReturnTransaction(saleId);
		assertNotEquals(-1, returnId, 0);
		assertTrue(ezShop.returnProductRFID(returnId, "000000001100"));
	}

}
