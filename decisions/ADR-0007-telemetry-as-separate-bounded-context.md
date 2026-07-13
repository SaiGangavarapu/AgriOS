# ADR-0007: Telemetry as a Separate Bounded Context

## Status

Accepted

## Context

Telemetry has high volume, replay, retention, quality, and time-series concerns distinct from device ownership, installation, and maintenance.

## Decision

IoT Device owns device identity, installation, assignment, calibration, and health. Telemetry owns readings, validation, gaps, aggregates, replay, and retention.

## Consequences

- telemetry storage may scale independently
- device domain remains operationally focused
- consumers receive quality-labelled readings
- device and telemetry communicate through identifiers and events
