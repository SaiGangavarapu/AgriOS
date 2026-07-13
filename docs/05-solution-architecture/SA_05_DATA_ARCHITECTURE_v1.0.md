# SA 05 — Data Architecture

## Transactional data

PostgreSQL stores:

- farmer
- farm
- field
- crop cycles
- tests
- plans
- operations
- advisories
- market
- economics
- consent
- audit references

## Spatial data

PostGIS stores:

- field boundaries
- management zones
- device locations
- weather geometries
- market distance
- soil-zone intersections

## Time-series data

TimescaleDB or equivalent stores:

- telemetry
- weather observations
- summarized sensor metrics
- device health history

## Object storage

Stores:

- reports
- images
- voice
- laboratory files
- invoices
- geospatial imports
- certificates

## Search

OpenSearch or PostgreSQL search initially for:

- knowledge
- farmer lookup
- advisories
- documents
- logs and operational search

## Analytics

A governed analytical store is introduced after pilot validation.
