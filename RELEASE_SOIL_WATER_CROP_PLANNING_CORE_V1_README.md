# AgriOS Soil, Water, Crop Knowledge, and Crop Planning Core v1.0

This cumulative release uses the uploaded, user-corrected AgriOS repository as
its authoritative baseline.

## User corrections preserved

- Maven compiler processing configuration
- Spring Modulith package boundary corrections
- Pageable imports
- corrected FarmerRegistered JSON payload
- Hibernate JSON mappings for consent
- Spring Modulith event-publication tables
- PostGIS Testcontainers compatibility
- architecture and integration test corrections

## New implementation

- laboratory registry
- soil and water sample collection
- laboratory test reports and parameter results
- publication of current soil/water profiles
- crop catalogue lifecycle
- variety catalogue
- crop requirement rules
- field crop-suitability assessment
- deterministic requirement-based scoring
- crop-plan creation and approval
- Flyway V008 through V010
- REST APIs and Testcontainers integration coverage
