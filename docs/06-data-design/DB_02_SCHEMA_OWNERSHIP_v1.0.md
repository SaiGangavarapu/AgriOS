# DB 02 — Schema Ownership

## Initial PostgreSQL schemas

- `identity`
- `farmer`
- `household`
- `organization`
- `consent`
- `farm`
- `tenure`
- `soilwater`
- `knowledge`
- `cropplanning`
- `cropcycle`
- `seed`
- `operations`
- `tasks`
- `nutrient`
- `irrigation`
- `weather`
- `iot`
- `telemetry`
- `advisory`
- `expert`
- `crophealth`
- `harvest`
- `traceability`
- `market`
- `economics`
- `insurance`
- `lending`
- `notification`
- `localization`
- `configuration`
- `documents`
- `audit`
- `support`
- `reporting`

## Ownership rules

- each bounded context owns one or more schemas
- only owning module writes its tables
- cross-context joins are restricted to projections and reporting
- cross-context foreign keys are avoided
- references use UUIDs and published contracts
