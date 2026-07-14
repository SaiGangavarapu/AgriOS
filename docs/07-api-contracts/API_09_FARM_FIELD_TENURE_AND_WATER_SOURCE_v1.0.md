# API 09 — Farm, Field, Tenure, and Water Source

## Farm

- `POST /farms`
- `GET /farms/{farmId}`
- `PATCH /farms/{farmId}`
- `GET /farmers/{farmerId}/farms`
- `POST /farms/{farmId}/participants`
- `POST /farms/{farmId}/verify`
- `POST /farms/{farmId}/archive`

## Field

- `POST /farms/{farmId}/fields`
- `GET /fields/{fieldId}`
- `PATCH /fields/{fieldId}`
- `POST /fields/{fieldId}/boundaries`
- `POST /fields/{fieldId}/boundaries/{versionId}/validate`
- `POST /fields/{fieldId}/boundaries/{versionId}/verify`
- `POST /fields/{fieldId}/split`
- `POST /fields/merge`
- `POST /fields/{fieldId}/zones`
- `GET /fields/{fieldId}/history`

## Tenure

- `POST /fields/{fieldId}/tenure-arrangements`
- `GET /fields/{fieldId}/tenure-arrangements`
- `POST /tenure-arrangements/{id}/end`
- `POST /tenure-arrangements/{id}/dispute`

## Water sources

- `POST /farms/{farmId}/water-sources`
- `GET /water-sources/{id}`
- `PATCH /water-sources/{id}`
- `POST /water-sources/{id}/field-links`
- `POST /water-sources/{id}/inspections`
