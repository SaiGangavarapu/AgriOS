# What Changed — IoT, Telemetry, and Smart Irrigation Automation Core v1.0

Added device lifecycle, telemetry ingestion, alerting, and irrigation automation.

Smart irrigation remains safety-first:

- automation policies must be approved and activated
- inactive actuators cannot be used
- decisions are recorded before commands
- approval-required mode blocks direct execution
- actuator commands require an idempotency key
- every command is auditable
