# Congestion Tax API For vehicles APIs

API based application to calculate the congestion tax for vehicles by type  
in any given city with defined exempted vehicles and free tax days configured

## Contents
- [Used Tech Stack and Libs](#tech-stack)
- [Getting Started](#getting-started)
- [Using The API](#using-the-api)
- [Configurations](#configurations)
- [Access to the H2 console](#access-to-the-h2-console)

## Tech Stack
- Java 17
- Spring boot, Spring WebMVC
- Spring data
- H2 DB
- Lombok
- MapStruct

## Getting Started
Make Sure you have the following:
- Java 17
- Maven 
- Build command: **_mvn clean install_**

## Using The API

curl --request POST \
--url http://localhost:8081/congestion-tax \
--header 'Content-Type: application/json' \
--data '{
"crossingDateTimes": [
"2013-01-07 15:00:00",
"2013-01-08 16:59:59"
],
"vehicleType":"Car",
"city":"Gothenburg"
}'

## Configurations

### Exempted Vehicles names 
- emergency
- bus
- diplomat
- motorcycle
- military
- foreign

### Defined cities
- Gothenburg

### Other config in DB table CITY_TAX_CONFIGS
- To Get the one tax charge per duration of minutes -> one_charge_per_minutes:**_60_**
- To check if weekend tax-free enabled -> weekend_tax_free_enabled:**_true_**
- To check if tax-free days before holiday is enabled -> tax_free_days_before_holiday_enabled:**_true_**
- To check how many days before holiday is tax-free -> tax_free_days_before_holiday_number:**_1_**


## Access to the H2 console

http://localhost:8081/h2-console/
