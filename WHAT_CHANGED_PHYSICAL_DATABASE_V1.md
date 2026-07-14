# What Changed — Physical Database Foundation v1.0

Added:

- `database/docker-compose.yml`
- `database/.env.example`
- `database/migration/V001__extensions_and_platform_schemas.sql`
- `database/migration/V002__tenant_and_programme.sql`
- `database/migration/V003__identity_and_farmer.sql`
- `database/migration/V004__farm_field_tenure_and_water.sql`
- `database/migration/V005__consent_audit_and_outbox.sql`
- `database/migration/V006__core_indexes.sql`
- `database/scripts/verify-database.sql`
- `docs/10-physical-database-design/*`

This milestone intentionally implements the platform foundation first. Later
milestones will extend the physical schema for agronomy, crop operations,
weather, IoT, telemetry, advisory, harvest, traceability, market, economics,
insurance, and lending.
