# API 07 — File, Media, and Geospatial Contracts

## Upload lifecycle

| Method | Path |
|---|---|
| POST | `/artifacts/upload-requests` |
| POST/PUT | provider-signed upload URL |
| POST | `/artifacts/{artifactId}/complete` |
| GET | `/artifacts/{artifactId}` |
| DELETE | `/artifacts/{artifactId}` |

## Metadata

- artifact type
- owner context and owner id
- media type
- size
- checksum
- classification
- retention policy
- consent reference

## Geometry

Field boundaries use GeoJSON Polygon or MultiPolygon in WGS84.

```json
{
  "type": "Polygon",
  "coordinates": [[[80.0, 16.0], [80.1, 16.0], [80.1, 16.1], [80.0, 16.0]]]
}
```

Geometry validation responses include calculated area, validity, overlap findings, and review requirements.
