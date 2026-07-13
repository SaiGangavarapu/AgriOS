# Module Dependency Rules

## Allowed dependency direction

Presentation and adapters:

`web/api → application → domain`

Infrastructure:

`infrastructure → application ports and domain`

Domain:

`domain → shared kernel only`

## Cross-module interaction

Allowed:

- another module's published API
- another module's published event
- explicit query projection
- anti-corruption adapter

Forbidden:

- another module's JPA entity
- another module's repository
- direct cross-schema SQL
- internal package imports
- reflection to bypass boundaries
- shared mutable domain object

## High-level dependency groups

### Foundation

- identity
- authorization
- consent
- audit
- localization
- configuration
- documents

### Farm platform

- farmer
- household
- farm
- tenure
- soilwater

### Agronomy platform

- knowledge
- cropplanning
- cropcycle
- seed
- nutrient
- irrigation
- crophealth

### Operations platform

- operations
- tasks
- advisory
- expert
- harvest
- traceability

### Intelligence platform

- weather
- iotdevice
- telemetry
- reporting

### Ecosystem platform

- market
- economics
- insurance
- lending
- notification
- support

## Cycle prevention

Forbidden examples:

- cropcycle ↔ cropplanning direct mutual dependencies
- advisory ↔ knowledge direct mutual dependencies
- iotdevice ↔ telemetry direct domain imports
- market ↔ economics direct aggregate imports

Resolve using:

- published contracts
- events
- application-level ports
- read models

## Database ownership

- each module owns its tables
- migrations are grouped by module
- cross-module foreign keys require explicit ADR
- references across modules use identifiers
