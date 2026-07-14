# AgriOS Weather Intelligence, Monsoon, and Climate Risk Core v1.0

Implemented:

- weather-provider registry
- field weather locations
- normalized observation ingestion
- forecast-run and forecast-period ingestion
- official/provider warning ingestion
- current field forecast query
- active field warnings
- monsoon-season status and predictions
- deterministic field climate-risk assessment
- source confidence and reason codes
- Flyway migrations V017 and V018

External provider clients remain adapters. This release defines the normalized
AgriOS contracts without binding the domain to one commercial provider.
