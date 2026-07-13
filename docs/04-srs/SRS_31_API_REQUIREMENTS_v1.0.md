# SRS 31 — API Requirements

- IR-API-001: APIs shall be versioned.
- IR-API-002: State-changing requests shall support idempotency keys where retries are possible.
- IR-API-003: Updates shall support optimistic concurrency.
- IR-API-004: Responses shall use canonical identifiers and units.
- IR-API-005: Errors shall include code, localized message key, correlation id, and retryability.
- IR-API-006: Pagination, filtering, and sorting shall be standardized.
- IR-API-007: External payloads shall pass through anti-corruption adapters.
- IR-API-008: Events shall use standard envelope, version, correlation, causation, tenant, actor, and aggregate metadata.
- IR-API-009: Sensitive fields shall be minimized.
