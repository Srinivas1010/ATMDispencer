# ATMDispencer


Utility to simulate ATM Cash dispenser. Rest endpoints has been exposed to consume services such as 

1) Enquiry to fetch all available denominations.
2) Enquiry get any particular denomination details.
3) Cash Deposit
4) Cash Withdrawl 

Rest API's are built using spring-boot and H2 in memory database has been used as temporary persistance store.

Pre-requisite to build and compile the project.
  1) Java 8+
  2) Maven installed


Maven commands to build and run:

mvn spring-boot:run -Dspring-boot.run.profiles=dev
