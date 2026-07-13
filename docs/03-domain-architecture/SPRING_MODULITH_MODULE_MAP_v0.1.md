# Spring Modulith Module Map

## Proposed top-level modules

```text
com.agrios
├── identity
├── farmer
├── household
├── consent
├── farm
├── tenure
├── soilwater
├── knowledge
├── cropplanning
├── cropcycle
├── seed
├── operations
├── tasks
├── nutrient
├── irrigation
├── weather
├── iotdevice
├── telemetry
├── advisory
├── expert
├── crophealth
├── harvest
├── traceability
├── market
├── economics
├── insurance
├── lending
├── notification
├── localization
├── configuration
├── policy
├── documents
├── audit
├── support
├── reporting
└── integration
```

## Module internal structure

```text
<module>
├── api
├── application
├── domain
└── infrastructure
```

## Dependency rules

- `domain` depends only on the JDK and approved shared-kernel types
- `application` depends on domain and ports
- `infrastructure` implements ports
- `api` exposes commands, queries, and published contracts
- modules interact through public APIs or events
- package-private types are preferred for internal implementation

## Initial grouping option

To avoid excessive module count in the first implementation, related contexts may be grouped operationally while preserving internal boundaries:

- farmer-platform
- farm-platform
- agronomy-platform
- crop-operations
- intelligence-platform
- ecosystem-platform
- platform-foundation

This grouping must not erase bounded-context ownership.
