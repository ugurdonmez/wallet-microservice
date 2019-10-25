# Wallet Microservice

## Getting Started  
  
### Prerequisites  
  
- JDK 1.8  
- Maven 3.2+ (`brew install maven`)  
  
  
### Install deps  
  
- `mvn install`  
  
### Compile  
  
- `mvn compile`  
  
### Package  
  
- `mvn package`  
  
## Test  
  
- `mvn test`  
  
## Run  
  
- `java -jar target/wallet-0.0.1-SNAPSHOT.jar`  

## Tools  
  
### Swagger UI  
  
- Access swagger ui via `http://localhost:8080/swagger-ui.html`  
  
### H2 Console  
  
- You can access h2 console via `http://localhost:8080/h2-console`  
- Default _**JDBC URL**_ : `jdbc:h2:/./wallet-db`  

## Endpoints

- Get:  `/wallet/player/{id}`
- Post: `/wallet/player/{id}` 
- Get:  `/wallet/player/{id}/transactions`

Player with UUID:e2f1b602-d7b3-4eb2-984d-2d8f9e289511 is already loaded in order to test endpoints.

## Used technologies  

- Spring Boot Web  
- Spring Boot Data JPA  
- H2  
- Lombok  
- Swagger-ui  
- Mockito