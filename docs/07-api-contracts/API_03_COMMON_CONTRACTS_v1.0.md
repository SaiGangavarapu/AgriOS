# API 03 — Common Contracts

## Resource metadata

```json
{
  "id": "uuid",
  "version": 3,
  "status": "ACTIVE",
  "createdAt": "2026-07-14T12:00:00Z",
  "updatedAt": "2026-07-14T12:30:00Z"
}
```

## Actor reference

```json
{
  "actorId": "uuid",
  "actorType": "USER",
  "displayName": "Field Officer"
}
```

## Quantity

```json
{
  "value": 12.5,
  "unit": "KG"
}
```

## Money

```json
{
  "amount": 1250.00,
  "currency": "INR"
}
```

## Confidence

```json
{
  "level": "MODERATE",
  "score": 0.72,
  "reasonCodes": ["FORECAST_PROVIDER_DISAGREEMENT"]
}
```

## Evidence reference

```json
{
  "evidenceId": "uuid",
  "evidenceType": "SOIL_TEST",
  "source": "LABORATORY",
  "version": "1",
  "observedAt": "2026-07-10T00:00:00Z",
  "qualityStatus": "VALID"
}
```
