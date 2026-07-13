# Repository and Port Catalogue

## Repository rules

- repositories are owned by one bounded context
- repository interfaces operate on aggregate roots
- no generic repository abstraction across all contexts
- cross-context lookups use ports, APIs, or projections
- persistence technology shall not leak into domain interfaces

## Repository examples

- FarmerRepository
- FarmRepository
- FieldRepository
- SoilTestRepository
- KnowledgeItemRepository
- CropPlanRepository
- CropCycleRepository
- SeedLotRepository
- FarmOperationRepository
- NutrientPlanRepository
- IrrigationPlanRepository
- DeviceRepository
- AdvisoryRepository
- CropHealthCaseRepository
- ProduceLotRepository
- ConsentGrantRepository
- SupportCaseRepository

## Outbound ports

### External systems

- WeatherProviderPort
- SoilLaboratoryPort
- NotificationProviderPort
- MapProviderPort
- MarketDataProviderPort
- InsuranceProviderPort
- LenderProviderPort
- ObjectStoragePort
- SpeechRecognitionPort
- TextToSpeechPort
- ModelInferencePort

### Internal context ports

- FarmerReferencePort
- FieldReferencePort
- ActiveConsentPort
- CurrentSoilProfilePort
- PublishedKnowledgePort
- CurrentCropCyclePort
- ValidatedTelemetryPort
- AdvisoryPublicationPort
- AuditPort

## Adapter rule

Adapters translate and validate; they do not contain domain policy.
