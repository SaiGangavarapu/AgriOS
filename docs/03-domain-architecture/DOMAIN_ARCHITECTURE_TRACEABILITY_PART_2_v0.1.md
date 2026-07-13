# Domain Architecture Traceability — Part 2

| Domain concern | Primary contexts | Key architecture artifacts |
|---|---|---|
| Identity and onboarding | Identity, Farmer, Household | commands, repositories, consent checks |
| Farm and field | Farm and Field, Tenure | aggregates, field versioning, GIS ports |
| Soil and water | Soil and Water | samples, tests, interpretations |
| Knowledge governance | Agronomy Knowledge | publication lifecycle, evidence ports |
| Crop planning | Crop Planning | suitability service, plan aggregate |
| Crop execution | Crop Cycle, Operations, Tasks | lifecycle, task saga |
| Nutrient and irrigation | Nutrient, Irrigation | plan aggregates, safety policies |
| Weather and IoT | Weather, IoT Device, Telemetry | adapters, quality, event flows |
| Advisory and expertise | Advisory, Expert Services | publication policy, review process |
| Crop health | Pest and Disease | diagnosis and treatment invariants |
| Harvest and traceability | Harvest, Produce Traceability | lot lineage and balance |
| Market and economics | Market, Farm Economics | offer and financial projections |
| Insurance and lending | Insurance Evidence, Lending Evidence | consented evidence packages |
| Platform services | Notification, Localization, Configuration, Audit, Support, Reporting | generic platform contracts |

## BRD alignment

Patch 010 operationalizes BR-001 through BR-340 by defining where commands, invariants, data authority, and workflows belong.

## Next architecture step

The next patch should define:

- ubiquitous language dictionary by context
- detailed aggregate state diagrams
- command acceptance criteria
- event payload contracts
- module dependency tests
- ADRs resolving the highest-priority open decisions
