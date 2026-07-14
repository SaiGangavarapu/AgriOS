# AgriOS Backend Foundation v1.0

This cumulative release adds a real Java 21 and Spring Boot backend foundation.

## Included

- Spring Boot web, validation, security, JPA, actuator, and OpenAPI
- Spring Modulith module declarations
- PostgreSQL and Flyway integration
- physical database migrations copied into backend resources
- JWT resource-server scaffold
- correlation-id propagation
- standardized API error handling
- pagination contract
- audit port and logging adapter
- transactional outbox persistence foundation
- platform information endpoint
- Dockerfile and Docker Compose sandbox
- Adminer and Mailpit
- Maven, Spring Modulith, ArchUnit, and context tests
- GitHub Actions backend CI

## Reuse from ConfigurableBusiness ERP

The milestone reuses architectural patterns proven in the ERP project—security,
auditing, modular boundaries, Docker sandbox, exception handling, OpenAPI, and
CI concepts—without copying commerce-domain entities or unstable implementation
details.
