# Context Specification 05 — Nutrient, Irrigation, Weather, IoT, and Telemetry

## Nutrient Management Context

### Aggregate root

NutrientPlan

### Commands

- GenerateNutrientPlan
- ApproveNutrientPlan
- ReviseNutrientPlan
- ScheduleNutrientApplication
- CancelNutrientApplication

### Queries

- GetActiveNutrientPlan
- CompareNutrientPathways
- GetNutrientBudget
- GetUpcomingApplications

### Invariants

- commercial-product quantity must trace to nutrient requirement
- micronutrient recommendation requires evidence
- low-confidence plan cannot auto-publish

## Irrigation Context

### Aggregate root

IrrigationPlan

### Commands

- GenerateIrrigationPlan
- ScheduleIrrigation
- RescheduleIrrigation
- SkipIrrigation
- RecordIrrigationExecution
- ReconcileIrrigationVolume

### Invariants

- automated irrigation requires current valid field, source, device, and safety state
- execution volume cannot be negative
- measured and estimated volume are distinct

## Weather Intelligence Context

### Aggregate roots

- WeatherSource
- WeatherProduct
- WeatherWarning

### Commands

- RegisterWeatherSource
- IngestWeatherObservation
- IngestWeatherForecast
- IngestOfficialWarning
- RecomputeForecastConfidence
- WithdrawWeatherWarning

### Invariants

- weather record requires provider, issue time, valid time, location, unit, and source version
- official warning priority cannot be downgraded by a commercial source

## IoT Device Context

### Aggregate root

Device

### Commands

- RegisterDevice
- ProvisionDevice
- InstallDevice
- CommissionDevice
- AssignDevice
- RecordCalibration
- RecordMaintenance
- DecommissionDevice

### Invariants

- active device requires valid assignment
- actuator cannot be enabled with unresolved critical health condition
- calibration validity must be explicit

## Telemetry Context

### Aggregate root

TelemetryStream

### Commands

- IngestTelemetry
- ValidateTelemetry
- RejectTelemetry
- RecordTelemetryGap
- ReplayBufferedTelemetry

### Invariants

- duplicate telemetry event id is idempotent
- invalid telemetry cannot be promoted to trusted state
- correction must retain raw value and correction metadata
