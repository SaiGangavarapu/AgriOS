# Shared Kernel and Integration Rules

## 1. Shared kernel policy

The shared kernel must remain minimal.

Allowed shared types:

- identifiers
- money
- quantity
- unit
- date range
- geo point
- audit metadata
- correlation identifier
- standard result and error structures

Not allowed:

- Farmer domain entity
- Field domain entity
- CropCycle domain entity
- Advisory domain entity
- Device domain entity
- JPA entities
- repository interfaces owned by a context
- vendor API payloads

## 2. Published language

Cross-context contracts should use:

- immutable event schemas
- versioned API DTOs
- explicit identifiers
- canonical units
- source and timestamp metadata
- quality and confidence metadata where relevant

## 3. Compatibility

- additive changes preferred
- breaking changes require a new contract version
- consumers must tolerate unknown optional fields
- event schemas must include event id, version, time, producer, and correlation id

## 4. Integration reliability

- idempotency
- retries
- dead-letter handling
- observability
- replay safety
- reconciliation
- contract tests

## 5. Database rule

Separate schemas may be used per module within the same PostgreSQL instance. Cross-schema foreign keys should be avoided unless explicitly approved.
