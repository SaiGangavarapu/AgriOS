# API 20 — Platform Services

## Consent

- `POST /consents`
- `GET /consents`
- `GET /consents/{id}`
- `POST /consents/{id}/revoke`
- `POST /consents/{id}/renew`
- `POST /consent-checks`

## Notifications

- `GET /notifications`
- `POST /notifications/{id}/acknowledge`
- `GET /notification-preferences`
- `PUT /notification-preferences`

## Localization

- `GET /localization/languages`
- `GET /localization/packs/{language}`
- administrative translation lifecycle endpoints

## Support

- `POST /support-cases`
- `GET /support-cases`
- `GET /support-cases/{id}`
- `POST /support-cases/{id}/events`
- `POST /support-cases/{id}/resolve`
- `POST /support-cases/{id}/reopen`

## Reporting and administration

- `GET /reports/{reportCode}`
- `POST /exports`
- `GET /exports/{id}`
- configuration, policy, programme, reference-data, audit, and incident endpoints restricted to privileged roles
