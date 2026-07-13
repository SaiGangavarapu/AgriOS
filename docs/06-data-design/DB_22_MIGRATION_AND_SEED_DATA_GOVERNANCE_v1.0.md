# DB 22 — Migration and Seed-Data Governance

## Flyway structure

Recommended:

```text
db/migration
├── platform
├── identity
├── farmer
├── farm
├── soilwater
├── knowledge
├── cropplanning
├── cropcycle
├── operations
├── nutrient
├── irrigation
├── weather
├── iot
├── advisory
├── traceability
└── reporting
```

## Migration rules

- immutable applied migrations
- forward-only correction
- no destructive production migration without reviewed plan
- repeatable migrations only for views or controlled reference data
- migration ownership by module
- migration validation in CI

## Seed data

Separate:

- mandatory reference data
- development demo data
- test fixtures
- pilot configuration
- production configuration

Production migrations must never insert demo farmers or fake operational data.
