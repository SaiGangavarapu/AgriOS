# API 10 — Soil and Water Laboratory

## Sampling

- `POST /sample-plans`
- `GET /sample-plans/{id}`
- `POST /sample-plans/{id}/assign`
- `POST /samples`
- `POST /samples/{id}/custody-events`
- `POST /samples/{id}/receive`
- `POST /samples/{id}/reject`

## Tests

- `POST /test-orders`
- `GET /test-orders/{id}`
- `POST /soil-tests`
- `POST /water-tests`
- `POST /soil-tests/{id}/publish`
- `POST /water-tests/{id}/publish`
- `GET /fields/{fieldId}/soil-profile`
- `GET /water-sources/{id}/quality-profile`
- `GET /fields/{fieldId}/tests`

## Published result request

Includes:

- sample id
- laboratory id
- tested at
- parameter values
- units
- analytical methods
- quality flags
- reviewer
