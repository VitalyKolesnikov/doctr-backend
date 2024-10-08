# Doctr
**Doctr** is a Spring Boot application with REST API. It was developed for my wife who is a dentist so she can track patients and their visits data.

This is the backend part. Frontend part is available [here](https://github.com/VitalyKolesnikov/doctr-frontend). 

API documentation is available at http://localhost:8080/swagger-ui/index.html

In this project I'm trying to implement all the technologies and best practices I know and use at my job as Java developer.

#### Key features are listed below:

- [Java 21](https://www.java.com/)
- [Spring Boot 3](https://spring.io/)

#### Database
- [Postgresql](https://www.postgresql.org/) as a data storage
- [Flyway](https://flywaydb.org/) for database migrations
- [Hibernate](https://hibernate.org/) as ORM

#### Testing
- Unit tests with [JUnit 5](https://junit.org/junit5/), [Mockito](https://site.mockito.org/) and [Hamcrest](https://hamcrest.org/)
- Integration tests with [Testcontainers](https://www.testcontainers.org/) and [REST Assured](https://rest-assured.io/)
- Hexagonal architecture tests with [ArchUnit](https://www.archunit.org/)

#### Tools
- [Lombok](https://projectlombok.org/) to write less boilerplate code
- [Mapstruct](https://mapstruct.org/) for mappings
- [OpenAPI](https://www.openapis.org/) and [Swagger](https://swagger.io/) for REST documentation
- [Sentry](https://sentry.io/) for error tracking
- [JaCoCo](https://www.eclemma.org/jacoco/) for code coverage
