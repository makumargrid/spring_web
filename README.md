# spring_web — Account REST API

A Spring Boot 3 RESTful service for managing `Account` resources, built with Maven,
Java 17, Spring Data JPA and an in-memory H2 database.

The project is delivered incrementally across feature branches:

| Branch | Scope |
| ------ | ----- |
| `feature/http-rest` | Project scaffold + OpenAPI 3 contract (`openapi.yaml`) |
| `feature/spring-mvc-practice` | Spring MVC foundations (controller, request filter, servlet registration) |
| `feature/crud-implementation` | DTO-based CRUD with MockMvc tests (JaCoCo ≥ 80%) |
| `feature/validations-and-exception-handling` | Bean Validation + RFC 7807 errors + custom `@ValidUsername` |
| `feature/search-endpoints-and-pagination` | Pagination, filtering & sorting via JPA Specifications |

## Requirements

- Java 17+
- Maven 3.9+

## Build & test

```bash
mvn verify
```

JaCoCo coverage report: `target/site/jacoco/index.html`

## Run

```bash
mvn spring-boot:run
```

- API base:   `http://localhost:8080/api/v1/accounts`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console`

## Domain

`Account` — `id`, `username`, `email`, `password` (never returned), `status`
(`ACTIVE` / `INACTIVE` / `SUSPENDED`), `balance`, `createdAt`.

## Security note

This service ships with **no authentication or authorization** — every endpoint is
publicly accessible. Add Spring Security (and transport security) before exposing it
beyond local development.
