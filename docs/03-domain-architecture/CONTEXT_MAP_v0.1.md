# Context Map

## 1. Upstream and downstream relationships

### Identity and Access → all secured contexts

Relationship: Open Host Service and Published Language.

All contexts consume authenticated actor and authorization claims. No business context owns credentials.

### Farmer Registry → Farm and Field, Consent, Household and Organization

Relationship: Customer-Supplier.

Farmer Registry is authoritative for farmer identity and lifecycle. Downstream contexts store only farmer references.

### Consent → Reporting, Insurance Evidence, Lending Evidence, Market, Analytics

Relationship: Conformist with enforcement gateway.

Downstream contexts must check active consent for purpose-specific access.

### Farm and Field → Crop Planning, Crop Cycle, Soil and Water, IoT Device

Relationship: Customer-Supplier.

Farm and Field owns field identity, geometry, and versions.

### Agronomy Knowledge → Crop Planning, Nutrient, Irrigation, Pest and Disease, Advisory

Relationship: Published Language.

Published knowledge contracts are immutable and versioned.

### Soil and Water → Crop Planning, Nutrient, Irrigation, Advisory

Relationship: Customer-Supplier.

Consumers receive interpreted constraints and quality metadata, not direct table access.

### Weather Intelligence → Crop Planning, Irrigation, Advisory, Pest and Disease, Harvest

Relationship: Open Host Service.

Weather records include source, issue time, valid time, and confidence.

### IoT Device → Telemetry

Relationship: Customer-Supplier.

Device Context owns identity and calibration. Telemetry owns readings.

### Telemetry → Irrigation, Advisory, IoT Device Health

Relationship: Published Language.

Only validated or quality-labelled readings are published.

### Crop Planning → Crop Cycle

Relationship: Customer-Supplier.

An approved crop-plan version can activate a crop cycle.

### Crop Cycle → Farm Operations, Task and Calendar, Advisory, Harvest

Relationship: Published Language.

Crop stage and lifecycle changes are published.

### Nutrient and Irrigation → Farm Operations and Advisory

Relationship: Customer-Supplier.

Approved schedules produce operation tasks and advisory candidates.

### Pest and Disease → Advisory and Farm Operations

Relationship: Customer-Supplier.

Approved treatment plans produce tasks and advisories.

### Harvest and Post-Harvest → Produce Traceability

Relationship: Customer-Supplier.

Harvest events create produce lots.

### Produce Traceability → Market, Insurance Evidence, Reporting

Relationship: Published Language.

Lot lineage and quality summaries are shared through governed contracts.

### Farm Economics ← Farm Operations, Harvest, Market

Relationship: Conformist.

Economics consumes cost and revenue facts but owns calculations.

## 2. Forbidden relationships

- no direct database reads across contexts
- no shared mutable domain entities
- no weather provider types in core domain models
- no IoT vendor payloads outside integration adapters
- no LLM response objects in domain models
- no marketplace product model inside agronomy knowledge
