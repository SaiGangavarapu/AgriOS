# Architecture Test Strategy

## 1. Objective

Automatically verify domain and module boundaries as implementation grows.

## 2. Spring Modulith verification

The implementation shall include tests that:

- discover all application modules
- verify allowed dependencies
- detect cycles
- verify event publication boundaries
- generate module documentation where useful

## 3. ArchUnit tests

ArchUnit rules should enforce:

- domain packages do not depend on Spring Web, JPA, messaging, or vendor SDKs
- application packages do not depend on web adapters
- internal packages are not imported externally
- repository interfaces remain inside owning modules
- controllers access application APIs, not repositories
- external provider DTOs remain in integration packages
- JPA entities do not cross module boundaries

## 4. Contract tests

Required for:

- weather adapters
- laboratory adapters
- notification providers
- IoT gateways
- market data
- insurer and lender integrations
- published module APIs
- event schemas

## 5. Persistence-boundary tests

Verify:

- each module writes only its own tables
- migrations do not modify another module's schema
- projections are rebuildable
- outbox events are transactional with aggregate changes

## 6. Event tests

Verify:

- event envelope fields
- version compatibility
- idempotent consumption
- replay behavior
- sensitive-field minimization
- outbox publication

## 7. Domain invariant tests

Examples:

- field split preserves area and lineage
- crop plan cannot activate with unresolved hard constraint
- nutrient plan product conversion reconciles with nutrient requirement
- actuator command is blocked when calibration is expired
- produce-lot balance never becomes negative
- revoked consent blocks future sharing

## 8. CI requirement

Architecture tests shall run in every backend pull request and must not be bypassed without documented approval.
