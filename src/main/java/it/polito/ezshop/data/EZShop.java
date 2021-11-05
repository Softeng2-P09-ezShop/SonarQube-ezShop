package it.polito.ezshop.data;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import it.polito.ezshop.data.model.AccountBook;
import it.polito.ezshop.data.model.BalanceOperation;
import it.polito.ezshop.data.model.CreditCardCircuit;
import it.polito.ezshop.data.model.Product;
import it.polito.ezshop.data.model.ProductType;
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


public class EZShop implements EZShopInterface {

    public static User loggedUser = null;
    public static it.polito.ezshop.data.model.SaleTransaction currentSaleTransaction = new it.polito.ezshop.data.model.SaleTransaction();
    public static it.polito.ezshop.data.model.ReturnTransaction currentReturnTransaction = new it.polito.ezshop.data.model.ReturnTransaction();
    public static List<it.polito.ezshop.data.ProductType> products = new ArrayList<>();
    public static AccountBook accountBook = new AccountBook();
    public static CreditCardCircuit creditCards = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);

    public EZShop() {
    	loggedUser = null;
    	currentSaleTransaction = new it.polito.ezshop.data.model.SaleTransaction();
    	currentReturnTransaction = new it.polito.ezshop.data.model.ReturnTransaction();
    	products = new ArrayList<>();
    	accountBook = new AccountBook();
    	creditCards = new CreditCardCircuit(CreditCardCircuit.CREDIT_CARD_FILE);
    }
    
    @Override
    public void reset() {
    	currentSaleTransaction = new it.polito.ezshop.data.model.SaleTransaction();
    	currentReturnTransaction = new ReturnTransaction();
    	products = new ArrayList<>();
    	accountBook = new AccountBook();
    	
//    	truncate
    	try {
			BalanceOperation.resetBalanceOperations();
			it.polito.ezshop.data.model.SaleTransaction.resetSaleTransaction();
			it.polito.ezshop.data.model.TicketEntry.resetTicketEntries();
			ReturnTransaction.resetReturnTransaction();
			ProductType.resetProductTypes();
			it.polito.ezshop.data.model.Order.resetOrders();
			it.polito.ezshop.data.model.Customer.resetCustomer();
			it.polito.ezshop.data.model.User.resetUsers();
			Product.resetProducts();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public Integer createUser(String username, String password, String role)
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

		Integer userId = -1;
		if (username == null || username.isEmpty())
			throw new InvalidUsernameException();
		
		if (role == null || role.isEmpty() || (!role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)
				&& !role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_CASHIER)
				&& !role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER)))
			throw new InvalidRoleException();
		

		if (password == null || password.isEmpty())
			throw new InvalidPasswordException();
		
		
		
				List<User> users = null;
				try {
					users = it.polito.ezshop.data.model.User.getAllUsersFromDb();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(users != null)
				for (User user : users) {
					if (user.getUsername().equalsIgnoreCase(username))
						return userId;
				}
		


		

		try {
			userId = it.polito.ezshop.data.model.User.storeUserInDb(username, password, role);
		} catch (SQLException e) {
			return userId;
		}

		return userId; // Should be > 0, -1 if there are errors with the db or if a User with the same
	}

	@Override
	public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		if (id == null || id <= 0)
			throw new InvalidUserIdException();

		if (loggedUser == null
				|| !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)))
			throw new UnauthorizedException();

		boolean result = false;

		try {
			result = it.polito.ezshop.data.model.User.deleteUserFromDb(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<User> getAllUsers() throws UnauthorizedException {

		if (loggedUser == null
				|| !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)))
			throw new UnauthorizedException();

		List<User> users = new ArrayList<User>();

		try {
			users = it.polito.ezshop.data.model.User.getAllUsersFromDb();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

		if (loggedUser == null
				|| !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)))
			throw new UnauthorizedException();

		if (id == null || id <= 0)
			throw new InvalidUserIdException();

		User u = null;

		try {
			u = it.polito.ezshop.data.model.User.getUserFromDb(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return u;
	}

	@Override
	public boolean updateUserRights(Integer id, String role)
			throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

		if (loggedUser == null
				|| !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)))
			throw new UnauthorizedException();

		if (id == null || id <= 0)
			throw new InvalidUserIdException();

		if (role == null || (!role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR)
				&& !role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_CASHIER)
				&& !role.equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER)))
			throw new InvalidRoleException();

		boolean result = false;

		try {
			result = it.polito.ezshop.data.model.User.updateUserRightsInDb(id, role);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

		if (username == null || username.isEmpty())
			throw new InvalidUsernameException();

		if (password == null || password.isEmpty())
			throw new InvalidPasswordException();

		User u = null;

		try {
			u = it.polito.ezshop.data.model.User.authenticateFromDb(username, password);
			if (u != null) { // u may be null because credentials are wrong
				loggedUser = u;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return u;
	}

	@Override
	public boolean logout() {
		boolean result = false;
		if (loggedUser != null) {
			loggedUser = null;
			currentSaleTransaction = null;
			result = true;
		}
		return result;
	}

	@Override
	public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException {

		Integer productTypeId = -1;

		if (description == null || description.isEmpty())
			throw new InvalidProductDescriptionException();

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (pricePerUnit <= 0)
			throw new InvalidPricePerUnitException();

		checkProductCodeValidity(productCode);

		if (note == null)
			note = "";

		try {
			if (ProductType.getProductTypeByBarCodeFromDb(productCode) != null)
				return productTypeId;

			productTypeId = it.polito.ezshop.data.model.ProductType.storeProductTypeInDb(description, productCode,
					pricePerUnit, note);
		} catch (SQLException e) {
			return productTypeId;
		}

		return productTypeId;
	}

	@Override
	public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
			throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, UnauthorizedException {
		if (id == null || id <= 0)
			throw new InvalidProductIdException();

		if (newDescription == null || newDescription.isEmpty())
			throw new InvalidProductDescriptionException();

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (newPrice <= 0)
			throw new InvalidPricePerUnitException();

		if (newNote == null)
			newNote = "";

		checkProductCodeValidity(newCode);

		boolean result = false;
		try {
			List<it.polito.ezshop.data.ProductType> products = ProductType.getAllProductTypesFromDb();

			// checking if this product's barcode changed
			it.polito.ezshop.data.ProductType product = products.stream()
					.filter(p -> p.getId().equals(id) && p.getBarCode().equalsIgnoreCase(newCode)).findFirst().orElse(null);

			// if it changed, check if the new barcode is already used, if yes return false
			if (product == null)
				if (products.stream().filter(p -> p.getBarCode().equalsIgnoreCase(newCode)).findFirst()
						.orElse(null) != null)
					return result;

			result = it.polito.ezshop.data.model.ProductType.updateProductTypeInDb(id, newDescription, newCode, newPrice,
					newNote);
		} catch (SQLException e) {
			return result;
		}

		return result;
	}

	@Override
	public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (id == null || id <= 0)
			throw new InvalidProductIdException();

		boolean result = false;
		try {
			result = it.polito.ezshop.data.model.ProductType.deleteProductTypeFromDb(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<it.polito.ezshop.data.ProductType> getAllProductTypes() throws UnauthorizedException {
		if (loggedUser == null)
			throw new UnauthorizedException();

		List<it.polito.ezshop.data.ProductType> products = new ArrayList<it.polito.ezshop.data.ProductType>();
		try {
			products = it.polito.ezshop.data.model.ProductType.getAllProductTypesFromDb();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}

	@Override
	public it.polito.ezshop.data.ProductType getProductTypeByBarCode(String barCode)
			throws InvalidProductCodeException, UnauthorizedException {
		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		checkProductCodeValidity(barCode);

		it.polito.ezshop.data.ProductType product = null;

		try {
			product = it.polito.ezshop.data.model.ProductType.getProductTypeByBarCodeFromDb(barCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return product;
	}

	@Override
	public List<it.polito.ezshop.data.ProductType> getProductTypesByDescription(String description)
			throws UnauthorizedException {

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (description == null)
			description = "";

		List<it.polito.ezshop.data.ProductType> products = new ArrayList<it.polito.ezshop.data.ProductType>();
		try {
			products = it.polito.ezshop.data.model.ProductType.getProductTypesByDescriptionFromDb(description);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}

	@Override
	public boolean updateQuantity(Integer productId, int toBeAdded)
			throws InvalidProductIdException, UnauthorizedException {
		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (productId == null || productId <= 0)
			throw new InvalidProductIdException();

		boolean result = false;
		try {
			List<it.polito.ezshop.data.ProductType> products = ProductType.getAllProductTypesFromDb();
			for (it.polito.ezshop.data.ProductType product : products) {
				if (product.getId().equals(productId)) {
					if (product.getQuantity() == null && toBeAdded < 0)
						return result;
					else if (product.getQuantity() != null && product.getQuantity() + toBeAdded < 0)
						return result;
					if (product.getLocation() == null || product.getLocation().isEmpty())
						return result;
					break;
				}
			}
			result = it.polito.ezshop.data.model.ProductType.updateQuantityInDb(productId, toBeAdded);
		} catch (SQLException e) {
			return result;
		}

		return result;
	}

	@Override
	public boolean updatePosition(Integer productId, String newPos)
			throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		if (productId == null || productId <= 0)
			throw new InvalidProductIdException();

		if (newPos == null || newPos.isEmpty())
			newPos = "";
		else {
			// check validity of position
			checkPositionValidity(newPos);
		}

		boolean result = false;
		try {
			List<it.polito.ezshop.data.ProductType> products = ProductType.getAllProductTypesFromDb();
			for (it.polito.ezshop.data.ProductType product : products) {
				if (product.getLocation() != null && !product.getLocation().isEmpty()
						&& product.getLocation().equalsIgnoreCase(newPos))
					return result;
			}
			result = it.polito.ezshop.data.model.ProductType.updatePositionInDb(productId, newPos);
		} catch (SQLException e) {
			return result;
		}

		return result;
	}
	
	private void checkPositionValidity(String newPos) throws InvalidLocationException {
		String[] locationInfo = new String[] {};

		// split to get the three parameters of location
		locationInfo = newPos.split("-");
		// if they're not three throw the exception because format is wrong
		if (locationInfo.length != 3)
			throw new InvalidLocationException();

		// convert first parameter to array of chars
		char[] charArray = locationInfo[0].toCharArray();
		// check if all chars are between 0 and 9, if not throw the exception
		for (int i = 0; i < charArray.length; i++) {
			if (!(charArray[i] >= '0' && charArray[i] <= '9')) {
				throw new InvalidLocationException();
			}
		}

		// convert third parameter to array of chars
		charArray = locationInfo[2].toCharArray();
		// check if all chars are between 0 and 9, if not throw the exception
		for (int i = 0; i < charArray.length; i++) {
			if (!(charArray[i] >= '0' && charArray[i] <= '9')) {
				throw new InvalidLocationException();
			}
		}

		// convert the second parameter to lowercase
		locationInfo[1] = locationInfo[1].toLowerCase();
		// convert to array of chars
		charArray = locationInfo[1].toCharArray();
		// check if all chars are between a and z, if not throw the exception
		for (int i = 0; i < charArray.length; i++) {
			if (!(charArray[i] >= 'a' && charArray[i] <= 'z')) {
				throw new InvalidLocationException();
			}
		}
	}

    /**
     * This method issues an order of <quantity> units of product with given <productCode>, each unit will be payed
     * <pricePerUnit> to the supplier. <pricePerUnit> can differ from the re-selling price of the same product. The
     * product might have no location assigned in this step.
     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
     *
     * @param productCode the code of the product that we should order as soon as possible
     * @param quantity the quantity of product that we should order
     * @param pricePerUnit the price to correspond to the supplier (!= than the resale price of the shop) per unit of
     *                     product
     *
     * @return  the id of the order (> 0)
     *          -1 if the product does not exists, if there are problems with the db
     *
     * @throws InvalidProductCodeException if the productCode is not a valid bar code, if it is null or if it is empty
     * @throws InvalidQuantityException if the quantity is less than or equal to 0
     * @throws InvalidPricePerUnitException if the price per unit of product is less than or equal to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {

        checkProductCodeValidity(productCode);

        if (quantity <= 0)
            throw new InvalidQuantityException();

        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        if (loggedUser == null || (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
                && !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
            throw new UnauthorizedException();

        int orderId = -1;
        it.polito.ezshop.data.ProductType p = null;
        try {
            p = it.polito.ezshop.data.model.ProductType.getProductTypeByBarCodeFromDb(productCode);

            if (p == null)
                return orderId;

            int balanceId = it.polito.ezshop.data.model.BalanceOperation.incrementCounter();
            orderId = it.polito.ezshop.data.model.Order.issueOrderInDb(productCode, quantity, pricePerUnit, balanceId, it.polito.ezshop.data.model.Order.STATUS_ISSUED);
            if(orderId > 0)
            	accountBook.addBalanceOperation(new it.polito.ezshop.data.model.Order(orderId, balanceId, productCode, it.polito.ezshop.data.model.Order.STATUS_ISSUED, 
            			quantity, pricePerUnit, it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID, 0.0));

            return orderId;   
        } catch (SQLException e) {
            return orderId;
        }
    }

    /**
     * This method records the arrival of an order with given <orderId>. This method changes the quantity of available product.
     * This method records each product received, with its RFID. RFIDs are recorded starting from RFIDfrom, in increments of 1
     * ex recordOrderArrivalRFID(10, "000000001000")  where order 10 ordered 10 quantities of an item, this method records
     * products with RFID 1000, 1001, 1002, 1003 etc until 1009
     * The product type affected must have a location registered. The order should be either in the PAYED state (in this
     * case the state will change to the COMPLETED one and the quantity of product type will be updated) or in the
     * COMPLETED one (in this case this method will have no effect at all).
     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
     *
     * @param orderId the id of the order that has arrived
     *
     * @return  true if the operation was successful
     *          false if the order does not exist or if it was not in an ORDERED/COMPLETED state
     *
     * @throws InvalidOrderIdException if the order id is less than or equal to 0 or if it is null.
     * @throws InvalidLocationException if the ordered product type has not an assigned location.
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     * @throws InvalidRFIDException if the RFID has invalid format or is not unique 
     */
	@Override
	public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
	InvalidLocationException, InvalidRFIDException {
		
		if (orderId == null || orderId <= 0)
			throw new InvalidOrderIdException();

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		checkRFIDValidity(RFIDfrom);
		
		try {
			return it.polito.ezshop.data.model.Order.recordOrderArrivalInDb(orderId, RFIDfrom);
		} catch (SQLException e) {
			return false;
		} catch (InvalidLocationException e) {
			throw new InvalidLocationException();
		}
	}
    
    /**
     * This method directly orders and pays <quantity> units of product with given <productCode>, each unit will be payed
     * <pricePerUnit> to the supplier. <pricePerUnit> can differ from the re-selling price of the same product. The
     * product might have no location assigned in this step.
     * This method affects the balance of the system.
     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
     *
     * @param productCode the code of the product to be ordered
     * @param quantity the quantity of product to be ordered
     * @param pricePerUnit the price to correspond to the supplier (!= than the resale price of the shop) per unit of
     *                     product
     *
     * @return  the id of the order (> 0)
     *          -1 if the product does not exists, if the balance is not enough to satisfy the order, if there are some
     *          problems with the db
     *
     * @throws InvalidProductCodeException if the productCode is not a valid bar code, if it is null or if it is empty
     * @throws InvalidQuantityException if the quantity is less than or equal to 0
     * @throws InvalidPricePerUnitException if the price per unit of product is less than or equal to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
            UnauthorizedException {
        checkProductCodeValidity(productCode);

        if (quantity <= 0)
            throw new InvalidQuantityException();

        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        if (loggedUser == null || (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
                && !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
            throw new UnauthorizedException();

        it.polito.ezshop.data.ProductType p = null;
        int orderId = -1;
        try {
            p = it.polito.ezshop.data.model.ProductType.getProductTypeByBarCodeFromDb(productCode);
            if (p == null)
                return orderId;

            int balanceId = it.polito.ezshop.data.model.BalanceOperation.incrementCounter();
            orderId = it.polito.ezshop.data.model.Order.issueOrderInDb(productCode, quantity, pricePerUnit, balanceId, it.polito.ezshop.data.model.Order.STATUS_PAID);
            if(orderId > 0) {
            	accountBook.addBalanceOperation(new it.polito.ezshop.data.model.Order(orderId, balanceId, 
            			productCode, it.polito.ezshop.data.model.Order.STATUS_PAID, 
            			quantity, pricePerUnit, it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID, quantity * pricePerUnit));
//            	compute balance update
				accountBook.recordBalanceUpdate(-(quantity * pricePerUnit));
			}
		} catch (SQLException e) {
			return orderId;
		}
		return orderId;
	}

	/**
	 * This method change the status the order with given <orderId> into the "PAYED"
	 * state. The order should be either issued (in this case the status changes) or
	 * payed (in this case the method has no effect). This method affects the
	 * balance of the system. It can be invoked only after a user with role
	 * "Administrator" or "ShopManager" is logged in.
	 *
	 * @param orderId the id of the order to be ORDERED
	 *
	 * @return true if the order has been successfully ordered false if the order
	 *         does not exist or if it was not in an ISSUED/ORDERED state
	 *
	 * @throws InvalidOrderIdException if the order id is less than or equal to 0 or
	 *                                 if it is null.
	 * @throws UnauthorizedException   if there is no logged user or if it has not
	 *                                 the rights to perform the operation
	 */
	@Override
	public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
		if (orderId == null || orderId <= 0)
			throw new InvalidOrderIdException();

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		try {
			List<Order> orders = it.polito.ezshop.data.model.Order.getAllOrdersFromDb();
			for(Order o : orders) {
				if(o.getOrderId().intValue() == orderId.intValue()) {
					if(accountBook.getBalance() - (o.getQuantity() * o.getPricePerUnit()) < 0) return false;
				}
			}
			
			final Order order = it.polito.ezshop.data.model.Order.payOrderInDb(orderId);
			if (order != null) {
				for (BalanceOperation balance : accountBook.getBalanceOperations()) {
					if (balance.getBalanceId() == order.getBalanceId()) {
						balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID);
						balance.setMoney(order.getQuantity() * order.getPricePerUnit());
						BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), BalanceOperation.TYPE_ORDER);
						accountBook.recordBalanceUpdate(-(order.getQuantity() * order.getPricePerUnit()));
						break;
					}
				}
				return true;
			} else
				return false;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * This method records the arrival of an order with given <orderId>. This method
	 * changes the quantity of available product. The product type affected must
	 * have a location registered. The order should be either in the PAYED state (in
	 * this case the state will change to the COMPLETED one and the quantity of
	 * product type will be updated) or in the COMPLETED one (in this case this
	 * method will have no effect at all). It can be invoked only after a user with
	 * role "Administrator" or "ShopManager" is logged in.
	 *
	 * @param orderId the id of the order that has arrived
	 *
	 * @return true if the operation was successful false if the order does not
	 *         exist or if it was not in an ORDERED/COMPLETED state
	 *
	 * @throws InvalidOrderIdException  if the order id is less than or equal to 0
	 *                                  or if it is null.
	 * @throws InvalidLocationException if the ordered product type has not an
	 *                                  assigned location.
	 * @throws UnauthorizedException    if there is no logged user or if it has not
	 *                                  the rights to perform the operation
	 */
	@Override
	public boolean recordOrderArrival(Integer orderId)
			throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
		if (orderId == null || orderId <= 0)
			throw new InvalidOrderIdException();

		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		try {
			return it.polito.ezshop.data.model.Order.recordOrderArrivalInDb(orderId, null);
		} catch (SQLException e) {
			return false;
		} catch (InvalidLocationException e) {
			throw new InvalidLocationException();
		}
	}

	/**
	 * This method return the list of all orders ISSUED, ORDERED and COMLPETED. It
	 * can be invoked only after a user with role "Administrator" or "ShopManager"
	 * is logged in.
	 *
	 * @return a list containing all orders
	 *
	 * @throws UnauthorizedException if there is no logged user or if it has not the
	 *                               rights to perform the operation
	 */
	@Override
	public List<Order> getAllOrders() throws UnauthorizedException {
		if (loggedUser == null
				|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
						&& !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
			throw new UnauthorizedException();

		List<Order> orders = new ArrayList<>();
		try {
			orders = it.polito.ezshop.data.model.Order.getAllOrdersFromDb();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return orders;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (customerName == null || customerName.isEmpty())
            throw new InvalidCustomerNameException();
        if (loggedUser == null)
            throw new UnauthorizedException();

        Integer customerId = -1;

        try {
            customerId = it.polito.ezshop.data.model.Customer.storeCustomerInDb(customerName);
        } catch (SQLException e) {
            return customerId;
        }

        return customerId;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
            throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
            UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        
		if (id == null || id <= 0)
			throw new InvalidCustomerIdException();

        if (newCustomerName == null || newCustomerName.isEmpty())
            throw new InvalidCustomerNameException();

        if (newCustomerCard.length() != 0 && !newCustomerCard.chars().allMatch(Character::isDigit))
            throw new InvalidCustomerCardException();
         
        boolean result = false;
        try {
    		if(it.polito.ezshop.data.model.Customer.getAllCustomersFromDb()
    				.stream().filter(customer -> customer.getCustomerCard().equalsIgnoreCase(newCustomerCard)).findFirst().orElse(null) == null)
    			result = it.polito.ezshop.data.model.Customer.modifyCustomerInDb(id, newCustomerName, newCustomerCard);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();
        boolean result = false;

        try {
            result = it.polito.ezshop.data.model.Customer.deleteCustomerFromDb(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();
        Customer c = null;
        try {
            c = it.polito.ezshop.data.model.Customer.getCustomerFromDb(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        List<Customer> customers = new ArrayList<Customer>();
        try {
            customers = it.polito.ezshop.data.model.Customer.getAllCustomersFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        
        String number = "";
        try {
            number = it.polito.ezshop.data.model.CustomerCard.storeCardInDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        if (customerId == null || customerId <= 0)
            throw new InvalidCustomerIdException();

        if (customerCard == null || customerCard.length() != 10)
            throw new InvalidCustomerCardException();

        // check if all chars are between 0 and 9, if not throw the exception
        try { 
        	customerCard.chars().allMatch(Character::isDigit);
        } catch (Exception e) {
            throw new InvalidCustomerCardException();	
        }

        boolean result = false;
        try {
            result = it.polito.ezshop.data.model.Customer.attachCardToCustomerInDb(customerCard, customerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        if (customerCard == null || customerCard.length() != 10)
            throw new InvalidCustomerCardException();

        // check if all chars are between 0 and 9, if not throw the exception
        try { 
        	boolean isDigit = customerCard.chars().allMatch(Character::isDigit);
        	if(!isDigit) throw new InvalidCustomerCardException();
        } catch (Exception e) {
            throw new InvalidCustomerCardException();	
        }

        boolean result = false;
        try {
            result = it.polito.ezshop.data.model.CustomerCard.modifyPointsOnCardInDb(customerCard, pointsToBeAdded);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method starts a new sale transaction and returns its unique identifier.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @return the id of the transaction (greater than or equal to 0)
     */
    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        
        // Get last ticketNumber in the system
        int ticketNumber = 0;
        try {
            ticketNumber = it.polito.ezshop.data.model.SaleTransaction.getLastIdFromSaleTransactions();
            ticketNumber++;

            // create a new sale transaction
            currentSaleTransaction = new it.polito.ezshop.data.model.SaleTransaction(ticketNumber, it.polito.ezshop.data.model.BalanceOperation.incrementCounter(), new ArrayList<TicketEntry>(), 0.0, 
            		it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED, 0.0, 
            		it.polito.ezshop.data.model.SaleTransaction.NAME, it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID);
            
            // get all the products for DB
            products = it.polito.ezshop.data.model.ProductType.getAllProductTypesFromDb();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return currentSaleTransaction.getTicketNumber();
    }

    public void checkRFIDValidity(String RFID) throws InvalidRFIDException {
    	// RFID is a positive integer (received as a 10 characters string)
		// unique 
		if(RFID == null || RFID.length() != 12)
			throw new InvalidRFIDException();
		try {
			Integer rfidFrom = new Integer(RFID);
		}catch (Exception e) {
			throw new InvalidRFIDException();
		}
    }
    
    /**
     * This method adds a product to a sale transaction receiving  its RFID, decreasing the temporary amount of product available on the
     * shelves for other customers.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the Sale transaction
     * @param RFID the RFID of the product to be added
     * @return  true if the operation is successful
     *          false   if the RFID does not exist,
     *                  if the transaction id does not identify a started and open transaction.
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws InvalidRFIDException if the RFID code is empty, null or invalid
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
	@Override
	public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        
		if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if (loggedUser == null)
            throw new UnauthorizedException();
        
		checkRFIDValidity(RFID);

        if ((currentSaleTransaction.getTicketNumber() != transactionId)
                || !(currentSaleTransaction.getStatus().equals(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))) {
            return false;
        }
        
        try {
        	Product product = Product.getProductByRFID(RFID);
        	if (product == null)
        		return false;
        	
            it.polito.ezshop.data.ProductType pr = products.stream().filter(productType -> productType.getBarCode().equalsIgnoreCase(product.getBarCode())).findFirst().orElse(null);
            
            if(pr.getQuantity() - 1 < 0)
                return false;
            
            if(currentSaleTransaction.addProductToSale(product.getBarCode(), pr.getProductDescription(),
            		pr.getPricePerUnit(), 1)) {
            	currentSaleTransaction.setPrice(currentSaleTransaction.getPrice() + (pr.getPricePerUnit() * 1));

            	for(it.polito.ezshop.data.ProductType p : products) {
            		if(p.getBarCode().equalsIgnoreCase(product.getBarCode()))
            			p.setQuantity(pr.getQuantity() - 1);
            	}
            	
//            	if(Product.deleteProductByRFID(RFID)) 
            		return true;
//            	else return false;
            }
            else 
                return false;

        } catch (SQLException e) {
    		return false;
		}
    }
	
	/**
     * This method deletes a product from a sale transaction , receiving its RFID, increasing the temporary amount of product available on the
     * shelves for other customers.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the Sale transaction
     * @param RFID the RFID of the product to be deleted
     *
     * @return  true if the operation is successful
     *          false   if the product code does not exist,
     *                  if the transaction id does not identify a started and open transaction.
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws InvalidRFIDException if the RFID is empty, null or invalid
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        
    	// Check transaction id
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        
        if (loggedUser == null)
            throw new UnauthorizedException();

        checkRFIDValidity(RFID);
        
        //wrong transactionId
        if ((currentSaleTransaction.getTicketNumber() != transactionId)
                || !(currentSaleTransaction.getStatus().equals(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))) 
            return false;
        
		try {
			Product product = Product.getProductByRFID(RFID);
	        if(product == null) return false;
	
	        return deleteProduct(product.getBarCode(), 1);
		} catch (SQLException e) {
			return false;
		}
    }
    
    private boolean deleteProduct(String barCode, int amount) {
    	//If productCode does not exist between entries
        List<TicketEntry> tt = currentSaleTransaction.getEntries();
        for(int i = 0; i < tt.size(); i++) {
        	if(tt.get(i).getBarCode().equalsIgnoreCase(barCode)) {
        		
//        		check amount 
        		if(amount > tt.get(i).getAmount())
        			return false;
        		        
        		final int index = i;
        		for(it.polito.ezshop.data.ProductType productType : products) {
        			if(productType.getBarCode().equalsIgnoreCase(barCode)) {
        				productType.setQuantity(productType.getQuantity() + amount);

                		currentSaleTransaction.setPrice(currentSaleTransaction.getPrice()
                				 -(tt.get(index).getDiscountRate() > 0 ? productType.getPricePerUnit() * amount * tt.get(index).getDiscountRate() 
                						 : productType.getPricePerUnit() * amount));
                	}
        		}

        		if (tt.get(i).getAmount() == amount) 
        			tt.remove(i);
        		else 
	        		tt.get(i).setAmount(tt.get(i).getAmount() - amount);
        		
        		return true;
        	}
        }
        return false;
    }

    /**
     * This method adds a product to a sale transaction decreasing the temporary amount of product available on the
     * shelves for other customers.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the Sale transaction
     * @param productCode the barcode of the product to be added
     * @param amount the quantity of product to be added
     * @return  true if the operation is successful
     *          false   if the product code does not exist,
     *                  if the quantity of product cannot satisfy the request,
     *                  if the transaction id does not identify a started and open transaction.
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws InvalidProductCodeException if the product code is empty, null or invalid7
     * @throws InvalidQuantityException if the quantity is less than 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {

        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if (loggedUser == null)
            throw new UnauthorizedException();

        checkProductCodeValidity(productCode);

        if (amount < 0)
            throw new InvalidQuantityException();

        if ((currentSaleTransaction.getTicketNumber() != transactionId)
                || !(currentSaleTransaction.getStatus().equals(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))) {
            return false;
        }
        
        it.polito.ezshop.data.ProductType pr = products.stream().filter(product -> product.getBarCode().equalsIgnoreCase(productCode)).findFirst().orElse(null);
        
        if (pr == null || pr.getQuantity() - amount < 0)
            return false;

        if(currentSaleTransaction.addProductToSale(productCode, pr.getProductDescription(),pr.getPricePerUnit(),amount)) {
        	currentSaleTransaction.setPrice(currentSaleTransaction.getPrice() + (pr.getPricePerUnit()*amount));
//            products.stream().filter(product -> {
//            	if(product.getBarCode().equalsIgnoreCase(productCode)) {
//            		product.setQuantity(pr.getQuantity() - amount);
//            	}
//        		return true;
//            });
        	for(it.polito.ezshop.data.ProductType p : products) {
        		if(p.getBarCode().equalsIgnoreCase(productCode))
        			p.setQuantity(pr.getQuantity() - amount);
        		
        	}
            return true;
        }
        else 
            return false;
    }


    /**
     * This method deletes a product from a sale transaction increasing the temporary amount of product available on the
     * shelves for other customers.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the Sale transaction
     * @param productCode the barcode of the product to be deleted
     * @param amount the quantity of product to be deleted
     *
     * @return  true if the operation is successful
     *          false   if the product code does not exist,
     *                  if the quantity of product cannot satisfy the request,
     *                  if the transaction id does not identify a started and open transaction.
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws InvalidProductCodeException if the product code is empty, null or invalid
     * @throws InvalidQuantityException if the quantity is less than 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {
        // Check transaction id
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        
        if (loggedUser == null)
            throw new UnauthorizedException();

        checkProductCodeValidity(productCode);

        // Check quantity
        if (amount < 0)
            throw new InvalidQuantityException();

        //wrong transactionId
        if ((currentSaleTransaction.getTicketNumber() != transactionId)
                || !(currentSaleTransaction.getStatus().equals(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))) {
            return false;
        }

        return deleteProduct(productCode, amount);
    }
    
    /**
     * This method applies a discount rate to all units of a product type with given type in a sale transaction. The
     * discount rate should be greater than or equal to 0 and less than 1.
     * The sale transaction should be started and open.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the Sale transaction
     * @param productCode the barcode of the product to be discounted
     * @param discountRate the discount rate of the product
     *
     * @return  true if the operation is successful
     *          false   if the product code does not exist,
     *                  if the transaction id does not identify a started and open transaction.
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws InvalidProductCodeException if the product code is empty, null or invalid
     * @throws InvalidDiscountRateException if the discount rate is less than 0 or if it greater than or equal to 1.00
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
            UnauthorizedException {
    	
    	if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();

		checkProductCodeValidity(productCode);

		if (loggedUser == null)
			throw new UnauthorizedException();

		// Check discount rate
		if (discountRate < 0.0 || discountRate >= 1.0)
			throw new InvalidDiscountRateException();

		if ((currentSaleTransaction.getTicketNumber() != transactionId) || !(currentSaleTransaction.getStatus()
				.equalsIgnoreCase(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))) {
			return false;
		}

		if (currentSaleTransaction.getEntries().stream()
				.filter(ticket -> ticket.getBarCode().equalsIgnoreCase(productCode)).findFirst().orElse(null) == null)
			return false;

		for(TicketEntry ticket : currentSaleTransaction.getEntries()) {
			if (ticket.getBarCode().equalsIgnoreCase(productCode)) {
				ticket.setDiscountRate(discountRate);
		        currentSaleTransaction.setPrice(currentSaleTransaction.getPrice() - (ticket.getPricePerUnit()*ticket.getAmount()* ticket.getDiscountRate()));
		        break;
			}	
		}
		return true;
	}

	/**
	 * This method applies a discount rate to the whole sale transaction. The
	 * discount rate should be greater than or equal to 0 and less than 1. The sale
	 * transaction can be either started or closed but not already payed. It can be
	 * invoked only after a user with role "Administrator", "ShopManager" or
	 * "Cashier" is logged in.
	 *
	 * @param transactionId the id of the Sale transaction
	 * @param discountRate  the discount rate of the sale
	 *
	 * @return true if the operation is successful false if the transaction does not
	 *         exists
	 *
	 * @throws InvalidTransactionIdException if the transaction id less than or
	 *                                       equal to 0 or if it is null
	 * @throws InvalidDiscountRateException  if the discount rate is less than 0 or
	 *                                       if it greater than or equal to 1.00
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 */
	@Override
	public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
			throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();

		// Check discount rate
		if (discountRate < 0.0 || discountRate >= 1.0)
			throw new InvalidDiscountRateException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if ((currentSaleTransaction.getTicketNumber() != transactionId)
				|| (currentSaleTransaction.getStatus().equals(it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED))) {
			return false;
		}

		currentSaleTransaction.setDiscountRate(discountRate);
		currentSaleTransaction.setPrice(currentSaleTransaction.getPrice() - currentSaleTransaction.getPrice() * discountRate);
		return true;
	}

	/**
	 * This method returns the number of points granted by a specific sale
	 * transaction. Every 10€ the number of points is increased by 1 (i.e. 19.99€
	 * returns 1 point, 20.00€ returns 2 points). If the transaction with given id
	 * does not exist then the number of points returned should be -1. The
	 * transaction may be in any state (open, closed, payed). It can be invoked only
	 * after a user with role "Administrator", "ShopManager" or "Cashier" is logged
	 * in.
	 *
	 * @param transactionId the id of the Sale transaction
	 *
	 * @return the points of the sale (1 point for each 10€) or -1 if the
	 *         transaction does not exists
	 *
	 * @throws InvalidTransactionIdException if the transaction id less than or
	 *                                       equal to 0 or if it is null
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 */
	@Override
	public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if (currentSaleTransaction.getTicketNumber() == transactionId) {
			return currentSaleTransaction.computePointsForSale();
		}

		return -1;
	}

	/**
	 * This method closes an opened transaction. After this operation the
	 * transaction is persisted in the system's memory. It can be invoked only after
	 * a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
	 *
	 * @param transactionId the id of the Sale transaction
	 *
	 * @return true if the transaction was successfully closed false if the
	 *         transaction does not exist, if it has already been closed, if there
	 *         was a problem in registering the data
	 *
	 * @throws InvalidTransactionIdException if the transaction id less than or
	 *                                       equal to 0 or if it is null
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 */
	@Override
	public boolean endSaleTransaction(Integer transactionId)
			throws InvalidTransactionIdException, UnauthorizedException {

		if (transactionId == null || transactionId <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if (currentSaleTransaction == null || currentSaleTransaction.getTicketNumber() != transactionId
				|| !currentSaleTransaction.getStatus()
						.equalsIgnoreCase(it.polito.ezshop.data.model.SaleTransaction.STATUS_OPENED))
			return false;

		currentSaleTransaction.setStatus(it.polito.ezshop.data.model.SaleTransaction.STATUS_CLOSED);

		boolean revert = false;
		try {
			
			if(!saveTransaction(transactionId)) return false;

			//update product quantities
			revert = updateSaleProductQuantity(false, true);
			
			if(revert) {
				it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(currentSaleTransaction.getTicketNumber());
				accountBook.removeBalanceOperation(accountBook.balanceOperations.stream().filter(balance -> balance.getBalanceId() == currentSaleTransaction.getBalanceId()).findFirst().orElse(null));
				return false;
			}
			else {
				for (TicketEntry ticket : currentSaleTransaction.getEntries()) {
					if (it.polito.ezshop.data.model.TicketEntry.storeTicketEntryInDb(
							currentSaleTransaction.getTicketNumber(), ticket.getBarCode(), ticket.getDiscountRate(),
							ticket.getAmount(), ticket.getPricePerUnit(), ticket.getProductDescription()) < 0)
						revert = true;
				}
				
				if (revert) {
					it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(currentSaleTransaction.getTicketNumber());
					accountBook.removeBalanceOperation(accountBook.balanceOperations.stream().filter(balance -> balance.getBalanceId() == currentSaleTransaction.getBalanceId()).findFirst().orElse(null));
					
					for(it.polito.ezshop.data.TicketEntry ticket : currentSaleTransaction.getEntries()) {
						ProductType.updateQuantityInDb(products.stream().filter(product -> product.getBarCode().equalsIgnoreCase(ticket.getBarCode())).findFirst().get().getId() , ticket.getAmount());
					}

					return false;
				}
				else return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	private boolean saveTransaction(int transactionId) {
		try {
//			save transaction in db
			if (it.polito.ezshop.data.model.SaleTransaction.storeSaleInDb(transactionId, currentSaleTransaction.getDiscountRate(),
					currentSaleTransaction.getPrice(), currentSaleTransaction.getStatus()) > 0) {
//				update balance 
				accountBook.addBalanceOperation(new it.polito.ezshop.data.model.SaleTransaction(transactionId, currentSaleTransaction.getBalanceId(), 
						currentSaleTransaction.getEntries(), currentSaleTransaction.getDiscountRate(), currentSaleTransaction.getStatus(),
						currentSaleTransaction.getPrice(), it.polito.ezshop.data.model.SaleTransaction.NAME, it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID));
				return true;
			}	
			else 
				return false;
			
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * This method deletes a sale transaction with given unique identifier from the
	 * system's data store. It can be invoked only after a user with role
	 * "Administrator", "ShopManager" or "Cashier" is logged in.
	 *
	 * @param transactionId the number of the transaction to be deleted
	 *
	 * @return true if the transaction has been successfully deleted, false if the
	 *         transaction doesn't exist, if it has been payed, if there are some
	 *         problems with the db
	 *
	 * @throws InvalidTransactionIdException if the transaction id number is less
	 *                                       than or equal to 0 or if it is null
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 */
	public BalanceOperation removeBalance(int balanceId) {
//		update balance 
		for (it.polito.ezshop.data.model.BalanceOperation balance : accountBook.getBalanceOperations()) {
			if (balance.getBalanceId() == balanceId) {
				accountBook.removeBalanceOperation(balance);
				return balance;
			}
		}
		return null;
	}
	
	private boolean updateSaleProductQuantity(boolean positiveAmount, boolean checkQuantity) {
		
		try {
			for (it.polito.ezshop.data.TicketEntry ticket : currentSaleTransaction.getEntries()) {
				int amount = ticket.getAmount();
				if(!positiveAmount) amount = -amount;
					
				it.polito.ezshop.data.ProductType p = products.stream().filter(product -> product.getBarCode().equalsIgnoreCase(ticket.getBarCode()))
						.findFirst().orElse(null);
				
				if(checkQuantity) {
					if(p != null && p.getQuantity() - ticket.getAmount() >=0) {
						if(!ProductType.updateQuantityInDb(p.getId() , amount)) 
							return true;
					}
					else return true;
				}
				else {
					if (!ProductType.updateQuantityInDb(p.getId(), amount)) 
						return true;
				}
			}
		} catch (SQLException e) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteSaleTransaction(Integer saleNumber)
			throws InvalidTransactionIdException, UnauthorizedException {

		if (saleNumber == null || saleNumber <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if (currentSaleTransaction == null || currentSaleTransaction.getTicketNumber() != saleNumber
				|| !currentSaleTransaction.getStatus()
						.equalsIgnoreCase(it.polito.ezshop.data.model.SaleTransaction.STATUS_CLOSED))
			return false;

		try {

			boolean revert = false;
//			update balance 
			it.polito.ezshop.data.model.BalanceOperation balanceOp = removeBalance(currentSaleTransaction.getBalanceId());
			if(balanceOp == null) revert = true;
			
			if(revert) return false;
			
			// update product quantities
			revert = updateSaleProductQuantity(true, false);
			
			if(revert) {
				accountBook.addBalanceOperation(balanceOp);
				return false;
			}
			else {
				//delete transaction in db
				if(it.polito.ezshop.data.model.SaleTransaction.deleteSaleTransactionFromDb(saleNumber)) {
					it.polito.ezshop.data.model.TicketEntry.deleteTicketsBySaleFromDb(saleNumber);
					currentSaleTransaction = new it.polito.ezshop.data.model.SaleTransaction();
					return true;
				} else {
					// update product quantities
					updateSaleProductQuantity(false, false);
					accountBook.addBalanceOperation(balanceOp);
				}
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
    }
    
    /**
     * This method returns  a closed sale transaction.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the id of the CLOSED Sale transaction
     *
     * @return the transaction if it is available (transaction closed), null otherwise
     *
     * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
    
        try {
			return it.polito.ezshop.data.model.SaleTransaction.getSaleTransactionFromDb(transactionId);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
    }

    /**
     * This method starts a new return transaction for units of products that have already been sold and payed.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the number of the transaction
     *
     * @return the id of the return transaction (>= 0), -1 if the transaction is not available.
     *
     * @throws InvalidTransactionIdException if the transactionId  is less than or equal to 0 or if it is null
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        
        if (saleNumber == null || saleNumber <= 0)
            throw new InvalidTransactionIdException();
        
        Integer returnTransactionId = -1;

        try {
        	products = ProductType.getAllProductTypesFromDb();
        	
        	int amount = 0;
        	it.polito.ezshop.data.model.SaleTransaction sale = it.polito.ezshop.data.model.SaleTransaction.getSaleTransactionFromDb(saleNumber);
        	if(sale != null) {
            	for(TicketEntry ticket : sale.getEntries()) {
	        		amount +=ticket.getAmount();
	        	}
	        	if (amount == 0)
	        		return returnTransactionId;
	        	
        		int balanceId = it.polito.ezshop.data.model.BalanceOperation.incrementCounter();
				returnTransactionId = ReturnTransaction.storeReturnInDb(saleNumber, "", 0, balanceId, ReturnTransaction.STATUS_UNPAID);
				currentReturnTransaction = new ReturnTransaction(returnTransactionId, saleNumber, balanceId, false, ReturnTransaction.STATUS_UNPAID, 0, it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return returnTransactionId;
		}
		return returnTransactionId;
    }
    
    /**
     * This method adds a product to the return transaction
     * The amount of units of product to be returned should not exceed the amount originally sold.
     * This method DOES NOT update the product quantity
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the id of the return transaction
     * @param productCode the bar code of the product to be returned
     * @param amount the amount of product to be returned
     *
     * @return  true if the operation is successful
     *          false   if the the product to be returned does not exists,
     *                  if it was not in the transaction,
     *                  if the amount is higher than the one in the sale transaction,
     *                  if the transaction does not exist
     *
     * @throws InvalidTransactionIdException if the return id is less ther or equal to 0 or if it is null
     * @throws InvalidProductCodeException if the product code is empty, null or invalid
     * @throws InvalidQuantityException if the quantity is less than or equal to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
            InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        checkProductCodeValidity(productCode);
        if (amount <= 0)
            throw new InvalidQuantityException();

//      same return transaction
        if(currentReturnTransaction.getId() == null || !currentReturnTransaction.getId().equals(returnId))
        	return false;

    	try {
    		return returnProduct(productCode, amount);
        } catch (SQLException e) {
			return false;
		}
	}
    
    /**
     * This method adds a product to the return transaction, starting from its RFID
     * This method DOES NOT update the product quantity
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the id of the return transaction
     * @param RFID the RFID of the product to be returned
     *
     * @return  true if the operation is successful
     *          false   if the the product to be returned does not exists,
     *                  if it was not in the transaction,
     *                  if the transaction does not exist
     *
     * @throws InvalidTransactionIdException if the return id is less ther or equal to 0 or if it is null
     * @throws InvalidRFIDException if the RFID is empty, null or invalid
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
	@Override
	public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
		if (loggedUser == null)
            throw new UnauthorizedException();
     
		if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        checkRFIDValidity(RFID);

//      same return transaction
        if(currentReturnTransaction.getId() == null || !currentReturnTransaction.getId().equals(returnId))
        	return false;
        
        try {
        	Product product = Product.getProductByRFID(RFID);
            if(product == null) return false;
            
        	return returnProduct(product.getBarCode(), 1);
        } catch (SQLException e) {
			return false;
		}
    }
	
	public boolean returnProduct(String barCode, int amount) throws SQLException {
		
//      product exists 
        if(products.stream().filter(productType -> productType.getBarCode().equalsIgnoreCase(barCode)).findFirst().orElse(null) == null) 
        	return false;
        
//    	sale transaction exists
        it.polito.ezshop.data.model.SaleTransaction saleTransaction 
        	= it.polito.ezshop.data.model.SaleTransaction.getSaleTransactionFromDb(currentReturnTransaction.getTicketNumber());
		if(saleTransaction == null) 
			return false;
		
		it.polito.ezshop.data.TicketEntry ticket = checkProductAmountInSaleTransaction(saleTransaction, barCode, amount);
    	if(ticket != null) {

//			add product to return transaction
			currentReturnTransaction.returnProduct(barCode, amount);
			
			double money =0;
			if(saleTransaction.getDiscountRate() > 0) 
				if(ticket.getDiscountRate() > 0) money = saleTransaction.getDiscountRate() * ticket.getDiscountRate() * ticket.getPricePerUnit() * amount;
				else money = saleTransaction.getDiscountRate() * ticket.getPricePerUnit() * amount;
			else 
				if(ticket.getDiscountRate() > 0) money = ticket.getDiscountRate() * ticket.getPricePerUnit() * amount;
				else money = ticket.getPricePerUnit() * amount;
				
			currentReturnTransaction.setMoney(money);
			return true;	
    	}
    	return false;
	}
    
    private it.polito.ezshop.data.TicketEntry checkProductAmountInSaleTransaction(it.polito.ezshop.data.model.SaleTransaction saleTransaction, 
    		String productCode, int amount) {
		boolean contains = false;
		it.polito.ezshop.data.TicketEntry ticket = null;
//    		sale transaction contains the product
		for(it.polito.ezshop.data.TicketEntry t : saleTransaction.getEntries()) {
			if(t.getBarCode().equalsIgnoreCase(productCode)) {
				ticket = t; 
				contains = true;
				break;
			}
		}
		if(!contains) return null;
		
//    		IMPORTANT : WITHOUT UPDATING THE QUNATITY OF PRODUCTS IN DB, WE STILL CAN RETURN THE PRODUCT MULTIPLE TIMES WITHOUT GETTING AN ERROR 
//    		if amount if higher than the amount in ticketentry
		if(ticket != null && ticket.getAmount() < amount)
			return null;
		
		return ticket;
    }

	@Override
	public boolean endReturnTransaction(Integer returnId, boolean commit)
			throws InvalidTransactionIdException, UnauthorizedException {
		
		if (loggedUser == null)
			throw new UnauthorizedException();
		
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();

//      same return transaction
        if(currentReturnTransaction.getId() == null || !currentReturnTransaction.getId().equals(returnId))
        	return false;
        
        try {
	        if(commit) {

	        	List<it.polito.ezshop.data.TicketEntry> tickets = it.polito.ezshop.data.model.TicketEntry.getTicketsFromDb(currentReturnTransaction.getTicketNumber());

	        	//update sale transaction (quantity of entries returned)
	        	for (Map.Entry<String, Integer> entry : currentReturnTransaction.getProducts().entrySet()) {
	        	    String productCode = entry.getKey();
	        	    int amount = entry.getValue();
	        	    for(it.polito.ezshop.data.TicketEntry ticket : tickets) { 
	        	    	if(ticket.getBarCode().equalsIgnoreCase(productCode)) {
	        	    		it.polito.ezshop.data.model.TicketEntry.updateAmountInDb(currentReturnTransaction.getTicketNumber(), ticket.getBarCode(), ticket.getAmount() - amount);
//	        	    		update product quantity 
	        	    		try {
	        	    			int id = products.stream().filter(product -> product.getBarCode().equalsIgnoreCase(ticket.getBarCode())).findFirst().get().getId();
								ProductType.updateQuantityInDb(id, amount);
								break;
							} catch (SQLException e) {
								return false;
							}
	        	    	}
	        	    };
	        	}
	        	
//	        	update return transaction 
	        	ReturnTransaction.updateReturnStatusInDb(returnId, new JSONObject(currentReturnTransaction.getProducts()).toString(), commit, ReturnTransaction.STATUS_UNPAID);
	        	
//	        	update balance 
//	        	add return transaction to account book
            	accountBook.addBalanceOperation(new it.polito.ezshop.data.model.BalanceOperation(currentReturnTransaction.getBalanceId(), LocalDate.now(), 
            		Math.abs(currentReturnTransaction.getMoney()), it.polito.ezshop.data.model.BalanceOperation.TYPE_RETURN, it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID));
            	return true;
	        }
	        else {
	        	currentReturnTransaction = new ReturnTransaction();
				ReturnTransaction.deleteReturnTransactionFromDb(returnId);
	        }
        } catch (SQLException e) {
        	return false;
		}
        return true;
	}
	
	/**
     * This method deletes a closed return transaction. It affects the quantity of product sold in the connected sale transaction
     * (and consequently its price) and the quantity of product available on the shelves.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the identifier of the return transaction to be deleted
     *
     * @return  true if the transaction has been successfully deleted,
     *          false   if it doesn't exist,
     *                  if it has been payed,
     *                  if there are some problems with the db
     *
     * @throws InvalidTransactionIdException if the transaction id is less than or equal to 0 or if it is null
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
	@Override
	public boolean deleteReturnTransaction(Integer returnId)
			throws InvalidTransactionIdException, UnauthorizedException {
		
		if (loggedUser == null)
			throw new UnauthorizedException();
		
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();
		
//      same return transaction
        if(currentReturnTransaction.getId() == null || !currentReturnTransaction.getId().equals(returnId))
        	return false;
        
        if(currentReturnTransaction.getStatus().equalsIgnoreCase(ReturnTransaction.STATUS_PAID))
        	return false;
        
//        double total = 0;
//        boolean revert = false;
        try {
        	List<it.polito.ezshop.data.TicketEntry> tickets = it.polito.ezshop.data.model.SaleTransaction.getSaleTransactionFromDb(currentReturnTransaction.getTicketNumber()).getEntries();
//	        	update sale transaction (quantity of entries after return transaction is deleted)
        	if(updateReturnQuantity(true, tickets)) return false;
        	
        	
//			update balance 
			it.polito.ezshop.data.model.BalanceOperation balanceOp = removeBalance(currentReturnTransaction.getBalanceId());
			if(balanceOp == null) return false;
			
			//delete transaction in db
			if(!ReturnTransaction.deleteReturnTransactionFromDb(currentReturnTransaction.getId())) {
				// revert 
				updateReturnQuantity(false, tickets);
				accountBook.addBalanceOperation(balanceOp);
			}
			currentReturnTransaction = new ReturnTransaction();
			return true;
        } catch (SQLException e) {
        	return false;
		}
	}
	
	private boolean updateReturnQuantity(boolean positiveAmount, List<it.polito.ezshop.data.TicketEntry> tickets) {
		try {
			boolean revert = false;
			for (Map.Entry<String, Integer> entry : currentReturnTransaction.getProducts().entrySet()) {
	    	    String productCode = entry.getKey();
	    	    int amount = entry.getValue();
	    	    
	    	    if(!positiveAmount) amount = -amount;
	    	    
	    	    for(it.polito.ezshop.data.TicketEntry ticket : tickets) {
	    	    	if(ticket.getBarCode().equalsIgnoreCase(productCode)) {
	    	    		ticket.setAmount(ticket.getAmount() + amount);
	    	    		//	    	        	update product quantity 
						if(!ProductType.updateQuantityInDb(products.stream()
								.filter(product -> product.getBarCode().equalsIgnoreCase(ticket.getBarCode()))
								.findFirst().get().getId(), -amount)) {
							revert = true;
						}
						if(revert) {
							ticket.setAmount(ticket.getAmount() - amount);
							return true;
						}
	    	    	}
	    	    }
				return false;
	    	}
		} catch (SQLException e) {
			return true;
		}
		return true;
	}

	// -------------------- FR7 ------------------- //
	// ------------------- ADMIN ------------------ //
	// --------------- SHOP MANAGER --------------- //
	// ------------------ CASHIER ----------------- //

	/**
	 * This method record the payment of a sale transaction with cash and returns
	 * the change (if present). This method affects the balance of the system. It
	 * can be invoked only after a user with role "Administrator", "ShopManager" or
	 * "Cashier" is logged in.
	 *
	 * @param transactionId the number of the transaction that the customer wants to
	 *                      pay
	 * @param cash          the cash received by the cashier
	 *
	 * @return the change (cash - sale price) -1 if the sale does not exists, if the
	 *         cash is not enough, if there is some problemi with the db
	 *
	 * @throws InvalidTransactionIdException if the number is less than or equal to
	 *                                       0 or if it is null
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 * @throws InvalidPaymentException       if the cash is less than or equal to 0
	 */
	@Override
	public double receiveCashPayment(Integer ticketNumber, double cash)
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {

		if (ticketNumber == null || ticketNumber <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if (cash <= 0)
			throw new InvalidPaymentException();

		double change = 0;

		if (currentSaleTransaction.getTicketNumber() == ticketNumber) {
//	        check if cash is enough 	
			change = cash - currentSaleTransaction.getPrice();
			if (change < 0)
				return -1;

////	    update status to payed 	
			try {
				if (it.polito.ezshop.data.model.SaleTransaction.updateSaleStatusInDb(ticketNumber,
						it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED)) {
					// update balance
					for (it.polito.ezshop.data.model.BalanceOperation balance : accountBook.getBalanceOperations()) {
						if (balance.getBalanceId() == currentSaleTransaction.getBalanceId()) {
							currentSaleTransaction.setStatus(it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED);
							balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID);
							BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), balance.getType());
							accountBook.recordBalanceUpdate(currentSaleTransaction.getPrice());
							break;
						}
					}

					return change;
				} else {
					return -1;
				}
			} catch (SQLException e) {
				return -1;
			}
		}

		return -1;
	}

	/**
	 * This method record the payment of a sale with credit card. If the card has
	 * not enough money the payment should be refused. The credit card number
	 * validity should be checked. It should follow the luhn algorithm. The credit
	 * card should be registered in the system. This method affects the balance of
	 * the system. It can be invoked only after a user with role "Administrator",
	 * "ShopManager" or "Cashier" is logged in.
	 *
	 * @param transactionId the number of the sale that the customer wants to pay
	 * @param creditCard    the credit card number of the customer
	 *
	 * @return true if the operation is successful false if the sale does not
	 *         exists, if the card has not enough money, if the card is not
	 *         registered, if there is some problem with the db connection
	 *
	 * @throws InvalidTransactionIdException if the sale number is less than or
	 *                                       equal to 0 or if it is null
	 * @throws InvalidCreditCardException    if the credit card number is empty,
	 *                                       null or if luhn algorithm does not
	 *                                       validate the credit card
	 * @throws UnauthorizedException         if there is no logged user or if it has
	 *                                       not the rights to perform the operation
	 */

	@Override
	public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

		if (ticketNumber == null || ticketNumber <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		// this algorithm doesn't work,i used another one
		
		if (!it.polito.ezshop.data.model.CreditCardCircuit.validateCreditCard(creditCard))
			throw new InvalidCreditCardException();


		if (currentSaleTransaction.getTicketNumber() == ticketNumber) {

////	    update status to payed 	
			try {
				if (it.polito.ezshop.data.model.SaleTransaction.updateSaleStatusInDb(ticketNumber,
						it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED)) {
					// update balance
					for (it.polito.ezshop.data.model.BalanceOperation balance : accountBook.getBalanceOperations()) {
						if (balance.getBalanceId() == currentSaleTransaction.getBalanceId()) {
							currentSaleTransaction.setStatus(it.polito.ezshop.data.model.SaleTransaction.STATUS_PAYED);
							balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID);
							BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), balance.getType());
							accountBook.recordBalanceUpdate(currentSaleTransaction.getPrice());
							// break;
							if (creditCards.pay(creditCard, currentSaleTransaction.getPrice())) {
								return true;

							} else {
								// revert balance
								balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID);
								accountBook.recordBalanceUpdate(currentSaleTransaction.getPrice());
							}
						}
					}
				}
				else {
					// rollback
					it.polito.ezshop.data.model.SaleTransaction.updateSaleStatusInDb(ticketNumber,
							it.polito.ezshop.data.model.SaleTransaction.STATUS_UNPAID);
				}
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}


    /**
     * This method record the payment of a closed return transaction with given id. The return value of this method is the
     * amount of money to be returned.
     * This method affects the balance of the application.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the id of the return transaction
     *
     * @return  the money returned to the customer
     *          -1  if the return transaction is not ended,
     *              if it does not exist,
     *              if there is a problem with the db
     *
     * @throws InvalidTransactionIdException if the return id is less than or equal to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
	@Override
	public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
		
		// Check logged user
		if (loggedUser == null)
			throw new UnauthorizedException();

		// Check transaction id
		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();

		if (currentReturnTransaction.getId().intValue() != returnId.intValue() || currentReturnTransaction.getStatus().equalsIgnoreCase(ReturnTransaction.STATUS_PAID)) 
			return -1;

//	    update status to payed 	
		try {
			currentReturnTransaction.setStatus(ReturnTransaction.STATUS_PAID);
			if (it.polito.ezshop.data.model.ReturnTransaction.updateReturnStatusInDb(currentReturnTransaction.getId(), 
					new JSONObject(currentReturnTransaction.getProducts()).toString(), currentReturnTransaction.isCommitted(), 
					currentReturnTransaction.getStatus())) {
				// update balance
				for (it.polito.ezshop.data.model.BalanceOperation balance : accountBook.getBalanceOperations()) {
					if (balance.getBalanceId() == currentReturnTransaction.getBalanceId()) {
						balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID);
						BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), balance.getType());
						accountBook.recordBalanceUpdate(-currentReturnTransaction.getMoney());
						break;
					}
				}

				return currentReturnTransaction.getMoney();
			} else {
				return -1;
			}
		} catch (SQLException e) {
			return -1;
		}
	}

    /**
     * This method record the payment of a return transaction to a credit card.
     * The credit card number validity should be checked. It should follow the luhn algorithm.
     * The credit card should be registered and its balance will be affected.
     * This method affects the balance of the system.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the id of the return transaction
     * @param creditCard the credit card number of the customer
     *
     * @return  the money returned to the customer
     *          -1  if the return transaction is not ended,
     *              if it does not exist,
     *              if the card is not registered,
     *              if there is a problem with the db
     *
     * @throws InvalidTransactionIdException if the return id is less than or equal to 0
     * @throws InvalidCreditCardException if the credit card number is empty, null or if luhn algorithm does not
     *                                      validate the credit card
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
	@Override
	public double returnCreditCardPayment(Integer returnId, String creditCard)
			throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {


		if (returnId == null || returnId <= 0)
			throw new InvalidTransactionIdException();

		if (loggedUser == null)
			throw new UnauthorizedException();

		if (!it.polito.ezshop.data.model.CreditCardCircuit.validateCreditCard(creditCard))
			throw new InvalidCreditCardException();

		if (currentReturnTransaction.getId() != returnId || currentReturnTransaction.getStatus().equalsIgnoreCase(ReturnTransaction.STATUS_PAID)) 
			return -1;
		
//	    update status to payed 	
		try {
			currentReturnTransaction.setStatus(ReturnTransaction.STATUS_PAID);
			if (ReturnTransaction.updateReturnStatusInDb(currentReturnTransaction.getId(),
					new JSONObject(currentReturnTransaction.getProducts()).toString(), currentReturnTransaction.isCommitted(),
					currentReturnTransaction.getStatus())) {
				
				// update balance
				for (it.polito.ezshop.data.model.BalanceOperation balance : accountBook.getBalanceOperations()) {
					if (balance.getBalanceId() == currentReturnTransaction.getBalanceId()) {
						balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID);
						BalanceOperation.storeBalanceInDb(balance.getBalanceId(), balance.getMoney(), balance.getType());
						accountBook.recordBalanceUpdate(-currentReturnTransaction.getMoney());
						// break;
						if (creditCards.pay(creditCard, currentReturnTransaction.getMoney())) {
							return currentReturnTransaction.getMoney();

						} else {
							// revert balance
							balance.setStatus(it.polito.ezshop.data.model.BalanceOperation.STATUS_UNPAID);
							accountBook.recordBalanceUpdate(currentReturnTransaction.getMoney());
						}
					}
				}
			}
			else {
				currentReturnTransaction.setStatus(ReturnTransaction.STATUS_UNPAID);
				// rollback
				ReturnTransaction.updateReturnStatusInDb(currentReturnTransaction.getId(),
						new JSONObject(currentReturnTransaction.getProducts()).toString(), currentReturnTransaction.isCommitted(),
						currentReturnTransaction.getStatus());
			}
		} catch (SQLException e) {
			return -1;
		}
		return -1;
	}
    
    // -------------------- FR8 ------------------- //
    // ------------------- ADMIN ------------------ //
    // --------------- SHOP MANAGER --------------- //

    /**
     * This method record a balance update. <toBeAdded> can be both positive and nevative. If positive the balance entry
     * should be recorded as CREDIT, if negative as DEBIT. The final balance after this operation should always be
     * positive.
     * It can be invoked only after a user with role "Administrator", "ShopManager" is logged in.
     *
     * @param toBeAdded the amount of money (positive or negative) to be added to the current balance. If this value
     *                  is >= 0 than it should be considered as a CREDIT, if it is < 0 as a DEBIT
     *
     * @return  true if the balance has been successfully updated
     *          false if toBeAdded + currentBalance < 0.
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {

        if (loggedUser == null || (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
                && !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
            throw new UnauthorizedException();

    	try {
        
	        if(accountBook.recordBalanceUpdate(toBeAdded)) {
	        	int balanceId = BalanceOperation.incrementCounter() <= accountBook.getBalanceOperations().size() ? accountBook.getBalanceOperations().size() + 1 : BalanceOperation.incrementCounter();
					accountBook.addBalanceOperation(new it.polito.ezshop.data.model.BalanceOperation(balanceId, LocalDate.now(), Math.abs(toBeAdded), 
						toBeAdded >= 0 ? it.polito.ezshop.data.model.BalanceOperation.TYPE_CREDIT : it.polito.ezshop.data.model.BalanceOperation.TYPE_DEBIT, 
						it.polito.ezshop.data.model.BalanceOperation.STATUS_PAID));
				return true;
	        }
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    	return false;
    }

    /**
     * This method returns a list of all the balance operations (CREDIT,DEBIT,ORDER,SALE,RETURN) performed between two
     * given dates.
     * This method should understand if a user exchanges the order of the dates and act consequently to correct
     * them.
     * Both <from> and <to> are included in the range of dates and might be null. This means the absence of one (or
     * both) temporal constraints.
     *
     *
     * @param from the start date : if null it means that there should be no constraint on the start date
     * @param to the end date : if null it means that there should be no constraint on the end date
     *
     * @return All the operations on the balance whose date is <= to and >= from
     *
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public List<it.polito.ezshop.data.BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {

    	if (loggedUser == null|| (!(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_ADMINISTRATOR))
                && !(loggedUser.getRole().equalsIgnoreCase(it.polito.ezshop.data.model.User.ROLE_SHOPMANAGER))))
            throw new UnauthorizedException();
    	
        return accountBook.getCreditsAndDebits(from, to);
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return accountBook.getBalance();
    }

    private void checkProductCodeValidity(String productCode) throws InvalidProductCodeException {

        if (productCode == null || productCode.isEmpty())
            throw new InvalidProductCodeException();

        // convert productCode to array of chars
        char[] charArray = productCode.toCharArray();
        // check if all chars are between 0 and 9, if not throw the exception
        for (int i = 0; i < charArray.length; i++) {
            if (!(charArray[i] >= '0' && charArray[i] <= '9')) {
                throw new InvalidProductCodeException();
            }
        }

        // check validity of productCode
        int l = productCode.length();

        if (l < 12 || l > 14)
            throw new InvalidProductCodeException();

        int sum = 0;

        for (int i = l - 2; i >= 0; i--) {
            int j = (l - 2) - i;
            if (j % 2 == 0) {
                sum += Character.getNumericValue(productCode.charAt(i)) * 3;
            } else {
                sum += Character.getNumericValue(productCode.charAt(i));
            }
        }

        int multipleOfTen = sum;

        while (multipleOfTen % 10 != 0) {
            multipleOfTen++;
        }

        if ((multipleOfTen - sum) != Integer.parseInt(productCode.substring(l-1)))
            throw new InvalidProductCodeException();
        // end of product code validation
        return;
    }


}
