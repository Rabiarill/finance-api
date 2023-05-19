# Finance-api
An REST API which help to manage finance and create statistic.

## How to launch
You need to have Docker installed on your machine to run the API.
1. Remove postfix `.origin` in filename  `application.properties.origin`
2. Generate `jar` at target directory, e.g.  `finance-api-1.0.0-SNAPSHOT.jar`
    ```bash
    mvn package
    ```
3. Create container and run application:
    ```bash
    docker-compose up -d
    ```

## Documentation
Swagger (development environment): http://localhost:8080/swagger-ui/#/

## Technologies
- Java 17 (Java Development Kit - JDK: 17.0.6)
- Spring Boot 2.5.1
- Maven
- JUnit 5
- PostgreSQL 14
- Swagger 3.0.0
- Flyway
- Docker
