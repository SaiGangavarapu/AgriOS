# API 14 — Weather and Monsoon

## Internal/query APIs

- `GET /weather/fields/{fieldId}/current`
- `GET /weather/fields/{fieldId}/forecast`
- `GET /weather/fields/{fieldId}/warnings`
- `GET /weather/fields/{fieldId}/risk-summary`
- `GET /weather/monsoon/status`
- `GET /weather/sources`
- `GET /weather/products/{id}`

## Ingestion APIs

Restricted to trusted adapters:

- `POST /integration/weather/observations`
- `POST /integration/weather/forecasts`
- `POST /integration/weather/warnings`

## Forecast response

Includes:

- issue time
- valid range
- location
- variables
- probability
- provider
- confidence
- disagreement indicators
- source freshness
