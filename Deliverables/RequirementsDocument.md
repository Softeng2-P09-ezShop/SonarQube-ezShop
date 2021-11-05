# Requirements Document 

Authors: Claudio Tancredi (s292523), Ehsan Ansari Nejad (s288903), Takla Trad (s289222)

Date: 21/04/2021

Version: 1.0

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases and relevant scenarios](#use-cases-and-relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)


# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders


| Stakeholder name  | Description | 
| :----------------- |:-----------|
| User | User of EZShop application, it could be either the owner or the cashier |
| Owner | The owner can see a dashboard with useful information about income and expenses, can see transactions lists and can send new orders to suppliers |
| Cashier | Can scan products of a customer in order to start and complete a transaction, can scan products delivered by a supplier in order to update the inventory |
| Google Sheets | Online service used to guarantee data persistence and consistency |
| Administrator | The administrator can create, update and remove users accounts (owner/cashier) by interacting with the users data via Google Sheet (as a normal sheet on Google website)  |
| Supplier | Company that supply the shop |
| Credit card system | System used to manage electronic payments |  
| Customer | Can indirectly interact with the system through the cashier in order to perform a purchase |
| Product | Physical item that can be sold |

# Context Diagram and interfaces

## Context Diagram
```plantuml
actor Cashier as c
actor Owner as o
actor Product as p
actor :Credit card system: as ccs
actor Supplier as s
actor User as u
rectangle System{
	u -right- (EZShop)
	o -up-^ u
	c -up-^ u
	p -down- (EZShop)
	s <-left- (EZShop)
	ccs -down- (EZShop)
}
	
```


## Interfaces

| Actor | Logical Interface | Physical Interface  |
| :------------- |:-------------| :-----|
| User | GUI | Screen, keyboard, mouse on a PC |
| Supplier | [Gmail API](https://developers.google.com/gmail/api/guides) | Internet connection |
| Credit card system | Web services | Internet connection |  
| Product | Bar code | Laser beam (on bar code reader) |

# Stories and personas

The following personas and stories are meant to cover different profiles of the Owner actor:

Daniela is 38 and she is the owner of a small food shop. Due to the pandemic her financial situation has worsened. She has never really been good at managing profits and losses, so if she wants her shop to survive these hard times she really needs a way to track all the income and the expenses.

Ehsan is a young entrepreneur who found an interesting workplace suited for a supermarket, which the neighborhood lacks. Thinking about the easiest way to manage the supermarket and interested in the latest technologies, he found that managing this shop - sales, inventory and accounting - using a stable and user-friendly platform is a guaranty to a good sustainability for the future.

Rick is 58 and he is the owner of a small clothing shop. After years of presence in the business he's aging and he can't keep going with handwritten transactions lists anymore. He would like to automate transactions management with an easy to use application, because he's not very used to technology.


# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------| 
| FR1 | Authenticate and authorize User |
| FR1.1 | Log in |
| FR1.2 | Log out |
| FR2 | Scan product | 
| FR2.1 | Read bar code through bar code reader |
| FR2.2 | Get bar code inserted by cashier through keyboard |
| FR2.3 | Retrieve product data from inventory given bar code |
| FR3 | Handle payment |
| FR3.1 | Handle payment in cash |
| FR3.1.1 | Get Customer paid amount |
| FR3.1.2 | Compute change |
| FR3.2 | Handle payment with credit card |
| FR4 | Manage sale |
| FR4.1 | Scan product (FR2) |
| FR4.2 | Compute total amount for transaction every time a product is scanned |
| FR4.3 | Manage Customer shopping cart |
| FR4.3.1 | Empty shopping cart |
| FR4.3.2 | Increase or decrease quantity of a product that is in the shopping cart |
| FR4.3.3 | Delete all instances of a product from the shopping cart |
| FR4.4 | Start sale transaction |
| FR4.5 | Handle payment (FR3) |
| FR4.6 | Print receipt |
| FR4.7 | Update stored data about products in inventory and transactions |
| FR4.8 | End sale transaction |
| FR4.9 | Empty shopping cart (FR4.3.1) |
| FR5 | Update inventory with delivered products |
| FR5.1 | Scan product (FR2) |
| FR5.2 | Manage list of scanned delivered products |
| FR5.2.1 | Empty list |
| FR5.2.2 | Delete a product from the list |
| FR5.3 | Get product missing data inserted by cashier through keyboard|
| FR5.3.1 | Get quantity |
| FR5.3.2 | Get also name and price per unit if product is new |
| FR5.4 | Update stored data about products in inventory and eventually add new products |
| FR5.5 | Empty list (FR5.2.1) |
| FR6 | Place new order |
| FR6.1 | Retrieve almost out of stock products (quantity < 100) and suppliers data |
| FR6.2 | Sort products by quantity left (ascending order) |
| FR6.3 | Update suppliers list according to supplier name filter |
| FR6.4 | Place new order for almost out of stock product |
| FR6.4.1 | Get product and supplier from selections |
| FR6.4.2 | Automatically fill e-mail with selections |
| FR6.5 | Fill e-mail with data inserted by the owner through keyboard | 
| FR6.6 | Clear selections and e-mail |
| FR6.7 | Send e-mail to supplier |
| FR6.8 | Handle payment with credit card (FR3.2) |
| FR6.9 | Update stored data about transactions and eventually add new supplier |
| FR6.10 | Clear selections and e-mail (FR6.6) |
| FR7 | Compute financial report |
| FR7.1 | Retrieve transactions data |
| FR7.2 | Compute total income, expenses and profits of the last 30 days |
| FR7.3 | Compute daily income and expenses of the last 30 days |
| FR7.4 | Print total income and expenses bar chart |
| FR7.5 | Print daily income and expenses line charts |
| FR7.6 | Print total profits |
| FR7.7 | Update charts and profits according to time filter |
| FR8 | See transactions history |
| FR8.1 | Retrieve transactions data for the last 30 days |
| FR8.2 | Sort transactions by date (descending, most recent ones are up) |
| FR8.3 | Update transactions lists according to time filter |
| FR8.4 | Update customer transactions list according to customer credit card number filter |
| FR8.5 | Update owner orders list according to supplier name filter |


### Access right, actor vs function

| Function | Cashier | Owner |
| :------------- |:-------------|:--|
| FR1 | yes | yes |
| FR2 | yes | no |
| FR3 | yes | yes, but only FR3.2 |
| FR4 | yes | no |
| FR5 | yes | no |
| FR6 | no | yes |
| FR7 | no | yes |
| FR8 | no | yes |


## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| :------------- |:-------------| :-----| :-----|
|  NFR1     |  Usability | At most 2 hours of training for cashier and owner| All FR |
|  NFR2     | Security| Only cashiers and owner can access the application | FR1 |
|  NFR3     | Performance| All functions should complete in < 1 sec | All FR|
|  NFR4  | Portability| EZShop application should be available for different desktop operating systems (e.g. Windows 10, MacOS) | All FR |
|  NFR5    | Privacy| Customer personal data must be stored lawfully, fairly and in a transparent manner, so the application should be compliant with (EU) 2016/679 GDPR Regulation | All FR |
| NFR6 | Availability | The system should NOT be unavailable for more than 1 minute | All FR |
| NFR7 | Localization | Decimal numbers use . (dot) as decimal separator | All FR |
| NFR8 | Localization | EZShop application should be available in the following languages: English, Italian | All FR |
| NFR9 | Localization | Dates are stored, treated and displayed in the "DD/MM/YYYY" format | All FR |
|  Domain1  |-|Currency is Euro|All FR|


# Use case diagram and use cases


## Use case diagram

```plantuml
actor Cashier as c
actor Owner as o
actor Product as p
actor :Credit card system: as ccs
actor Supplier as s
actor User as u

rectangle System{
	usecase "FR1 Authenticate \nand authorize User" as FR1
    usecase "FR4 Manage sale" as FR4
    usecase "FR2 Scan product" as FR2
    usecase "FR3 Handle payment" as FR3
    usecase "FR3.1 Handle payment in cash" as FR31
    usecase "FR3.2 Handle payment \nwith credit card" as FR32
    usecase "FR5 Update inventory \nwith delivered products" as FR5
    usecase "FR6 Place new order" as FR6
	usecase "FR6.4 Place new order for almost out of stock product" as FR64
    usecase "FR7 Compute financial report" as FR7
    usecase "FR8 See transactions history" as FR8
}

o --|> u
c --|> u
u --> FR1
c --> FR4
FR4 ..> FR2 : include
FR2 --> p
FR4 ..> FR3 : include
FR3 ..> FR31 : include
FR3 ..> FR32 : include
FR32 --> ccs
c --> FR5
FR5 ..> FR2 : include
o --> FR6
FR6 --> s
FR6 ..> FR32 : include
o --> FR7
o --> FR8
FR64 .up.> FR6 : extend
```

## Use cases and relevant scenarios

### Use case 1, UC1 - "Authenticate and authorize User" (ref. FR1)
| Actors Involved        | A registered user |
| :-------------: |:-------------:| 
|  Precondition     | 1. User account has already been created by the administrator and added on storage |  
|  Post condition     | 1. User gets/loses access to the system |
|  Nominal Scenario     | User inserts his Username and Password and presses login button; users data is retrieved from storage and access is granted/denied; when the user finishes his operations, he logs out |
|  Variants     | 1. Incorrect Username and/or Password inserted by the User<br/>2. No internet connection |


### Use case 2a, UC2a - "Manage sale (with cash)" (ref. FR4/FR3.1)
| Actors Involved        | Cashier |
| :-------------: |:-------------:| 
|  Precondition     | 1. Cashier is authenticated<br/>2. Customer has enough money in cash<br/>3. Bar code X is valid<br/>4. Bar code X corresponds to a product available in the shop |  
|  Post condition     | 1. New transaction is added to storage<br />2. Product quantity is updated in the inventory<br />3. Receipt is printed |
|  Nominal Scenario     | Cashier scans all the products using the bar code reader; cashier checks out the customer by cash; cashier gives the receipt to customer |
|  Variants     | 1. Printer malfunctioning or disconnected <br />2. Transactions and inventory not updated due to absence of internet connection |

##### Scenario 2a.1 

| Scenario 2a.1 | |
| :-------------: |:-------------:| 
|  Precondition     | 1. Cashier is authenticated<br/>2. Customer has enough money in cash<br/>3. Bar code X is valid<br/>4. Bar code X corresponds to a product available in the shop |  
|  Post condition     | 1. New transaction is added to storage<br />2. Product quantity is updated in the inventory<br />3. Receipt is printed | 
| Step#        | Description  |
|1 | Customer arrives at the checkout |
|  2     | Cashier scans all the products using the bar code reader |  
|  3     | Cashier manages the shopping cart according to customer's requests |
|  4     | Total amount is computed in real-time, every time a product is scanned |
| 5 | Cashier starts the transaction by cash |
| 6 | Cashier receives cash from customer and inserts the amount in the application |
| 7 | The change is computed |
| 8 | The cashier eventually gives the change to customer, if it is not zero |
| 9 | The cashier complete the transaction |
| 10 | The receipt is printed |
|11|The cashier gives the receipt to the customer |
|12 | Transactions and inventory in storage are updated |
|13 | Customer shopping cart is emptied |

### Use case 2b, UC2b - "Manage sale (with credit card)" (ref. FR4/FR3.2)
| Actors Involved        | Cashier and Credit Card System |
| :-------------: |:-------------:| 
|  Precondition  | 1. Cashier is authenticated<br/>2. Customer has enough credit on credit card<br/>3. Bar code X is valid<br/>4. Bar code X corresponds to a product available in the shop |  
|  Post condition     | 1. New transaction is added to storage <br/>2. Product quantity is updated in the inventory <br/>3. Credit Card Payment is successfully processed by the Credit Card System<br />4. Receipt is printed|
|  Nominal Scenario     | Cashier scans all the products using the bar code reader; cashier checks out the customer by Credit Card; payment successfully processed by the Credit Card System; cashier gives the receipt to customer|
|  Variants     | 1. Printer malfunctioning or disconnected <br/>2. Payment failed <br/>3. Transactions and inventory not updated due to absence of internet connection |

##### Scenario 2b.1 

| Scenario 2b.1 | |
| :-------------: |:-------------:| 
|  Precondition  | 1. Cashier is authenticated<br/>2. Customer has enough credit on credit card<br/>3. Bar code X is valid<br/>4. Bar code X corresponds to a product available in the shop |  
|  Post condition     | 1. New transaction is added to storage <br/>2. Product quantity is updated in the inventory <br/>3. Credit Card Payment is successfully processed by the Credit Card System<br />4. Receipt is printed|
| Step#        | Description  |
|1 | Customer arrives at the checkout |
|  2     | Cashier scans all the products using the bar code reader |  
|  3     | Cashier manages the shopping cart according to customer's requests |
|  4     | Total amount is computed in real-time, every time a product is scanned |
| 5 | Cashier starts the transaction by credit card |
| 6 | Customer swipes/taps/inserts the Credit Card into the Credit Card Reader *and* inserts the Card's PIN |
| 7 | Payment successfully processed by the Credit Card System |
| 8 | The application receives the success message from the credit card system |
| 9 | The receipt is printed |
|10|The cashier gives the receipt to the customer |
|11 | Transactions and inventory in storage are updated |
|12 | Customer shopping cart is emptied |

### Use case 3, UC3 - "Update inventory with delivered products" (ref. FR5)
| Actors Involved        | Cashier |
| :-------------: |:-------------:| 
|  Precondition     | 1. Cashier is authenticated <br/>2. Delivery arrived<br/>3. Cashier has the delivery paper with the information about the order<br/>4. Bar code X is valid<br/>5. Cashier knows prices to set for new products |  
|  Post condition     | 1. Inventory updated |
|  Nominal Scenario     | Cashier scans newly arrived products using the Bar Code Reader; cashier inserts missing information about the product; the cashier presses on the update inventory button and inventory is updated |
|  Variants     | 1. Inventory not updated due to absence of internet connection<br/>2. Product data is not in inventory (= product is new) |

##### Scenario 3.1 

| Scenario 3.1 | |
| :-------------: |:-------------:| 
|  Precondition     | 1. Cashier is authenticated <br/>2. Delivery arrived<br/>3. Cashier has the delivery paper with the information about the order<br/>4. Bar code X is valid<br/>5. Cashier knows prices to set for new products |  
|  Post condition     | 1. Inventory updated |
| Step#        | Description  |
|1 | Delivery arrives |
|  2     | Cashier scans a product using the bar code reader | 
| 3 | Product data retrieved from inventory |
| 4 | Cashier inserts the quantity that has been delivered |
| 5 | Inventory updated when the cashier presses the related button |
| 6 | List of products is emptied |

##### Scenario 3.2 

| Scenario 3.2 | |
| :-------------: |:-------------:| 
|  Precondition     | 1. Cashier is authenticated <br/>2. Delivery arrived<br/>3. Cashier has the delivery paper with the information about the order<br/>4. Bar code X is valid<br/>5. Cashier knows prices to set for new products |  
|  Post condition     | 1. Inventory updated |
| Step#        | Description  |
|1 | Delivery arrives |
|  2     | Cashier scans a product using the bar code reader | 
| 3 | Product data hasn't been retrieved from inventory (= new product) |
| 4 | Cashier inserts the new product's details: Bar Code, Name, Price per unit, quantity... |
| 5 | Inventory updated with the new product when the cashier presses the related button |
| 6 | List of products is emptied |


### Use case 4a, UC4a - "Place new order" (ref. FR6)
| Actors Involved        | Owner and Supplier |
| :-------------: |:-------------:| 
|  Precondition     |  1. Owner is authenticated <br/> 2. Owner is informed about Supplier name, prices for products, e-mail and IBAN<br/>3. Owner has enough credit on credit card |  
|  Post condition     | 1. New transaction is added to storage<br/> 2. Supplier receives the order via e-mail<br/>3. Credit Card Payment is successfully processed by the Credit Card System<br/>4. New supplier may be added to storage if not already present|
|  Nominal Scenario     | Owner writes the new product's and the supplier's details directly into the e-mail template; owner inserts all the required information (quantity, price for the order); owner confirms the order and sends the email; owner pays with credit card |
|  Variants     | 1. Email not sent to the supplier due to absence of internet connection<br/> 2. Payment failed  |

##### Scenario 4a.1 

| Scenario 4a.1 | |
| :-------------: |:-------------:| 
|  Precondition     |  1. Owner is authenticated <br/> 2. Owner is informed about Supplier name, prices for products, e-mail and IBAN<br/>3. Owner has enough credit on credit card |  
|  Post condition     | 1. New transaction is added to storage<br/> 2. Supplier receives the order via e-mail<br/>3. Credit Card Payment is successfully processed by the Credit Card System<br/>4. New supplier may be added to storage if not already present|
|1 | Owner fills the e-mail template |
|  2     | Owner confirms and sends the e-mail to supplier |  
|  3     | The application starts a payment transaction with credit card |
|  4     | Owner swipes/taps/inserts the Credit Card into the Credit Card Reader *and* inserts the Card's PIN |
| 5 | Payment successfully processed by the Credit Card System |
| 6 | The application receives the success message from the credit card system |
|7 | Transactions in storage are updated |
|8 | Supplier is added to storage if it is new |
|9 | E-mail template is cleared |

### Use case 4b, UC4b - "Place new order (for almost out of stock product)" (ref. FR6/FR6.4)
| Actors Involved        | Owner and Supplier |
| :-------------: |:-------------:| 
|  Precondition     | 1. Owner is authenticated <br/> 2. Owner is informed about Supplier prices for products<br/>3. Owner has enough credit on credit card |  
|  Post condition     | 1. New transaction is added to storage <br/>2. Supplier receives the order via e-mail <br/>3. Credit Card Payment is successfully processed by the Credit Card System|
|  Nominal Scenario     | Owner selects the product that is almost out of stock; owner selects the supplier; e-mail is partially filled with these information; owner inserts all the required information (quantity, price for the order); owner confirms the order and sends the email; owner pays with credit card|
|  Variants     | 1. Email not sent to the supplier due to absence of internet connection<br/>2. Payment failed |

##### Scenario 4b.1 

| Scenario 4b.1 | |
| :-------------: |:-------------:| 
|  Precondition     | 1. Owner is authenticated <br/> 2. Owner is informed about Supplier prices for products<br/>3. Owner has enough credit on credit card |  
|  Post condition     | 1. New transaction is added to storage <br/>2. Supplier receives the order via e-mail <br/>3. Credit Card Payment is successfully processed by the Credit Card System|
| Step#        | Description  |
|1 | Owner selects product and supplier |
| 2 | E-mail is partially filled with these information|
|3 | Owner inserts the remaining missing data into the e-mail template |
|  4     | Owner confirms and sends the e-mail to supplier |  
|  5    | The application starts a payment transaction with credit card |
|  6     | Owner swipes/taps/inserts the Credit Card into the Credit Card Reader *and* inserts the Card's PIN |
| 7 | Payment successfully processed by the Credit Card System |
| 8 | The application receives the success message from the credit card system |
|9 | Transactions in storage are updated |
|10 | E-mail template and selections are cleared |

### Use case 5, UC5 - "Compute financial report" (ref. FR7)
| Actors Involved        | Owner |
| :-------------: |:-------------:|
|  Precondition     | 1. Owner is authenticated <br/> 2. Default time interval is 30 days |  
|  Post condition     | 1. Owner can see income and expenses of the selected time interval, both globally and daily, and see profits (=income-expenses) |
|  Nominal Scenario     | Owner requests to see income and expenses by going to the dashboard page; transactions of the default time interval are retrieved; income and expenses are computed; computed values are displayed on the screen with charts |
|  Variants     | 1. No internet connection to retrieve transactions<br/>2. Owner changes time interval and the report is updated accordingly|

### Use case 6, UC6 - "See transactions history" (ref. FR8)
| Actors Involved        | Owner |
| :-------------: |:-------------:| 
|  Precondition     | 1. Owner is authenticated<br/> 2. Default time interval is 30 days |  
|  Post condition     | 1. Transactions lists are displayed |
|  Nominal Scenario     | Owner opens the transactions page; transactions of the default time interval are retrieved and showed |
|  Variants     | 1. No internet connection to retrieve transactions<br/>2. Owner adds new filters (time interval, customer credit card number and supplier name) and transactions lists are updated accordingly |

# Glossary

```plantuml
skinparam classAttributeIconSize 0
class EZShop
class User{
	+Username
	+Password
}
class Transaction{
	+ID
	+Date
	+Total amount
	+Payment method, credit card or cash
	+Receiver's IBAN
}
class Supplier{
	+Name
	+E-mail
}
class Owner{
	+E-mail
}
class Cashier
class Customer
class Purchase
class Inventory
class "Credit card" as cc{
	+Credit card number
}
class Order{
	+Quantity
}
class Product
class "Product descriptor" as pd{
	+Bar code
	+Name
	+Price per unit
}
class "Accounting Report" as ar{
	+Start date
	+End date
}

EZShop -- "2...*" User
EZShop -- Inventory
Customer -- "*" cc : owns
Order "*" -- Supplier : to
Order "    1...*" -- pd : for
Order --|> Transaction
User <|-- Owner
User <|-- "1...*" Cashier 
Cashier -- "*" Purchase : starts
Owner -- "*" Order : places
Customer -- "*" Purchase : requests
Purchase --|> Transaction
Purchase -- "1...*" Product
Inventory -- "*" pd
pd -- "*" Product : describes
cc "    +owner credit\ncard\n1"--"*" Order
cc "+customer credit \ncard\n0...1"--"*" Purchase
Owner -- "1...*" cc : owns
Owner -- "*" ar : requests
ar "*" -- "*" Transaction : about

note bottom of Product
This represents a SINGLE PRODUCT of the shop. For 
example, a pack of pasta.
It's what the cashier actually scans, the physical
object that is in the shop.
end note
note bottom of pd
Descriptor for products of the same type.
Example: suppose we have fifty packs of pasta of the same type.
Each pack is a SINGLE PRODUCT, but all of them have the listed
attributes in common (even the same bar code, it's not unique
for products!), so this class is a unified model for all packs
of pasta of that specific type. It's like a label, and within
this logical scope the bar code is indeed unique.
The same rule applies to all products.
The information about stocks is in the * multiplicity
of the "describes" association.
This is basically the abstract representation of products that the
system works with and that is stored into the inventory.
end note
note bottom of Supplier
Company that provides 
products to the shop.
end note
note bottom of cc
Customer can have a credit card
and pay with it. Owner MUST have a
credit card to pay for new orders.
end note
note bottom of ar
Accounting report for a given time interval.
The report could either be a financial report
represented on the dashboard through charts 
or a transactions history report represented 
on tables.
end note
note left of Customer
A person that enters the shop and
buys something.
Customers can either pay in cash or
with credit card.
end note
note left of Owner
E-mail is used to send orders to suppliers.
end note
note right of Transaction
IBAN can be owner's IBAN or
supplier's IBAN.
IBAN is needed to receive
electronic payments.
end note
```

# System Design

```plantuml
skinparam classAttributeIconSize 0 
class "EZShop" as ezs
class "Bar Code Reader" as bcr
class "Printer" as p
class "Credit Card Reader" as ccr
class "EZShop Application" as ezsa
note bottom of p
[[https://www.epson.eu/products/sd/pos-printer/epson-tm-t20iii-series]]
with its drivers (search for "TM-T20III"): 
[[https://download.epson-biz.com/modules/pos/]]
end note
note bottom of bcr
[[https://www.inateck.com/products/wireless-bluetooth-barcode-scanner-bcst-10]]
end note
note left of ccr
A sumup credit card reader. 
The communication is done via APIs: 
[[https://developer.sumup.com/docs/terminal-api/]]
end note
ezs o--p
ezs o--ccr
ezs o--bcr
ezs o-- ezsa
hide circle
ezs : +FR1 Authenticate and authorize User()
ezs : +FR4 Manage sale()
ezs : +FR5 Update inventory with delivered products()
ezs : +FR6 Place new order()
ezs : +FR7 Compute financial report()
ezs : +FR8 See transactions history()
```

# Deployment Diagram 
```plantuml
node "Google Server" as gs 
node "PC client" as pcc
artifact "EZShop Application" as ezsa
artifact <<artifact>> {
	storage "Data storage" as ds
}

ezsa -right- pcc
gs -- "*" pcc: internet
ds -left- gs

```

Data is stored in [Google Sheets](https://developers.google.com/sheets/api/guides/concepts) on the Google server. Each sheet contains data about one specific part of the application (suppliers, transactions, inventory, users).  
EZShop Application runs on the client side and all operations are performed locally.
