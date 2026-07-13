# SA 22 — Performance and Scalability

## Initial performance targets

- cached farmer dashboard: under 2 seconds on supported mobile
- common API reads: p95 under 500 ms under pilot load
- common commands: p95 under 1 second excluding external dependencies
- critical alert dispatch initiation: under 60 seconds after validated event
- offline sync resumes automatically after connectivity returns

## Scaling strategy

Scale in this order:

1. optimize queries and indexes
2. add caching
3. separate worker workloads
4. partition time-series data
5. externalize events
6. extract high-volume services

## High-volume candidates

- telemetry
- weather ingestion
- notification delivery
- media
- search
- analytics
