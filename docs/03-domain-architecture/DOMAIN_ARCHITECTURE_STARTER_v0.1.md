# Domain Architecture Starter

## Proposed bounded contexts

- Identity and Access
- Farmer Registry
- Farm and Field
- Soil and Water
- Crop Knowledge
- Seed and Variety
- Crop Planning
- Crop Cycle
- Nutrient Management
- Irrigation
- Weather Intelligence
- IoT Device Management
- Pest and Disease
- Advisory
- Expert Review
- Task and Calendar
- Traceability
- Consent
- Market Intelligence
- Notifications
- AI Assistant
- Analytics

## Initial aggregate candidates

### Farmer Registry

- Farmer
- FarmerHousehold
- FarmerOrganizationMembership
- ConsentProfile

### Farm and Field

- Farm
- Field
- FieldBoundary
- IrrigationSource
- WaterSource

### Crop Cycle

- CropCycle
- CropStage
- FarmOperation
- CropObservation
- Harvest

### Soil and Water

- SoilSample
- SoilTest
- WaterSample
- WaterTest
- SoilProfile

### Advisory

- Advisory
- Recommendation
- RecommendationEvidence
- RecommendationReview
- FarmerAcknowledgement

### IoT

- Device
- Sensor
- Gateway
- DeviceAssignment
- CalibrationRecord
- TelemetryReading
- DeviceAlert

## Initial domain events

- FarmerRegistered
- FarmRegistered
- FieldBoundaryCaptured
- SoilSampleCollected
- SoilTestImported
- CropPlanCreated
- CropCycleStarted
- SowingRecorded
- WeatherForecastUpdated
- HeavyRainRiskDetected
- SensorReadingReceived
- SoilMoistureLow
- IrrigationRecommended
- IrrigationCompleted
- NutrientApplicationRecorded
- DiseaseRiskDetected
- ExpertReviewRequested
- AdvisoryApproved
- HarvestRecorded
