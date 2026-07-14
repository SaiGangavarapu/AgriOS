# API 17 — Harvest, Storage, and Traceability

## Harvest

- `POST /crop-cycles/{id}/harvest-plans`
- `GET /harvest-plans/{id}`
- `POST /harvest-events`
- `POST /harvest-events/{id}/quality-results`
- `POST /post-harvest-operations`

## Storage

- `POST /storage-units`
- `GET /storage-units/{id}`
- `POST /storage-units/{id}/store`
- `POST /storage-units/{id}/inspections`
- `POST /storage-losses`

## Traceability

- `GET /produce-lots/{id}`
- `POST /produce-lots/{id}/split`
- `POST /produce-lots/merge`
- `POST /produce-lots/{id}/transformations`
- `POST /produce-lots/{id}/transfers`
- `GET /produce-lots/{id}/lineage`
- `GET /produce-lots/{id}/quantity-ledger`

Every transformation requires source quantities, resulting quantities, location, actor, time, and reason.
