# API 15 — IoT, Telemetry, and Actuator Commands

## Devices

- `POST /devices`
- `GET /devices/{id}`
- `POST /devices/{id}/provision`
- `POST /devices/{id}/install`
- `POST /devices/{id}/commission`
- `POST /devices/{id}/assignments`
- `POST /devices/{id}/calibrations`
- `POST /devices/{id}/maintenance`
- `POST /devices/{id}/decommission`
- `GET /devices/{id}/health`

## Telemetry

- `POST /integration/telemetry/batches`
- `GET /devices/{id}/telemetry`
- `GET /devices/{id}/telemetry/summary`
- `GET /devices/{id}/telemetry/gaps`

## Commands

- `POST /actuators/{id}/commands`
- `GET /actuator-commands/{commandId}`
- `POST /actuator-commands/{commandId}/cancel`

Command requests include action, target state or duration, expiry, reason, idempotency key, and expected safety policy version.
