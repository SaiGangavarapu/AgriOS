# BRD 40 — Telemetry and Data Quality

## 1. Objective

Ingest sensor observations reliably and determine whether the data is suitable for display, trends, advisories, or automation.

## 2. Telemetry attributes

- device
- sensor
- observed time
- received time
- value
- unit
- location
- sequence
- battery
- signal
- firmware
- quality flag
- calibration version

## 3. Data-quality checks

- accepted range
- rate of change
- stuck value
- missing interval
- duplicate message
- clock drift
- sequence gap
- impossible combination
- cross-sensor consistency
- battery or signal degradation

## 4. Quality states

- Valid
- Suspect
- Invalid
- Missing
- Estimated
- Corrected
- Uncalibrated

## 5. Offline synchronization

Devices and gateways shall support:

- local buffering
- replay
- duplicate detection
- ordered or reconstructable timestamps
- retry
- acknowledgement
- data-gap reporting

## 6. Retention

Retention may differ for:

- raw telemetry
- validated telemetry
- hourly summary
- daily summary
- advisory evidence
- automation evidence

## 7. Business requirements

- BR-168: The platform shall ingest time-stamped telemetry.
- BR-169: Telemetry shall include data-quality status.
- BR-170: Offline replay shall avoid duplicate records.
- BR-171: Invalid data shall not silently drive advisories or automation.
- BR-172: Retention policies shall be configurable by data class.
