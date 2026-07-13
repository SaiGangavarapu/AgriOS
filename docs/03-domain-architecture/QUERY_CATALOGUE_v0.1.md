# Query Catalogue

## Query design rules

Queries shall be side-effect free and may use projections.

Each query should define:

- query name
- actor and scope
- filters
- sorting
- pagination
- projection
- freshness requirement
- consent requirement
- data-quality annotations

## Operational queries

- GetFarmerDashboard
- GetFieldDigitalTwin
- GetActiveCropCycle
- GetTodayTasks
- GetUpcomingOperations
- GetActiveAdvisories
- GetCurrentSoilProfile
- GetCurrentWaterQuality
- GetDeviceHealth
- GetRecentTelemetrySummary
- GetHarvestReadiness

## Administrative queries

- SearchFarmers
- ListUnverifiedFields
- ListDuplicateFarmerCandidates
- ListExpiredConsent
- ListCalibrationDue
- ListPolicyChanges
- ListOpenIncidents
- ListSupportCases

## Analytical queries

- GetCropCycleEconomics
- CompareSeasonOutcomes
- GetAdvisoryEffectiveness
- GetWaterUseSummary
- GetDeviceDataCompleteness
- GetPilotKpis

## Query boundary rule

A context may own read projections from published events of other contexts, but the projection is not authoritative state.
