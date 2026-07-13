# Anti-Corruption Layer Guidance

## 1. Purpose

Prevent external systems and legacy product models from contaminating AgriOS domain language.

## 2. Required anti-corruption layers

### Weather providers

Map provider-specific forecasts into canonical WeatherRecord and WeatherWarning contracts.

### Soil laboratories

Map laboratory methods, units, codes, and reports into SoilTest contracts.

### IoT vendors

Map vendor payloads, device identifiers, and command protocols into Device and Telemetry contracts.

### Government systems

Map programme, geography, farmer, and scheme identifiers without replacing internal identities.

### Market sources

Map commodity names, units, market codes, and prices into canonical market contracts.

### Insurance systems

Map policy and claim statuses into insurance-evidence workflows.

### Lending systems

Map lender requests into purpose-scoped evidence-package requests.

### Existing AgriOS-related products

Milk Register, Livestock, Farm Fresh, and Seller ERP integrations must use explicit contracts. Their internal entities must not be reused as AgriOS core entities.

## 3. Adapter responsibilities

- authentication
- schema translation
- unit conversion
- identity mapping
- validation
- provenance
- error translation
- retry
- rate-limit handling
- licensing controls

## 4. Rule

No external payload may pass directly into domain logic without validation and translation.
