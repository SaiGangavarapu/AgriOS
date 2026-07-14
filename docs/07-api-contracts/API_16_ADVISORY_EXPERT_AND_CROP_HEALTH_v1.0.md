# API 16 — Advisory, Expert, and Crop Health

## Advisories

- `POST /advisories/generate`
- `GET /advisories/{id}`
- `GET /fields/{fieldId}/advisories`
- `POST /advisories/{id}/request-review`
- `POST /advisories/{id}/approve`
- `POST /advisories/{id}/publish`
- `POST /advisories/{id}/acknowledge`
- `POST /advisories/{id}/complete`
- `POST /advisories/{id}/withdraw`

## Expert cases

- `POST /expert-cases`
- `GET /expert-cases/{id}`
- `GET /expert-cases`
- `POST /expert-cases/{id}/triage`
- `POST /expert-cases/{id}/assign`
- `POST /expert-cases/{id}/request-information`
- `POST /expert-cases/{id}/decisions`

## Crop health

- `POST /scouting-plans`
- `POST /crop-health-observations`
- `POST /crop-health-observations/{id}/images`
- `POST /crop-health-cases/{id}/differential-diagnoses`
- `POST /crop-health-cases/{id}/confirm`
- `POST /crop-health-cases/{id}/treatment-plans`
- `POST /treatment-plans/{id}/approve`
- `POST /treatment-plans/{id}/applications`
- `POST /treatment-plans/{id}/effectiveness-reviews`
