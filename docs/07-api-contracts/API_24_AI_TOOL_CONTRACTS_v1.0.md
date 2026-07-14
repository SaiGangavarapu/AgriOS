# API 24 — AI Tool Contracts

## Principles

The AI assistant may call only registered, authorized tools. Tool output remains structured and is not replaced by model invention.

## Core tools

- `getFarmerContext`
- `getFieldDigitalTwin`
- `getCurrentSoilProfile`
- `getCurrentWaterQuality`
- `getActiveCropCycle`
- `getWeatherForecast`
- `getActiveAdvisories`
- `getUpcomingTasks`
- `assessCropSuitability`
- `generateNutrientPlan`
- `generateIrrigationRecommendation`
- `createCropHealthObservation`
- `createExpertCase`
- `acknowledgeAdvisory`

## Tool contract fields

- tool name and version
- purpose
- required permission
- input JSON schema
- output JSON schema
- side-effect classification
- confirmation requirement
- audit classification
- timeout
- error codes

Side-effecting tools require explicit confirmation when the action is high impact.
