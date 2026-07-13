# BRD 37 — Weather Data and Forecast Management

## 1. Objective

Ingest, normalize, compare, and govern weather observations, forecasts, warnings, and derived agricultural indicators.

## 2. Weather-source categories

- authoritative government source
- agrometeorological advisory source
- on-farm weather station
- approved commercial forecast
- satellite-derived observation
- farmer observation
- historical climate dataset

## 3. Weather record attributes

- provider
- product
- issue time
- valid time
- location or geometry
- variable
- value
- unit
- probability
- severity
- model or source version
- retrieval time
- quality flag

## 4. Variables

- rainfall probability
- rainfall amount
- minimum and maximum temperature
- humidity
- wind speed and direction
- solar radiation
- pressure
- leaf wetness
- soil temperature
- evapotranspiration
- severe-weather warning

## 5. Forecast horizons

- nowcast
- short range
- medium range
- extended range
- seasonal outlook
- climate normal

## 6. Source comparison

The platform shall retain disagreement and may calculate confidence using:

- lead time
- provider agreement
- historical local accuracy
- spatial resolution
- observation consistency
- missing data
- warning authority

## 7. Business requirements

- BR-153: The platform shall support multiple weather sources through adapters.
- BR-154: Weather data shall retain provider, issue time, valid time, unit, and location.
- BR-155: Forecast disagreement shall remain visible.
- BR-156: Official emergency warnings shall receive priority.
- BR-157: Weather records shall support field-level geospatial association.
