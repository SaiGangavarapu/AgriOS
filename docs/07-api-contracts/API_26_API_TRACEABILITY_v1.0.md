# API 26 — API Traceability

| API area | SRS volumes | Primary bounded contexts |
|---|---|---|
| Auth and Farmer | 02, 04, 29 | Identity, Farmer |
| Farm, Field, Tenure | 05–06 | Farm and Field, Tenure |
| Soil and Water | 07–08 | Soil and Water |
| Knowledge, Planning, Cycle, Seed | 09–12 | Knowledge, Crop Planning, Crop Cycle, Seed |
| Nutrient and Irrigation | 13–16 | Nutrient, Irrigation |
| Weather and IoT | 17–18 | Weather, IoT Device, Telemetry |
| Advisory and Crop Health | 19–20 | Advisory, Expert, Pest and Disease |
| Harvest, Storage, Traceability | 21–24 | Harvest, Storage, Traceability |
| Market and Economics | 23, 25 | Market, Economics |
| Insurance and Lending | 26–27 | Insurance, Lending |
| Reporting, Security, Offline, APIs | 28–31 | Platform Foundation |

## Implementation rule

Endpoint implementation is complete only when it has:

- OpenAPI contract
- authorization rule
- validation
- error mapping
- audit requirement
- unit/integration test
- SRS reference
