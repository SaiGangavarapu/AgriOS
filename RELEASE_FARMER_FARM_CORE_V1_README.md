# AgriOS Farmer and Farm Core v1.0

This cumulative implementation milestone adds the first business capabilities:

- farmer registration, search, profile update, verification, suspension, reactivation, and merge
- farm registration and listing
- field registration
- GeoJSON Polygon/MultiPolygon boundary capture and version history
- water-source registration
- tenure creation, overlap validation, listing, and closure
- purpose-specific consent grant, history, revocation, and authorization check
- household, organization, and farm-participant support tables
- local development tenant seed
- REST APIs and Testcontainers integration test

## Important

The generated code should be verified locally with `mvn clean verify`.
GeoJSON area calculation in the application is a pilot estimate. Production area
authority should use PostGIS geography/projected calculations.
