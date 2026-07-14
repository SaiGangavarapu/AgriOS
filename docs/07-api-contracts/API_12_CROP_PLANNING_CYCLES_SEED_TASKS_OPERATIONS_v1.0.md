# API 12 — Crop Planning, Crop Cycles, Seed, Tasks, and Operations

## Planning

- `POST /fields/{fieldId}/crop-suitability-assessments`
- `GET /crop-suitability-assessments/{id}`
- `POST /crop-plans`
- `GET /crop-plans/{id}`
- `GET /crop-plans/{id}/scenarios`
- `POST /crop-plans/{id}/select-scenario`
- `POST /crop-plans/{id}/approve`
- `POST /crop-plans/{id}/revisions`

## Crop cycles

- `POST /crop-cycles`
- `GET /crop-cycles/{id}`
- `POST /crop-cycles/{id}/activate`
- `POST /crop-cycles/{id}/sowing`
- `POST /crop-cycles/{id}/stage-observations`
- `POST /crop-cycles/{id}/stage-corrections`
- `POST /crop-cycles/{id}/losses`
- `POST /crop-cycles/{id}/close`

## Seed

- `POST /seed-lots`
- `GET /seed-lots/{id}`
- `POST /seed-lots/{id}/germination-tests`
- `POST /seed-lots/{id}/treatments`
- `POST /seed-lots/{id}/allocations`

## Tasks and operations

- `GET /tasks`
- `POST /tasks/{id}/start`
- `POST /tasks/{id}/complete`
- `POST /tasks/{id}/defer`
- `POST /farm-operations`
- `POST /farm-operations/{id}/complete`
- `POST /farm-operations/{id}/correct`
