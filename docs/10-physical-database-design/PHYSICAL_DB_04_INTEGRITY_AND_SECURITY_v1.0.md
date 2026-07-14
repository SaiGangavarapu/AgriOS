# Integrity and Security

Implemented controls:

- UUID primary keys
- tenant references
- optimistic version columns
- lifecycle check constraints
- one current boundary per field
- PostGIS geometry types and indexes
- tenure and consent date checks
- append-only audit design
- unique idempotency key
- unpublished outbox index

Production hardening will add separate migration and runtime users, secret
management, encrypted backups, masking for non-production data, and optional
row-level security after the tenancy model is validated.
