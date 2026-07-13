# SA 06 — Database and Schema Strategy

## Strategy

Use one PostgreSQL cluster initially, with module-owned schemas.

Example schemas:

- identity
- farmer
- farm
- soilwater
- knowledge
- cropplanning
- cropcycle
- operations
- nutrient
- irrigation
- weather
- iot
- telemetry
- advisory
- traceability
- market
- economics
- audit

## Rules

- each module owns its tables and migrations
- no direct cross-schema repository access
- cross-module references use identifiers
- cross-schema foreign keys are avoided unless approved
- Flyway migration numbering is centrally governed
- read projections may combine published data
- all timestamps use UTC internally
- canonical units are stored
