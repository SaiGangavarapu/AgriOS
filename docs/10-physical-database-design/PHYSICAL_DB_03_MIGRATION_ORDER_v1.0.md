# Migration Order

- V001 extensions and schemas
- V002 tenant and programme
- V003 identity and farmer
- V004 farm, field, tenure, and water
- V005 consent, audit, outbox, and idempotency
- V006 indexes

When backend Flyway integration is added, these migrations move into the backend
resources directory without renumbering.
