# SA 18 — Deployment Topology

## Initial production topology

```text
CDN / WAF
   |
Frontend Hosting
   |
API Gateway / Reverse Proxy
   |
AgriOS Backend
   |
--------------------------------------
| PostgreSQL/PostGIS | Redis | Search |
| Time-Series Store | Object Storage |
--------------------------------------
   |
External Providers
```

## Optional worker processes

- weather ingestion
- telemetry ingestion
- notification delivery
- media processing
- report generation
- AI orchestration

## Edge topology

```text
Sensors → Gateway → Internet → AgriOS
                ↘ local rules and buffering
```
