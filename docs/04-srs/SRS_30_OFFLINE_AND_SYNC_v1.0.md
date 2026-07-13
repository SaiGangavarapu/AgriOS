# SRS 30 — Offline and Synchronization

- FR-OFFLINE-001: Cache assigned farmers, fields, crop cycles, tasks, advisories, and reference data.
- FR-OFFLINE-002: Record operations, observations, visits, irrigation, and draft registrations offline.
- FR-OFFLINE-003: Queue media and commands durably.
- FR-OFFLINE-004: Expose Draft, Queued, Uploading, Accepted, Conflict, Rejected, and Resolved states.
- FR-OFFLINE-005: Detect duplicates using idempotency keys.
- FR-OFFLINE-006: Preserve both versions during conflict.
- FR-OFFLINE-007: Apply deterministic merge only where safe.
- FR-OFFLINE-008: Encrypt and minimize cached data.
