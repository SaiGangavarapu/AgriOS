# DB 01 — Principles and Conventions

## Technology baseline

- PostgreSQL
- PostGIS
- TimescaleDB or compatible time-series extension where justified
- Flyway
- UTF-8
- UTC timestamps

## Naming

- schemas: lowercase snake_case
- tables: singular or clearly collective nouns, consistently applied
- primary keys: `id`
- foreign-reference identifiers: `<entity>_id`
- timestamps: `<action>_at`
- booleans: positive names such as `is_active`
- enums: lookup tables or constrained text based on stability

## Data rules

- UUID primary identifiers
- tenant scope on tenant-owned data
- immutable historical versions where required
- canonical SI or approved internal units
- soft delete only where business history must remain
- append-only audit data
- optimistic-lock version column on mutable aggregates
- no cross-context table writes
