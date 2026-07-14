# API 23 — External Adapter Contracts

## Adapter requirements

Every adapter shall:

- authenticate to provider
- map external ids
- normalize units and timestamps
- validate schema
- retain provider and source version
- translate provider errors
- apply retry and rate-limit policy
- record licensing constraints
- publish data-quality state

## Adapter families

- IMD or approved weather
- commercial weather
- soil and water laboratory
- GIS/maps
- market prices
- SMS/voice/messaging
- IoT gateway
- insurer
- lender
- government programme
- Milk Register
- Livestock
- Farm Fresh
- Seller ERP
