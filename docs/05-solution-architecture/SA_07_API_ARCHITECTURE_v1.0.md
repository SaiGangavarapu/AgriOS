# SA 07 — API Architecture

## API styles

- REST for commands and queries
- webhooks for partner callbacks
- events for asynchronous integration
- optional GraphQL only for specialized read needs later

## Standards

- `/api/v1/...`
- OpenAPI contracts
- canonical identifiers
- RFC3339 timestamps
- standardized pagination
- standardized error envelope
- idempotency key for retryable commands
- `ETag` or version field for optimistic concurrency
- correlation id on every request

## API gateway responsibilities

- TLS termination
- authentication
- request limits
- routing
- correlation
- basic threat protection

## BFF option

A mobile BFF may aggregate farmer dashboard data while preserving domain APIs.
