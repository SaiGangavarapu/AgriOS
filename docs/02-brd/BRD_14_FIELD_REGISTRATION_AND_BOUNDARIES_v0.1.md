# BRD 14 — Field Registration and Boundaries

## 1. Objective

Represent the specific land unit on which crop planning, sensing, advisories, and operations occur.

## 2. Field profile

- field name or local identifier
- farm
- area
- GPS boundary
- centroid
- village and administrative location
- soil type
- slope
- elevation
- drainage
- irrigation availability
- access path
- current use
- cultivation status

## 3. Boundary-capture methods

- walk boundary using mobile GPS
- draw on map
- import approved geospatial file
- use cadastral reference where available
- field-officer assisted capture

## 4. Boundary quality

The platform must store:

- capture method
- device
- date
- operator
- accuracy estimate
- geometry validation result
- verification status

## 5. Geometry validations

- polygon closure
- minimum area
- invalid self-intersection
- excessive point density
- overlap with another active field
- implausible location
- mismatch with farm location

Overlaps may be legitimate and must be routed for review rather than always rejected.

## 6. Field zoning

Future support may divide fields into management zones by:

- soil
- elevation
- drainage
- moisture
- satellite observation
- crop performance

## 7. Field lifecycle

`Draft → Boundary Captured → Validated → Verified → Active`

Exceptional states:

- Split
- Merged
- Inactive
- Disputed
- Archived

## 8. Business requirements

- BR-036: The platform shall support geospatial field boundaries.
- BR-037: The platform shall record boundary quality and capture method.
- BR-038: The platform shall detect invalid or suspicious geometry.
- BR-039: The platform shall preserve field lineage after split or merge.
- BR-040: Crop cycles and advisories shall reference the applicable field version.
