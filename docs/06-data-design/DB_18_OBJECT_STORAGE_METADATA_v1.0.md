# DB 18 — Object-Storage Metadata

## Table

`documents.stored_artifact`

Fields:

- id
- tenant_id
- owner_context
- owner_entity_id
- artifact_type
- object_key
- media_type
- size_bytes
- checksum
- classification
- retention_policy
- created_at
- deleted_at
- anonymized_at

## Supported artifacts

- soil reports
- water reports
- images
- voice
- invoices
- certificates
- geospatial imports
- generated reports
- expert attachments

## Rules

- object key immutable
- checksum required
- access through signed or controlled URLs
- classification and retention mandatory
