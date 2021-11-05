# Design Document 


Authors:Ehsan Ansari Nejad (s288903), Takla Trad (s289222)

Date: 19/05/2021

Version: 1.2


# Contents

- [Design Document](#design-document)
- [Contents](#contents)
- [Instructions](#instructions)
- [High level design](#high-level-design)
- [Low level design](#low-level-design)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

We're using a 3 layers architecture (it.polito.ezshop.GUI for the presentation layer, it.polito.ezshop.data for the application logic layer, it.polito.ezshop.model for the data layer).

```plantuml
package it.polito.ezshop.data
package it.polito.ezshop.exceptions
package it.polito.ezshop.data.model
package it.polito.ezshop.GUI
package it.polito.ezshop.utils

it.polito.ezshop.data -down-> it.polito.ezshop.exceptions
it.polito.ezshop.data -down-> it.polito.ezshop.data.model
it.polito.ezshop.GUI -down->it.polito.ezshop.data
it.polito.ezshop.data.model -down-> it.polito.ezshop.utils
```

The package it.polito.ezshop.utils is used to manage persistency through a database. The interactions with the database will not be explained in details, instead classes whose data should be made persistent will be pointed out and will show proper methods to fetch/store data from/to the database.

# Low level design

```plantuml
scale 1024 width 
scale 800 height 
package it.polito.ezshop.data{
    interface EZShopInterface
    class Shop
}
package it.polito.ezshop.data.model{
    class ProductType
    class User
    class Order
    class Customer
    class CustomerCard
    class SaleTransaction
    class ReturnTransaction
    class TicketEntry
    class BalanceOperation
    class AccountBook
    class CreditCardCircuit
    note "Persistent classes \nare: ReturnTransaction,\n SaleTransaction,\n Customer,\n CustomerCard,\n Order,\n ProductType,\n User, \nBalanceOperation, \nTicketEntry" as N1
}
interface EZShopInterface{
    +reset() : void
    +createUser(String username, String password, String role) : Integer
    +deleteUser(Integer id) : boolean
    +getAllUsers() : List<User>
    +getUser(Integer id) : User
    +updateUserRights(Integer id, String role) : boolean
    +login(String username, String password) : User
    +logout() : boolean
    +createProductType(String description, String productCode, double pricePerUnit, String note) : Integer
    +updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) : boolean
    +deleteProductType(Integer id) : boolean
    +getAllProductTypes() : List <ProductType>
    +getProductTypeByBarCode(String barCode) : ProductType
    +getProductTypesByDescription(String description) : List<ProductType>
    +updateQuantity(Integer productId, int toBeAdded) : boolean
    +updatePosition(Integer productId, String newPos) : boolean
    +issueOrder(String productCode, int quantity, double pricePerUnit) : Integer
    +payOrderFor(String productCode, int quantity, double pricePerUnit) : Integer
    +payOrder(Integer orderId) : boolean
    +recordOrderArrival(Integer orderId) : boolean
    +getAllOrders() : List<Order>
    +defineCustomer(String customerName) : Integer
    +modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) : boolean
    +deleteCustomer(Integer id) : boolean
    +getCustomer(Integer id) : Customer
    +getAllCustomers() : List<Customer>
    +createCard() : String
    +attachCardToCustomer(String customerCard, Integer customerId) : boolean
    +modifyPointsOnCard(String customerCard, int pointsToBeAdded) : boolean
    +startSaleTransaction() : Integer
    +addProductToSale(Integer transactionId, String productCode, int amount) : boolean
    +deleteProductFromSale(Integer transactionId, String productCode, int amount) : boolean
    +applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) : boolean
    +applyDiscountRateToSale(Integer transactionId, double discountRate) : boolean
    +computePointsForSale(Integer transactionId) : int
    +endSaleTransaction(Integer transactionId) : boolean
    +deleteSaleTransaction(Integer transactionId) : boolean
    +getSaleTransaction(Integer transactionId) : SaleTransaction
    +startReturnTransaction(Integer transactionId) : Integer
    +returnProduct(Integer returnId, String productCode, int amount) : boolean
    +endReturnTransaction(Integer returnId, boolean commit) : boolean
    +deleteReturnTransaction(Integer returnId) : boolean
    +receiveCashPayment(Integer transactionId, double cash) : double
    +receiveCreditCardPayment(Integer transactionId, String creditCard) : boolean
    +returnCashPayment(Integer returnId) : double
    +returnCreditCardPayment(Integer returnId, String creditCard) : double
    +recordBalanceUpdate(double toBeAdded) : boolean
    +getCreditsAndDebits(LocalDate from, LocalDate to) : List<BalanceOperation>
    +computeBalance() : double
}
class Shop{
    -loggedUser : User
    -currentSaleTransaction : SaleTransaction
    -currentReturnTransaction : ReturnTransaction
    -products : List<ProductType>
    -accountBook : AccountBook
    -creditCards : CreditCardCircuit 
}

class User{
    -username : String
    -password : String
    -role : String
    -id : Integer
    +storeUserInDb(String username, String password, String role) : Integer
    +deleteUserFromDb(Integer id) : boolean
    +getAllUsersFromDb(): List<User>
    +getUserFromDb(Integer id): User
    +updateUserRightsInDb(Integer id, String role) : boolean
    +authenticateFromDb(String username, String password) : User

}
class ProductType{
    -productDescription : String
    -barCode : String
    -pricePerUnit : double
    -note : String
    -id : Integer
    -quantity : int
    -location : String
    +resetProductTypes() : boolean
    +storeProductTypeInDb(String description, String productCode, double pricePerUnit, String note) : Integer
    +updateProductTypeInDb(Integer id, String newDescription, String newCode, double newPrice, String newNote) : boolean
    +deleteProductTypeFromDb(Integer id) : boolean
    +getAllProductTypesFromDb() : List<ProductType>
    +getProductTypeByBarCodeFromDb(String barCode) : ProductType
    +getProductTypesByDescriptionFromDb(String description) : List<ProductType>
    +updateQuantityInDb(Integer productId, int toBeAdded) : boolean
    +updatePositionInDb(Integer productId, String newPos) : boolean
}
class Order{
    -quantity : int
    -pricePerUnit : double
    -orderId : Integer
    -status : String 
    -productCode : String
    -balanceId : Integer
    +issueOrderInDb(String productCode, int quantity, double pricePerUnit) : Integer
    +payOrderInDb(Integer orderId) : boolean
    +recordOrderArrivalInDb(Integer orderId) : boolean
    +updateOrderInDb(Integer orderId, String status) : boolean
    +getAllOrdersFromDb() : List<Order>
    +resetOrders() : void
}
class Customer{
    -id : Integer
    -name : String
    -card : CustomerCard
    -points : Integer
    +storeCustomerInDb(String customerName) : Integer
    +modifyCustomerInDb(Integer id, String newCustomerName, String newCustomerCard) : boolean
    +deleteCustomerFromDb(Integer id) : boolean
    +getCustomerFromDb(Integer id) : Customer
    +getAllCustomersFromDb() : List<Customer>
    +attachCardToCustomerInDb(String customerCard, Integer customerId) : boolean
}
class CustomerCard{
    '-id : String
    '-points : int
    +storeCardInDb() : String
    +modifyPointsOnCardInDb(String customerCard, int pointsToBeAdded) : boolean
}
class SaleTransaction{
    -prodList: List<TicketEntry>
    -discountRate : double
    -status : String
    -ticketNumber : Integer
    -price : double
    -balanceId : Integer
    +getLastIdFromSaleTransactions() : int
    +storeSaleInDb(double discountRate, double price, String status) : Integer
    +getSaleTransactionFromDb(Integer transactionId) : SaleTransaction
    +updateSaleStatusInDb(Integer ticketNumber, String status) : boolean
    +deleteSaleTransactionFromDb(Integer ticketNumber) : boolean
    +computePointsForSale(Integer transactionId) : int
    +resetSaleTransaction() : boolean
}

class ReturnTransaction{
    -id : int
    -ticketNumber : int
    -barCode : String
    -amount : int
    -committed : boolean
    -prodList: List<TicketEntry>
    +storeReturnInDb(int ticketNumber, String barCode, String amount, int committed) : Integer
    +resetReturnTransaction() : boolean
}
' +startReturnTransaction(Integer transactionId) : Integer
    ' +returnProduct(Integer returnId, String productCode, int amount) : boolean
    ' +endReturnTransactionInDb(Integer returnId, boolean commit) : boolean
    ' +deleteReturnTransactionFromDb(Integer returnId) : boolean
    ' +returnCashPayment(Integer returnId) : double
    ' +returnCreditCardPayment(Integer returnId, String creditCard) : double

class TicketEntry {
    - barCode: String
    - amount: int
    - discountRate: double
    - pricePerUnit: double
    - productDescription: String
    + storeTicketEntryInDb(Integer ticketNumber, String barCode, double discountRate, double amount, double pricePerUnit, String description) : Integer
    + getTicketsFromDb(Integer ticketNumber) : List<TicketEntry>
}

class BalanceOperation{
    -balanceId : Integer
    -money : double
    -date: LocalDate
    -type: String
    -status: String
    +resetBalanceOperations() : void
    +storeBalanceInDb(int id, double money, String type) : Integer
    +getCreditsAndDebitsFromDb() : List<BalanceOperation>
    +deleteBalanceFromDb(Integer id) : boolean
}
class AccountBook{
    -balance : double
    -balanceOperations : List<BalanceOperation>
    +addBalanceOperation(BalanceOperation balance) : boolean
    +removeBalanceOperation(BalanceOperation balance) : boolean
    +recordBalanceUpdate(double toBeAdded) : boolean
    +getCreditsAndDebits(LocalDate from, LocalDate to) : List<BalanceOperation>
    +computeBalance(List<BalanceOperation>) : void
    +getBalance(): double
    +resetBalance() : void
}
class CreditCardCircuit {
    -creditCardMoney : Map<String, Double>
    -fileName : String
    +validateCreditCard(String creditCard) : boolean
    +isValid(String creditCard) : boolean
    +pay(String creditCard, double money) : boolean
}

Shop --> EZShopInterface : implements
Shop -- User 
Shop -- ProductType
Order --^ BalanceOperation : extends
ReturnTransaction --^ BalanceOperation : extends
SaleTransaction --^ BalanceOperation : extends
Shop -- Order
Shop -- Customer
Customer -- "0...1" CustomerCard
Shop -- CustomerCard
Shop -- SaleTransaction
SaleTransaction --> TicketEntry
TicketEntry --> ProductType
Shop -- ReturnTransaction
ReturnTransaction --> TicketEntry
Shop -- AccountBook
Shop -- CreditCardCircuit
AccountBook -- "*" BalanceOperation
```





# Verification traceability matrix

| | Shop | ProductType | User | Order | Customer | CustomerCard | SaleTransaction | ReturnTransaction | BalanceOperation | AccountBook | TicketEntry |
| ----------- | :-------------:| :-------------:| :-------------:| :------------:| :-------------:| :-------------:| :------------:| :-------------:| :-------------:| :-------------:| :-------------:| 
|  FR1    | X | | X |
| FR3     | X | X
| FR4.1   | X | X
| FR4.2   | X | X
| FR4.3   | X | | | X
| FR4.4   | X | | | X | | | | | X | 
| FR4.5   | X | | | X | | | | | X | 
| FR4.6   | X | X | | X 
| FR4.7   | X | | | X
| FR5.1   | X | | | | X | X
| FR5.2   | X | | | | X
| FR5.3   | X | | | | X
| FR5.4   | X | | | | X
| FR5.5   | X | | | | | X
| FR5.6   | X | | | | X | X
| FR5.7   | X | | | | | X
| FR6.1   | X | | | | | | X 
| FR6.2   | X | X | | | | | X  
| FR6.3   | X | X | | | | | X  
| FR6.4   | X | | | | | | X
| FR6.5   | X | | | | | | | | | | X   
| FR6.6   | X | X | | | | | X | | | | X   
| FR6.7   | X | X
| FR6.8   |  
| FR6.10  | X | | | | | | X
| FR6.11  | X | | | | | | X
| FR6.12  | X | | | | | | | X
| FR6.13  | X | X | | | | | X | X
| FR6.14  | X | X | | | | | | X
| FR6.15  | X | X | | | | | | X
| FR7.1   | X | X | | | | | X | | X | X
| FR7.2   | X | X | | | | | X | | X | X
| FR7.3   | X | | | | | | | X | X 
| FR7.4   | X | | | | | | | X | X 
| FR8.1   | X | | | | | | | | X | X
| FR8.2   | X | | | | | | | | X | X
| FR8.3   | X | | | | | | | | X
| FR8.4   | X | | | | | | | | X | X



# Verification sequence diagrams 

Scenario 1-1
```plantuml
actor User as u
participant Shop as s
participant ProductType as pt
database Database as db
u -> s : 1. inserts product data
s -> s : 2. check for UnauthorizedException
s -> s : 3. check for InvalidProductCodeException
s -> s : 4. check for InvalidPricePerUnitException
s -> s : 5. check for InvalidProductDescriptionException
s -> pt : 6. storeProductTypeInDb(String description, String productCode, double pricePerUnit, String note)
pt -> db : 7. queries and checks on db
pt <-- db : 8. return Integer id or -1 if there's an error
s <-- pt : 9. return Integer id or -1 if there's an error
u <--s : 10. show outcome of operation
```

Scenario 1-2
```plantuml
actor User as u
participant Shop as s
participant ProductType as pt
database Database as db
u -> s : 1. inserts product data
s -> s : 2. check for UnauthorizedException
s -> s : 3. check for InvalidProductIdException
s -> s : 4. check for InvalidLocationException
s -> pt : 5. updatePositionInDb(Integer productId, String newPos)
pt -> db : 6. queries and checks on db
pt <-- db : 7. return boolean with outcome of operation
s <-- pt : 8. return boolean with outcome of operation
u <--s : 9. show outcome of operation
```

Scenario 2-1
```plantuml
actor User as u
participant Shop as s
participant User as up
database Database as db
u -> s : 1. inserts user data
s -> s : 2. check for InvalidUsernameException
s -> s : 3. check for InvalidPasswordException
s -> s : 4. check for InvalidRoleException
s -> up : 5. storeUserInDb(String username, String password, String role)
up -> db : 6. queries and checks on db
up <-- db : 7. return Integer id or -1 if there's an error
s <-- up : 8. return Integer id or -1 if there's an error
u <--s : 9. show outcome of operation
```

Scenario 2-3
```plantuml
actor User as u 
participant shop as s 
participant User as um
database Database as db
u -> s : 1. selects user to modify 
s -> s : 2. check for InvalidUserIdException
s -> s : 3. check for InvalidRoleException
s -> s : 4. check for UnauthorizedException
s -> um : 5. updateUserRightsInDb(Integer id, String role)
um -> db : 6. query executed 
um <-- db : 7. return ...
s <-- um : 8. return true on success or false if there's an error
u <--s : 9. show outcome of operation
```

Scenario 3-3
```plantuml
actor User as u 
participant shop as s 
participant Order as o
database Database as db
u -> s : 1. updates ProductType available quantity 
s -> s : 2. check for InvalidProductIdException
s -> s : 3. check for UnauthorizedException
s -> o : 4. updateQuantityInDb(Integer productId, int toBeAdded)
o -> db : 5. query executed 
o <-- db : 6. return ...
s <-- o : 7. return true on success or false if there's an error
u <--s : 8. show outcome of operation
```
Scenario 4-1
```plantuml

actor User
activate Shop

database Database
User ->Shop:defineCustomer()
activate Database
Shop -> Database:new Customer()
Shop <-- Database:Confirm
deactivate Database
Shop -->User:customer added
deactivate Shop

```
Scenario 5-1
```plantuml
actor User
activate Shop
User->Shop:Insert username
User->Shop:Insert password
database database
activate database
Shop ->database:getuserFromDb()
database --> Shop:user exists
deactivate database
Shop --> User:logged in
Shop --> Shop:Display account

```
Scenario 6-1
```plantuml
actor Cashier
activate Shop
Cashier->Shop:startSaleTransaction()
activate SaleTransaction

loop number of products
Shop->ProductType:getProductTypeByBarcode()
activate ProductType
ProductType-->Shop:return productType
Cashier->Shop:add N units of N
Shop->SaleTransaction:addProductToSale()
Shop -> ProductType:updateQuantity()
ProductType-->ProductType:deduce N units
deactivate ProductType
end
Cashier -> Shop:endSaleTransactio()

Shop -> Cashier:payment type
Cashier-->Shop:receiveCreditCardPayment()
Shop -> Shop: validate card
Shop -> SaleTransaction: receiveCreditCardPayment()
SaleTransaction -->Shop: paid
deactivate SaleTransaction
Shop -> Cashier:operation is successful
activate AccountBook
Shop->AccountBook:recordBalanceUpdate()
deactivate


```
Scenario 6-2
```plantuml
actor Cashier
activate Shop
Cashier->Shop:startSaleTransaction()
activate SaleTransaction

loop number of products
Shop->ProductType:getProductTypeByBarcode()
activate ProductType
ProductType-->Shop:return productType
Cashier->Shop:add N units of N
Cashier -> Shop:applyDiscountRateToProduct()
Shop -> SaleTransaction:applyDiscountRateToProduct()
Shop->SaleTransaction:addProductToSale()
Shop -> ProductType:updateQuantity()
ProductType-->ProductType:deduce N units
deactivate ProductType
end
Cashier -> Shop:endSaleTransactio()

Shop -> Cashier:payment type
Cashier-->Shop:receiveCreditCardPayment()
Shop -> Shop: validate card
Shop -> SaleTransaction: receiveCreditCardPayment()
SaleTransaction -->Shop: paid
deactivate SaleTransaction
Shop -> Cashier:operation is successful
activate AccountBook
Shop->AccountBook:recordBalanceUpdate()
deactivate


```
Scenario 6-4
```plantuml
actor Cashier
activate Shop
Cashier->Shop:startSaleTransaction()
activate SaleTransaction

loop number of products
Shop->ProductType:getProductTypeByBarcode()
activate ProductType
ProductType-->Shop:return productType
Cashier->Shop:add N units of N
Shop->SaleTransaction:addProductToSale()
Shop -> ProductType:updateQuantity()
ProductType-->ProductType:deduce N units
deactivate ProductType
end
Cashier -> Shop:endSaleTransactio()

Shop -> Cashier:payment type
Cashier-->Shop:receiveCreditCardPayment()
Shop -> Shop: validate card
Shop -> SaleTransaction: receiveCreditCardPayment()
SaleTransaction -->Shop: paid
deactivate SaleTransaction

Shop ->Customer:getCustomer()
database Database
Customer->Database:getCustomerFromDb()
Database -->Customer:Customer data
Customer -> CustomerCard:modifyPointsOnCardInDb()

Shop -> Cashier:operation is successful
activate AccountBook
Shop->AccountBook:recordBalanceUpdate()
deactivate

```
Scenario 7-1
```plantuml
actor User
activate Shop
User -> Shop: receiveCreditCardPayment()
Shop -> Shop: validate card
Shop --> User:amount of money to pay
User --> Shop:sale price
Shop --> User: price payed
activate AccountBook
Shop -> AccountBook:recordBalanceUpdate()
deactivate AccountBook
Shop --> User: exit with success
deactivate Shop

```
Scenario 9-1
```plantuml
actor User
Activate Shop
User -> Shop: getCreditsAndDebits()
activate AccountBook
Shop -> AccountBook:getcreditsAndDebits()
AccountBook --> Shop: list<BalanceOperation>
deactivate AccountBook
Shop -> Shop:display

```
Scenario 10-1
```plantuml
actor User
activate Shop
User ->Shop: insertCreditCradNumber
Shop -> Shop:validate card
User -> Shop:returnCreditCardPayment()
activate ReturnTransaction
Shop -> ReturnTransaction:returnCreditCardPayment()
ReturnTransaction->AccountBook:recordBalanceUpdate()
ReturnTransaction --> Shop:returned
deactivate ReturnTransaction
Shop -> User:exit with success



```
