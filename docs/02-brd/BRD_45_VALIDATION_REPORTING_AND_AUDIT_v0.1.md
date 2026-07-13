# BRD 45 — Validation, Reporting, and Audit

## 1. Validation

### Nutrient plan

- missing or expired soil test
- unsupported product conversion
- duplicate nutrient source
- excessive rate
- incompatible farming system
- missing expert review

### Irrigation

- missing source
- unreliable flow
- invalid sensor data
- forecast conflict
- impossible runtime
- water-quality restriction

### Weather

- missing issue or valid time
- stale forecast
- unsupported unit
- location mismatch
- withdrawn warning

### IoT

- unregistered device
- expired calibration
- invalid assignment
- duplicate telemetry
- missing command acknowledgement
- unresolved critical alert

### Advisory

- missing evidence
- missing confidence
- expired input data
- untranslated critical content
- conflicting active advisory

## 2. Reports and dashboards

- active nutrient plans
- upcoming nutrient applications
- irrigation schedule
- water-use summary
- weather-risk dashboard
- active advisories
- advisory acknowledgement
- device fleet health
- telemetry completeness
- calibration due
- automation command history
- expert-review queue
- service-level performance

## 3. Audit events

- nutrient-plan approval
- input-application override
- irrigation reschedule
- weather-source update
- advisory publication
- advisory withdrawal
- device assignment
- calibration
- command approval
- safety-rule block
- expert decision
- farmer acknowledgement

## 4. Business requirements

- BR-193: The platform shall validate nutrient, irrigation, weather, IoT, and advisory data.
- BR-194: Reports shall distinguish measured, estimated, and inferred values.
- BR-195: Critical advisory and automation actions shall generate immutable audit records.
- BR-196: Dashboards shall support geography, crop, field, and time filters.
- BR-197: Reports and exports shall respect role, consent, and data-minimization rules.
