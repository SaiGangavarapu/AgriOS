# Data Ownership and Authority Matrix

| Data | Authoritative context | Consumers |
|---|---|---|
| User account | Identity and Access | All secured contexts |
| Farmer profile | Farmer Registry | Farm, Consent, Market, Insurance |
| Household membership | Household and Organization | Access, Farm |
| Consent grant | Consent | Reporting, Insurance, Lending, Analytics |
| Farm | Farm and Field | Planning, Reporting, IoT |
| Field geometry | Farm and Field | Soil, Weather, Crop Cycle, IoT |
| Tenure | Tenure | Farm, Insurance, Lending |
| Soil test | Soil and Water | Planning, Nutrient, Advisory |
| Water test | Soil and Water | Planning, Irrigation |
| Crop and variety knowledge | Agronomy Knowledge | Planning, Crop Cycle, Advisory |
| Crop plan | Crop Planning | Crop Cycle, Economics |
| Crop cycle | Crop Cycle | Operations, Advisory, Harvest |
| Seed lot | Seed | Crop Cycle, Traceability |
| Farm operation | Farm Operations | Economics, Traceability |
| Nutrient plan | Nutrient Management | Operations, Advisory |
| Irrigation plan | Irrigation | Operations, Advisory |
| Weather record | Weather Intelligence | Planning, Advisory, Harvest |
| Device identity | IoT Device | Telemetry, Irrigation |
| Telemetry | Telemetry | IoT Device, Irrigation, Advisory |
| Advisory | Advisory | Notification, Farmer UI |
| Expert decision | Expert Services | Advisory, Crop Health |
| Crop-health case | Pest and Disease | Advisory, Operations |
| Harvest event | Harvest and Post-Harvest | Traceability, Economics |
| Produce lot | Produce Traceability | Market, Reporting |
| Buyer offer | Market | Economics |
| Cost and revenue model | Farm Economics | Reporting |
| Insurance evidence | Insurance Evidence | External insurer |
| Lending evidence | Lending Evidence | External lender |
| Audit event | Audit | Security, Compliance |

## Authority rules

- consumers may cache references and projections
- only the owning context may change authoritative state
- projections are disposable and rebuildable
- external system identifiers are mappings, not internal identity
