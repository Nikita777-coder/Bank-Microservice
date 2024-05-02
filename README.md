#readme_bank_microservice

# Bank microservice

💵Bank microservice - server side of application which has part functional of bank transaction server and operations of bank client application or website💵

## API
#### Server
1. Save transaction - save user transaction for it foreign getting 
2. Get currency ratio - get current information about the ratio of one currency to another at their current exchange rate from an external data source(twelvedata.com)

#### Client 
1. Set new month limit - setting a new monthly limit, taking into account past transactions (their deduction) for the month  
3. Get limit transactions - receiving transactions that exceeded the month limit, indicating the data on the limit (amount, date of establishment, currency)

## Stack
1. Spring Boot 3
2. PostgreSQL
3. Java
4. Web,
5. WebFlux
6. Rest API
7. WebClient
8. MapStruct
9. Lombok
10. Maven
