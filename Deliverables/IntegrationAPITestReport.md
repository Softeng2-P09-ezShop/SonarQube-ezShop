# Integration and API Test Documentation

Authors:Ehsan Ansari Nejad (s288903), Takla Trad (s289222)

Date:19/05/2021

Version:1.1

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

```plantuml
	@startuml

	class EZShop

    class AccountBook 
    class Order 
    class ReturnTransaction
    class SaleTransaction
    class BalanceOperation
    class CreditCardCircuit
    class Customer
    class CustomerCard
    class ProductType 
    class TicketEntry 

	EZShop -down-> AccountBook
	EZShop -down-> Order
	EZShop -down-> ReturnTransaction
	EZShop -down-> SaleTransaction
	EZShop -down-> BalanceOperation
	EZShop -down-> CreditCardCircuit
	EZShop -down-> Customer
	EZShop -down-> CustomerCard
	EZShop -down-> ProductType
	EZShop -down-> TicketEntry

	AccountBook -down-> BalanceOperation
	Order -down-> BalanceOperation
	ReturnTransaction -down-> BalanceOperation
	SaleTransaction -down-> BalanceOperation

	Order -down-> ProductType

	SaleTransaction -down-> TicketEntry

	@enduml
```

# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>


The tests we made are using the bottom up strategy.

| Step |                        Tested classes                        |
| ---- | :----------------------------------------------------------: |
| 1    |                         ProductType                          |
|      |                       BalanceOperation                       |
|      |                         ticketEntry                          |
|      |                      CreditCardCircuit                       |
|      |                           Customer                           |
|      |                         Customercard                         |
|      |                       SaleTransaction                        |
|      |                      ReturnTransaction                       |
|      |                            Order                             |
|      |                          User<br />                          |
| 2    |                 AccountBook+BalanceOperation                 |
|      |              Order+BalanceOperation+ProductType              |
|      |              Returntransaction+BalanceOperation              |
|      |      SaleTransaction+BalanceOperation+TicketEntry<br />      |
| 3    | EZShop+AccountBook+SaleTransaction+Returntransaction+Order<br />+BalanceOperation+User+Customer+CustomerCard+CreditCardCircuit<br />+TicketEntry+ProductType |



#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1

Step 1 tests are included on the unit test document.


## Step 2
| Classes  | JUnit test cases |
|--|--|
|AccountBook+BalanceOperation|AccountBookTest.testValidUNPAIDAddBalanceOperation()|
||AccountBookTest.testValidPAIDAddBalanceOperation()|
||AccountBookTest.testNullAddBalanceOperation()|
||AccountBookTest.testValidUNPAIDRemoveBalanceOperation()|
||AccountBookTest.testValidPAIDRemoveBalanceOperation()|
||AccountBookTest.testNullRemoveBalanceOperation()|
||AccountBookTest.testNullComputeBalance()|
||AccountBookTest.testValidComputeBalance()|
||AccountBookTest.testNullSetBalanceOperations()|
||AccountBookTest.testValidSetBalanceOperations()|
||AccountBookTest.testNullFROMTOGetCreditsAndDebits()|
||AccountBookTest.testNullFROMGetCreditsAndDebits()|
||AccountBookTest.testNullTOGetCreditsAndDebits()|
||AccountBookTest.testFROMTOGetCreditsAndDebits()|
|Order+BalanceOperation+ProductType|OrderTest.testUNPAIDOrderArrival()|
||OrderTest.ttestPAIDOrderArrival()|
||OrderTest.testBalanceAndOrderDependency()|
|Returntransaction+BalanceOperation|ReturntransactionTest.testReturnTransactionConstructor()|
|SaleTransaction+BalanceOperation<br />+TicketEntry|SaleTransactionTest.testBalanceOperationSaleTransactionConstructor()|
||SaleTransactionTest.testNullSetEntries()|
||SaleTransactionTest.testEmptyListSetEntries()|
||SaleTransactionTest.testValidSetEntries()|
||SaleTransactionTest.testNullBarcodeAddProductToSale()|
||SaleTransactionTest.testNullDescriptionAddProductToSale()|
||SaleTransactionTest.testZeroPricePerUnitAddProductToSale()|
||SaleTransactionTest.testZeroAmountAddProductToSale()|
||SaleTransactionTest.testInvalidBarcodeAddProductToSale()|
||SaleTransactionTest.testInvalidDescriptionAddProductToSale()|
||SaleTransactionTest.testValidAddProductToSale()|


## Step 3 

   

| Classes  | JUnit test cases |
|--|--|
|EZShop+AccountBook+SaleTransaction+<br />Returntransaction+Order<br />+BalanceOperation+User+Customer+<br />CustomerCard+CreditCardCircuit<br />+TicketEntry+ProductType|EZShopTest.testWrongParametersReturnCreditCardPayment|
||EZShopTest.testCreateCard()|
||EZShopTest.testCreateUser()|
||EZShopTest.testDeleteUser()|
||EZShopTest.testLogout()|
||EZShopTest.testLogin()|
||EZShopTest.testGetAllUsers()|
||EZShopTest.testGetUser()|
||EZShopTest.testPayOrder()|
||EZShopTest.testPayOrderFor()|
||EZShopTest.testGetAllOrders()|
||EZShopTest.testIssueOrder()|
||EZShopTest.testReset()|
||EZShopTest.testGetCustomer()|
||EZShopTest.testUnauthorizedDeleteSaleTransaction()|
||EZShopTest.testWrongParametersModifyPointsOnCard()|
||EZShopTest.testWrongParametersAttachCardToCustomer()|
||EZShopTest.testWrongParametersDeleteReturnTransaction()|
||EZShopTest.testWrongParametersReceiveCashPayment()|
||EZShopTest.testInvalidDescriptionCreateProduct()|
||EZShopTest.testInvalidLocationUpdateLocation()|
||EZShopTest.testUnauthorizedModifyPointsOnCard()|
||EZShopTest.testUnauthorizedDeleteProductType()|
||EZShopTest.testUnauthorizedDeleteProductFromSale()|
||EZShopTest.testRevertDeleteReturnTransaction()|
||EZShopTest.testNoEnoughMoneyReceiveCreditCardPayment()|
||EZShopTest.testUnauthorizedApplyDiscountRateToSale()|
||EZShopTest.testUnauthorizedGetSaleTransaction()|
||EZShopTest.testInvalidPricePerUnitCreateProduct()|
||EZShopTest.testWrongLoginAttachCardToCustomer()|
||EZShopTest.testWrongLoginDeleteReturnTransaction()|
||EZShopTest.testWrongParametersReceiveCreditCardPayment()|
||EZShopTest.testUnauthorizedComputePointsForSale()|
||EZShopTest.testWrongParametersReturnCashPayment()|
||EZShopTest.testWrongLoginReturnCreditCardPayment()|
||EZShopTest.testUnauthorizedStartSaleTransaction()|
||EZShopTest.testUnauthorizedAttachCardToCustomer()|
||EZShopTest.testUnauthorizedEndReturnTransaction()|
||EZShopTest.testUnauthorizedReceiveCreditCardPayment()|
||EZShopTest.testWrongLoginRecordBalanceUpdate()|
||EZShopTest.testUnauthorizedGetProductTypesByDescription()|
||EZShopTest.testUnauthorizedApplyDiscountRateToProduct()|
||EZShopTest.testUnauthorizedRecordOrderArrival()|
||EZShopTest.testUnauthorizedEndSaleTransaction()|
||EZShopTest.testUnauthorizedStartReturnTransaction()|
||EZShopTest.testInvalidDescriptionUpdateProduct()|
||EZShopTest.testInvalidPricePerUnitUpdateProduct()|
||EZShopTest.testUnauthorizedGetAllProductTypes()|
||EZShopTest.testInvalidIdGetUser()|
||EZShopTest.testUnauthorizedGetUser()|
||EZShopTest.testUsernameNullLogin()|
||EZShopTest.testPasswordEmptyLogin()|
||EZShopTest.testCreateProduct()|
||EZShopTest.testInvalidRoleCreateUser()|
||EZShopTest.testUpdateLocation()|
||EZShopTest.testWrongPasswordLogin()|
||EZShopTest.testUnauthorizedCreateProduct()|
||EZShopTest.testInvalidIdUpdateLocation()|
||EZShopTest.testInvalidUsernameCreateUser()|
||EZShopTest.testUnauthorizedGetAllUsers()|
||EZShopTest.testInvalidRoleUpdateUserRole()|
||EZShopTest.testUnauthorizedUpdateLocation()|
||EZShopTest.testInvalidIdUpdateProduct()|
||EZShopTest.testInvalidIdDeleteUser()|
||EZShopTest.testUsernameEmptyLogin()|
||EZShopTest.testUnauthorizedUpdateProduct()|
||EZShopTest.testUpdateUserRole()|
||EZShopTest.testUpdateProduct()|
||EZShopTest.testUnauthorizedLogout()|
||EZShopTest.testDeleteProductType()|
||EZShopTest.testPasswordNullLogin()|
||EZShopTest.testGetAllProductTypes()|
||EZShopTest.testInvalidPasswordCreateUser()|
||EZShopTest.testUnauthorizedDeleteUser()|
||EZShopTest.testUnauthorizedUpdateUserRole()|
||EZShopTest.testInvalidIdUpdateUserRole()|
||EZShopTest.testGetProductTypesByDescription()|
||EZShopTest.testUnauthorizedPayOrder()|
||EZShopTest.testUpdateQuantity()|
||EZShopTest.testUnauthorizedPayOrderFor()|
||EZShopTest.testUnauthorizedGetAllOrders()|
||EZShopTest.testUnauthorizedIssueOrder()|
||EZShopTest.testRecordOrderArrival()|
||EZShopTest.testDefineCustomer()|
||EZShopTest.testUnauthorizedUpdateQuantity()|
||EZShopTest.testAddProductToSale()|
||EZShopTest.testEndSaleTransaction()|
||EZShopTest.testUnauthorizedReturnProduct()|
||EZShopTest.testUnauthorizedDefineCustomer()|
||EZShopTest.testModifyPointsOnCard()|
||EZShopTest.testDeleteCustomer()|
||EZShopTest.testEndReturnTransaction()|
||EZShopTest.testReturnProduct()|
||EZShopTest.testReceiveCreditCardPayment()|
||EZShopTest.testGetSaleTransaction()|
||EZShopTest.testRevertDeleteSaleTransaction()|
||EZShopTest.testGetAllCustomers()|
||EZShopTest.testUnauthorizedModifyCustomer()|
||EZShopTest.testApplyDiscountRateToSale()|
||EZShopTest.testUnauthorizedGetAllCustomers()|
||EZShopTest.testAttachCardToCustomer()|
||EZShopTest.testModifyCustomer()|
||EZShopTest.testComputePointsForSale()|
||EZShopTest.testDeleteReturnTransaction()|
||EZShopTest.testReturnCashPayment()|
||EZShopTest.testUnauthorizedGetCustomer()|
||EZShopTest.testUnauthorizedDeleteCustomer()|
||EZShopTest.testNoCommitEndReturnTransaction()|
||EZShopTest.testStartSaleTransaction()|
||EZShopTest.testApplyDiscountRateToProduct()|
||EZShopTest.testRevertEndSaleTransaction()|
||EZShopTest.testInvalidIdEndSaleTransaction()|
||EZShopTest.testDeleteSaleTransaction()|
||EZShopTest.testStartReturnTransaction()|
||EZShopTest.testWrongLoginReceiveCashPayment()|
||EZShopTest.testNotLoggedCreateCard()|
||EZShopTest.testWrongLoginModifyPointsOnCard()|
||EZShopTest.testWrongLoginReturnCashPayment()|
||EZShopTest.testUnauthorizedAddProductToSale()|
||EZShopTest.testDeleteProductFromSale()|
||EZShopTest.testReturnCreditCardPayment()|
||EZShopTest.testNoMEnoughMoneyReturnCreditCardPayment()|
||EZShopTest.testGetProductTypesByBarCode()|
||EZShopTest.testReceiveCashPayment()|
||EZShopTest.testGetCreditsAndDebits()|




# Scenarios

<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:|
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |
|  2     |  ... |



# Coverage of Scenarios and FR

<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) |
| ----------- | ------------------------------- | ----------- |
|  1.1        | FR3.1                        |   EZShopTest.testCreateProduct()|
|           |                              |   EZShopTest.testUpdateLocation()|
|  1.2        | FR4.2                        | EZShopTest.testGetProductTypesByBarCode() |
| | |EZShopTest.testUpdateLocation() |
|  1.3       | FR3.1 |  EZShopTest.testGetProductTypesByBarCode()  |
| | |EZShopTest.testUpdateProduct() |
| | | |
|  2.1        | FR1.1                           | EZShopTest.testCreateUser() |
|  2.2        | FR1.2                             | EZShopTest.testDeleteUser()  |
|  2.3        | FR1.5                             | EZShopTest.testUpdateUserRole() |
|  |  |  |
|  3.1        |  FR4.3                         |   EZShopTest.testIssueOrder()|
|    3.2      |    FR4.5                        | EZShopTest.testPayOrder() |
|  3.3       |    FR4.6                       | EZShopTest.testRecordOrderArrival() |
|  |  |  |
|    4.1     |      FR5.1                      | EZShopTest.testDefineCustomer() |
|    4.2     |   FR5.6                         | EZShopTest.testCreateCard() |
| | |EZShopTest.testAttachCardToCustomer()  |
|    4.3    |        FR5.1                    | EZShopTest.testModifyCustomer() |
|    4.4     |         FR5.1                   |   EZShopTest.testModifyCustomer()|
|  |  | |
|  5.1        | FR1.5                             |   EZShopTest.testLogin()|
|    5.2      |                              |   EZShopTest.testLogout()|
|  |  | |
|  6.1       |      FR6                       | EZShopTest.testStartSaleTransaction() |
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testEndSaleTransaction()  |
| | |EZShopTest.testReceiveCreditCardPayment()/EZShopTest.testReceiveCashPayment()  |
| 6.2      |    FR6                              |   EZShopTest.testStartSaleTransaction()|
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testApplyDiscountRateToProduct() |
| | |EZShopTest.testEndSaleTransaction()  |
| | |EZShopTest.testReceiveCreditCardPayment()/EZShopTest.testReceiveCashPayment()  |
| 6.3     |     FR6                             |   EZShopTest.testStartSaleTransaction()|
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testApplyDiscountRateToSale()  |
| | | EZShopTest.testEndSaleTransaction() |
| | |EZShopTest.testReceiveCreditCardPayment()/EZShopTest.testReceiveCashPayment()  |
| 6.4   |       FR6                           |   EZShopTest.testStartSaleTransaction()|
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testEndSaleTransaction()  |
| | |EZShopTest.testReceiveCreditCardPayment()  |
| | |EZShopTest.testComputePointsForSale() |
| | |EZShopTest.testModifyPointsOnCard()  |
| 6.5    |       FR6                           |   EZShopTest.testStartSaleTransaction()|
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testEndSaleTransaction()  |
| | |EZShopTest.testDeleteSaleTransaction()  |
| 6.6      |        FR6                        |   EZShopTest.testStartSaleTransaction()|
| | |EZShopTest.testAddProductToSale()   |
| | |EZShopTest.testEndSaleTransaction()  |
| | |EZShopTest.testReceiveCashPayment() |
| | | |
| 7.1      |               FR7.2     |EZShopTest.testReceiveCreditCardPayment()|
| 7.2     |       FR7.2                           | EZShopTest.testReceiveCreditCardPayment()|
| 7.3    |     FR7.2                                         |EZShopTest.testReceiveCreditCardPayment()|
| 7.4      |   FR7.1                       |EZShopTest.testReceiveCashPayment()|
|  |  ||
| 8.1      |   FR6.12-FR6.13-FR6.14-FR6.15- FR7.4 |   EZShopTest.testStartReturnTransaction()|
|  |  | EZShopTest.testReturnProduct() |
| | |EZShopTest.testReturnCreditCardPayment() |
| 8.2     | FR6.12-FR6.13-FR6.14-FR6.15- FR7.3 |   EZShopTest.testStartReturnTransaction()|
|  |  | EZShopTest.testReturnProduct() |
|  |  | EZShopTest.testReturnCashPayment() |
|  |  |  |
| 9.1     |      FR8.3                       | EZShopTest.testGetCreditsAndDebits() |
|  |  | |
| 10.1     |    FR7.4                             |   EZShopTest.testReturnCreditCardPayment()|
| 10.2     |       FR7.3                      | EZShopTest.testReturnCashPayment() |



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|                            |           |

