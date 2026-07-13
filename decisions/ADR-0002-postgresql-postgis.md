# ADR-0002: PostgreSQL and PostGIS as the Primary Transactional Store

## Status

Accepted

## Decision

Use PostgreSQL as the primary transactional database and PostGIS for spatial field boundaries, geospatial joins, weather-grid mapping, soil-zone mapping, and market-distance analysis.

## Rationale

Agriculture decisions depend heavily on location and field geometry. Spatial capability is therefore a core platform requirement, not an optional extension.
