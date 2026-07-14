# API 19 — Insurance and Lending Evidence

## Insurance

- `POST /insurance/policies`
- `GET /insurance/policies/{id}`
- `POST /insurance/loss-events`
- `POST /insurance/evidence-packages`
- `GET /insurance/evidence-packages/{id}`
- `POST /insurance/evidence-packages/{id}/approve`
- `POST /insurance/evidence-packages/{id}/submit`
- `POST /integration/insurance/callbacks`

## Lending

- `POST /lending/evidence-requests`
- `GET /lending/evidence-requests/{id}`
- `POST /lending/evidence-requests/{id}/scope`
- `POST /lending/evidence-requests/{id}/approve`
- `POST /lending/evidence-requests/{id}/share`
- `DELETE /lending/evidence-access/{id}`
- `POST /integration/lending/callbacks`

AgriOS supplies evidence but never returns an autonomous claim or credit approval decision.
