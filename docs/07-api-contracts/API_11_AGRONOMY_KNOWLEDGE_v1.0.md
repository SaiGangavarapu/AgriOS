# API 11 — Agronomy Knowledge

## Crops and varieties

- `POST /knowledge/crops`
- `GET /knowledge/crops`
- `GET /knowledge/crops/{cropId}`
- `POST /knowledge/varieties`
- `GET /knowledge/varieties`
- `GET /knowledge/varieties/{varietyId}`

## Knowledge lifecycle

- `POST /knowledge/items`
- `GET /knowledge/items/{id}`
- `POST /knowledge/items/{id}/submit-review`
- `POST /knowledge/items/{id}/approve`
- `POST /knowledge/items/{id}/publish`
- `POST /knowledge/items/{id}/supersede`
- `POST /knowledge/items/{id}/withdraw`
- `GET /knowledge/search`

## Applicability filters

- crop
- variety
- geography
- soil
- irrigation condition
- farming system
- crop stage
- valid date
- evidence grade
