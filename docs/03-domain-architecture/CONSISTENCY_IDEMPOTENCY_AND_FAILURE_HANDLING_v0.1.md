# Consistency, Idempotency, and Failure Handling

## 1. Consistency model

Strong consistency is required:

- within an aggregate
- for aggregate invariants
- for authorization of sensitive actions
- for consent checks at the point of sharing
- for produce-lot quantity balance
- for actuator safety evaluation

Eventual consistency is acceptable:

- dashboards
- analytics
- notifications
- read projections
- external weather ingestion
- market-price updates
- search indexes

## 2. Optimistic concurrency

Use aggregate version checks for:

- farmer profile updates
- field-boundary changes
- crop-plan approval
- crop-stage updates
- advisory publication
- produce-lot transformations
- consent changes

## 3. Idempotency

Required for:

- external webhook handling
- telemetry ingestion
- notification sending
- payment-status recording
- actuator commands
- evidence sharing
- event replay

## 4. Failure categories

### Business rejection

No retry unless input or state changes.

### Concurrency conflict

Reload and retry through controlled application logic.

### Temporary technical failure

Retry with backoff and limit.

### Permanent integration failure

Move to dead-letter or exception queue.

### Safety failure

Block action and alert immediately.

### Data-quality failure

Quarantine data and prevent trusted use.

## 5. Reconciliation

Required for:

- telemetry gaps
- weather-feed outages
- notification delivery
- produce-lot balances
- payment states
- insurance and lender statuses
- actuator execution

## 6. Outbox and inbox

Use transactional outbox for reliable domain-event publication and inbox deduplication for external or cross-service messages.
