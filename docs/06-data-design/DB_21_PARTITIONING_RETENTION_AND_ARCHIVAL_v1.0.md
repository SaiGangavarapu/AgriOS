# DB 21 — Partitioning, Retention, and Archival

## Partition candidates

- telemetry
- weather records
- audit events
- notification delivery attempts
- support events
- market prices

## Partitioning

Prefer time-based range partitioning for high-volume append-only data.

## Retention classes

- operational
- safety evidence
- legal/audit
- research
- transient
- derived projection

## Archival

Archived data remains queryable through controlled processes and is excluded from normal operational queries.

## Deletion

Deletion uses:

- hard delete
- soft delete
- anonymization
- legal hold
- retention override

according to policy and data class.
