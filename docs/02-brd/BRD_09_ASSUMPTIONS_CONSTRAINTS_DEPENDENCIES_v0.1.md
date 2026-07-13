# BRD 09 — Assumptions, Constraints, and Dependencies

## 1. Assumptions

- farmers or field officers can identify field boundaries
- pilot experts will validate agronomy content
- official and licensed data access can be arranged
- supported crops will be limited initially
- local-language review resources will be available
- smartphones are available to at least one participant per farming household or field group

## 2. Constraints

### Connectivity

Rural connectivity may be intermittent or unavailable.

### Device capability

Farmer devices may have limited storage, memory, and battery.

### Data quality

Self-reported, sensor, laboratory, and external data may conflict.

### Expert availability

Specialists may not be available for immediate review.

### Budget

Farmers may not be able to purchase recommended inputs or sensors.

### Regulation

Seed, fertilizer, pesticide, privacy, finance, insurance, and certification requirements vary.

### Licensing

Weather, market, satellite, and map data may have usage restrictions.

## 3. Dependencies

- authoritative agronomy sources
- weather provider access
- soil laboratory partnerships
- GIS and map services
- notification providers
- local-language support
- IoT suppliers
- field pilot partners
- legal and privacy review
- cloud and deployment infrastructure

## 4. Design implications

- all external integrations require adapters
- source timestamps and provenance must be retained
- manual fallback is required
- offline queues must be durable
- recommendations must tolerate missing data
- low-confidence outputs must be explicit
