# SA 13 — Weather Intelligence Architecture

## Inputs

- official warnings
- district/block advisories
- forecast providers
- local stations
- satellite estimates
- historical climate

## Pipeline

```text
Adapters
 → Raw Weather Store
 → Normalization
 → Spatial Mapping
 → Confidence Calculation
 → Field Risk Evaluation
 → Advisory Candidate
```

## Canonical weather record

- provider
- product
- issue time
- valid time
- geometry
- variable
- unit
- probability
- quality
- source version

## Confidence

Uses:

- lead time
- provider agreement
- recent local accuracy
- spatial resolution
- station consistency
- missing data

Official warnings always retain authority priority.
