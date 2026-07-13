# SA 04 — Modular Monolith Architecture

## Backend stack

- Java 21
- Spring Boot 3.x
- Spring Modulith
- Spring Security
- Spring Data JPA
- Flyway
- PostgreSQL/PostGIS
- Redis
- optional Kafka or Redpanda later

## Module groups

```text
agrios-backend
├── platform-foundation
├── farmer-platform
├── farm-platform
├── agronomy-platform
├── crop-operations
├── intelligence-platform
├── ecosystem-platform
└── integration-platform
```

## Internal module structure

```text
module
├── api
├── application
├── domain
└── infrastructure
```

## Rules

- domain depends only on JDK and approved shared value objects
- application invokes domain and ports
- infrastructure implements ports
- API exposes public contracts
- package-private implementation is preferred
- module dependencies are verified by Spring Modulith and ArchUnit
