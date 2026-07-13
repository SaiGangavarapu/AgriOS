# SA 09 — Integration Architecture

## Integration gateway

Responsibilities:

- authentication
- schema validation
- unit normalization
- identity mapping
- provenance
- retry
- throttling
- licensing controls
- audit

## Adapter families

- weather
- laboratory
- GIS
- market
- notification
- IoT
- insurer
- lender
- government
- existing AgriOS-related products

## Existing-product rule

Milk Register, Livestock, Farm Fresh, and Seller ERP integrate through versioned APIs and events. They do not share mutable database tables or domain entities.
