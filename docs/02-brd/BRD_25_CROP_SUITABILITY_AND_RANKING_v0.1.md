# BRD 25 — Crop Suitability and Ranking

## 1. Objective

Rank candidate crops and varieties for a field and season using agronomic, weather, water, operational, and economic context.

## 2. Inputs

- field soil
- water quality
- water reliability
- forecast and climate history
- sowing date
- previous crop
- pest and disease history
- farmer budget
- labour
- machinery
- seed availability
- market conditions
- household food and fodder needs

## 3. Hard constraints

Potential rejection reasons:

- unsuitable soil
- crop duration exceeds growing period
- insufficient water
- excessive salinity
- incompatible sowing date
- unsupported geography
- unavailable planting material
- legal or programme restriction

## 4. Scored criteria

- agronomic fit
- water fit
- climate resilience
- farmer experience
- investment need
- gross-margin range
- market stability
- rotation value
- food or fodder value
- risk concentration

## 5. Output

For each candidate:

- score
- confidence
- supporting evidence
- rejected constraints
- recommended varieties
- sowing window
- input range
- water demand
- expected duration
- yield range
- price scenarios
- major risks
- contingency option

## 6. Business requirements

- BR-092: The platform shall rank multiple crop options.
- BR-093: Hard constraints shall be visible.
- BR-094: Scoring factors and weights shall be configurable.
- BR-095: Farmer preferences shall be captured without hiding agronomic risk.
- BR-096: Recommendations shall include confidence and alternatives.
