# BRD to Context Traceability

| BRD capability | Primary bounded context | Supporting contexts |
|---|---|---|
| Farmer onboarding | Farmer Registry | Identity, Localization, Audit |
| Household and organization | Household and Organization | Authorization |
| Farm and field | Farm and Field | Tenure, Audit |
| Consent | Consent | Identity, Audit |
| Soil and water | Soil and Water | Documents, Agronomy Knowledge |
| Crop and variety knowledge | Agronomy Knowledge | Localization |
| Crop suitability | Crop Planning | Soil, Weather, Economics |
| Crop cycle | Crop Cycle | Farm and Field, Tasks |
| Seed | Seed | Agronomy Knowledge, Crop Cycle |
| Farm operations | Farm Operations | Crop Cycle, Economics |
| Crop calendar | Task and Calendar | Advisory, Notification |
| Nutrient | Nutrient Management | Soil, Knowledge, Weather |
| Irrigation | Irrigation | Weather, Telemetry, IoT Device |
| Weather and monsoon | Weather Intelligence | Advisory |
| IoT devices | IoT Device | Telemetry, Support |
| Advisory | Advisory | Knowledge, Expert, Notification |
| Expert review | Expert Services | Advisory, Crop Health |
| Pest and disease | Pest and Disease | Knowledge, Expert |
| Harvest and storage | Harvest and Post-Harvest | Traceability |
| Market | Market | Traceability, Economics |
| Farm economics | Farm Economics | Operations, Harvest, Market |
| Insurance | Insurance Evidence | Consent, Traceability |
| Lending | Lending Evidence | Consent, Economics |
| Produce traceability | Produce Traceability | Harvest, Market |
| Multilingual UX | Localization | All presentation layers |
| Offline | Integration/Application layer | All relevant contexts |
| Security | Identity and Access | Audit, Policy |
| Analytics | Reporting and Analytics | All published data products |
