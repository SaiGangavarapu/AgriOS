# DB 04 — Tenant and Programme Model

## Tables

### `configuration.tenant`

- id
- code
- name
- status
- default_language
- timezone
- country_code
- created_at

### `configuration.programme`

- id
- tenant_id
- code
- name
- description
- valid_from
- valid_to
- status

### `configuration.programme_geography`

- programme_id
- geography_id

### `configuration.programme_feature`

- programme_id
- feature_code
- enabled
- configuration_json

## Rules

- tenant is operational isolation boundary
- programme is optional and time-bounded
- farmer data ownership is not transferred to tenant
- cross-tenant sharing requires explicit contract and consent
