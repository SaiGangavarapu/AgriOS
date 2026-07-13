# SA 19 — Environment and Cloud Strategy

## Environments

- local
- development
- test
- sandbox
- staging
- production

## Isolation

Each environment has separate:

- database
- object storage
- secrets
- external credentials
- notification configuration

## Cloud strategy

The architecture remains cloud-portable.

Required managed services may include:

- PostgreSQL
- object storage
- container runtime
- logging
- monitoring
- secrets
- CDN/WAF

Avoid unnecessary managed-service lock-in during the pilot.
