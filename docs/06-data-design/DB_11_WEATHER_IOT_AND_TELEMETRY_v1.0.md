# DB 11 — Weather, IoT, and Telemetry

## Weather tables

- `weather.weather_source`
- `weather.weather_product`
- `weather.weather_record`
- `weather.weather_warning`
- `weather.forecast_confidence`
- `weather.field_weather_projection`

## IoT tables

- `iot.device`
- `iot.sensor`
- `iot.actuator`
- `iot.gateway`
- `iot.device_assignment`
- `iot.installation_record`
- `iot.calibration_record`
- `iot.maintenance_record`
- `iot.device_health_event`
- `iot.actuator_command`

## Telemetry hypertables

- `telemetry.sensor_reading`
- `telemetry.device_metric`
- `telemetry.telemetry_gap`
- `telemetry.telemetry_daily_summary`

## Rules

- raw and validated telemetry separated by quality state
- command idempotency key unique
- calibration version linked to reading where required
- weather provenance mandatory
