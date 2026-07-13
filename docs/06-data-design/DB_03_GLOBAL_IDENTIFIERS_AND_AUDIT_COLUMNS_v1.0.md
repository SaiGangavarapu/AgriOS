# DB 03 — Global Identifiers and Audit Columns

## Standard columns

Recommended aggregate tables:

- `id uuid primary key`
- `tenant_id uuid not null`
- `programme_id uuid null`
- `version bigint not null`
- `status varchar(...) not null`
- `created_at timestamptz not null`
- `created_by uuid null`
- `updated_at timestamptz not null`
- `updated_by uuid null`

## Optional columns

- `effective_from`
- `effective_to`
- `archived_at`
- `archived_by`
- `source_system`
- `external_reference`
- `correlation_id`

## Versioning

Use optimistic locking with `version`.

Historical version tables should use:

- parent id
- version number
- valid from
- valid to
- reason
- changed by
