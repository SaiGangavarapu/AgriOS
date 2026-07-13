# DB 23 — Data Security, Privacy, and Masking

## Controls

- least-privilege database roles
- separate application and migration credentials
- encrypted connections
- encrypted backups
- secret rotation
- sensitive-column classification
- restricted document access
- export audit
- row/tenant scoping at application layer, with optional database policies later

## Sensitive data

- identity references
- mobile and contact
- bank references
- tenure documents
- exact field geometry
- voice and images
- financial evidence
- insurance and lending packages

## Masking

Non-production environments must use:

- synthetic data
- masked contact data
- generalized locations
- removed identity documents
- anonymized financial data
