# Weather and Monsoon Intelligence

## 1. Purpose

Convert forecasts, warnings, observations, and climate history into field-level agricultural actions.

## 2. Source hierarchy

For India:

1. official warnings and authoritative IMD products
2. agrometeorological advisories
3. local station observations
4. approved secondary forecast providers
5. satellite-derived estimates
6. farmer observations

## 3. Time horizons

- nowcasting
- short-range
- medium-range
- extended-range
- seasonal outlook
- historical climate normals

Confidence normally decreases with forecast horizon and must be displayed.

## 4. Variables

- rainfall probability
- rainfall amount
- temperature
- humidity
- wind
- solar radiation
- pressure
- leaf wetness
- soil temperature
- evapotranspiration
- severe-weather warnings

## 5. Monsoon support

The system should not present a deterministic field-level monsoon date far in advance. It should maintain:

- climatological onset range
- official seasonal outlook
- current progression
- local rainfall observations
- soil-moisture readiness
- sowing-window confidence
- delayed-onset contingency

## 6. Agricultural decisions

### Sowing

Consider:

- accumulated effective rainfall
- forecast persistence
- soil moisture
- seedbed condition
- crop window
- dry-spell risk

### Nutrient application

Avoid or defer when:

- heavy rain is likely
- runoff risk is high
- field is saturated
- wind makes foliar application unsuitable

### Spraying

Assess:

- rain-free period
- wind
- temperature
- humidity
- label restrictions

### Irrigation

Combine:

- observed rain
- forecast rain
- soil moisture
- crop stage
- ET estimate

### Harvest

Assess:

- rain
- humidity
- wind
- drying conditions
- crop maturity

## 7. Data normalization

Each forecast record needs:

- provider
- issue time
- valid time
- location geometry
- variable
- unit
- probability
- model or product
- retrieval time

## 8. Forecast comparison

AgriOS may compare providers but must not hide disagreement. A confidence service should consider:

- lead time
- provider agreement
- recent local performance
- spatial resolution
- missing data
- local-station consistency

## 9. Alert design

Alerts must include:

- severity
- affected fields
- valid period
- recommended action
- action deadline
- source
- confidence
- acknowledgement
- superseding update

## 10. Offline delivery

Critical alerts should support:

- in-app notification
- SMS fallback
- cached advisory
- field-officer delivery
- local-language audio where feasible
