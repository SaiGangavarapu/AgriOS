# DB 17 — Time-Series Design

## Hypertables

- sensor readings
- device metrics
- weather observations
- weather forecasts
- actuator execution metrics

## Partition dimensions

Primary time dimension:

- `observed_at`

Optional space dimension:

- device id
- source id

## Compression

Compress older chunks after configured age.

## Retention

- raw telemetry: short-to-medium retention
- validated telemetry: longer retention
- hourly/daily aggregates: long retention
- safety evidence: retained according to policy

## Quality

Each row includes:

- raw value
- normalized value
- unit
- quality state
- calibration version
- received at
- source sequence
