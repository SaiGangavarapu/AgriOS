# DB 16 — Geospatial Design

## Geometry types

- field boundary: `geometry(Polygon, 4326)` or `MultiPolygon`
- management zone: `geometry(Polygon, 4326)`
- device location: `geometry(Point, 4326)`
- sample location: `geometry(Point, 4326)`
- weather geometry: point, polygon, or raster reference
- administrative geography: polygon or multipolygon

## Spatial indexes

Use GiST indexes on:

- current field boundaries
- management zones
- devices
- samples
- weather coverage
- market locations

## Spatial validations

- valid polygon
- non-empty
- no self-intersection
- area threshold
- overlap review threshold
- within supported geography
- lineage after split/merge

## Area

Store derived area in hectares for quick reporting while geometry remains authoritative.
