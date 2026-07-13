# SA 21 — Reliability, Backup, and Recovery

## Reliability patterns

- timeouts
- retries with backoff
- circuit breakers
- bulkheads
- idempotency
- outbox/inbox
- dead-letter queues
- reconciliation jobs
- degraded read-only mode

## Backup

- automated PostgreSQL backups
- point-in-time recovery
- object-storage versioning
- configuration backup
- audit retention
- restore testing

## Recovery

Define RPO and RTO by service class.

Automation remains disabled after recovery until:

- device state is current
- telemetry is fresh
- safety checks pass
- operator or policy re-enables control
