# DB 24 — Backup, Restore, and Reconciliation

## Backup

- automated base backups
- WAL archiving or point-in-time recovery
- object-storage versioning
- separate backup credentials
- regular restore tests

## Restore validation

Verify:

- row counts
- aggregate invariants
- latest migration version
- outbox consistency
- lot balances
- consent state
- audit continuity
- telemetry gaps
- object metadata consistency

## Reconciliation jobs

- outbox publication
- telemetry gaps
- weather freshness
- notification delivery
- lot balances
- payment status
- external insurer/lender status
- projection rebuild
