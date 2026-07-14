# API 18 — Market and Farm Economics

## Market

- `GET /market/prices`
- `POST /buyers`
- `GET /buyers/{id}`
- `POST /buyers/{id}/requirements`
- `POST /buyer-offers`
- `GET /buyer-offers`
- `POST /buyer-offers/{id}/accept`
- `POST /deliveries`
- `POST /deliveries/{id}/confirm`
- `POST /deliveries/{id}/payment-status`

## Economics

- `POST /crop-cycles/{id}/costs`
- `POST /crop-cycles/{id}/revenues`
- `POST /crop-cycles/{id}/shared-cost-allocations`
- `GET /crop-cycles/{id}/economics`
- `POST /crop-cycles/{id}/economic-scenarios`
- `GET /farms/{id}/economics-summary`

Financial values identify whether they are confirmed, estimated, allocated, or missing.
