# SRS 04 — Non-Functional Requirement Conventions

## Categories

- performance
- scalability
- availability
- reliability
- security
- privacy
- accessibility
- localization
- usability
- offline operation
- interoperability
- maintainability
- observability
- recoverability
- compliance

## Format

Each NFR shall define:

- identifier
- metric
- target
- scope
- measurement method
- operating condition
- priority
- owner

## Example

`NFR-PERF-001`

The farmer dashboard shall display cached core content within two seconds on a supported mobile device under normal local conditions.

## Testability

Avoid statements such as:

- fast
- user friendly
- secure
- scalable

Replace them with measurable targets.
