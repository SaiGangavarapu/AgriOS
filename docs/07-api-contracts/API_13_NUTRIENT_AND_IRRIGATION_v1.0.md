# API 13 — Nutrient and Irrigation

## Nutrient

- `POST /crop-cycles/{id}/nutrient-plans/generate`
- `GET /nutrient-plans/{id}`
- `GET /nutrient-plans/{id}/comparison`
- `POST /nutrient-plans/{id}/approve`
- `POST /nutrient-plans/{id}/revisions`
- `POST /nutrient-plans/{id}/applications`
- `GET /crop-cycles/{id}/nutrient-budget`

## Irrigation

- `POST /crop-cycles/{id}/irrigation-plans/generate`
- `GET /irrigation-plans/{id}`
- `POST /irrigation-plans/{id}/schedules`
- `POST /irrigation-schedules/{id}/reschedule`
- `POST /irrigation-schedules/{id}/skip`
- `POST /irrigation-schedules/{id}/executions`
- `GET /fields/{fieldId}/water-accounting`

## Safety response

Generation and approval responses include:

- warnings
- blocking rules
- review requirement
- source versions
- confidence
