# IoT and Edge Agriculture

## 1. Objective

Use devices only where measurement or automation improves a decision, reduces labour, prevents loss, or creates trustworthy records.

## 2. Device categories

### Weather

- temperature
- humidity
- rainfall
- wind
- solar radiation
- pressure

### Soil

- moisture
- temperature
- EC
- pH
- matric potential

### Water

- flow
- pressure
- tank level
- borewell level
- quality

### Crop

- leaf wetness
- canopy temperature
- camera
- insect trap
- dendrometer

### Equipment

- pump state
- energy meter
- valve position
- controller status

## 3. Device trust model

Each device must have:

- device identity
- manufacturer
- model
- firmware
- owner
- installation
- field assignment
- calibration status
- maintenance history
- health state
- data-quality score

## 4. Telemetry quality

Validate:

- range
- rate of change
- missing sequence
- duplicate data
- clock drift
- stuck sensor
- battery
- signal quality
- cross-sensor consistency

## 5. Connectivity

### LoRaWAN

Useful for low-power, long-range, low-throughput devices.

### Cellular

Useful where direct device connectivity is available, with higher power and operating cost.

### Wi-Fi and Bluetooth

Useful for local infrastructure and provisioning.

### Store and forward

Gateways and devices must buffer during connectivity loss.

## 6. Edge gateway responsibilities

- authenticate devices
- buffer data
- normalize timestamps
- validate ranges
- execute approved local rules
- control actuators
- maintain safety interlocks
- synchronize configuration
- report health

## 7. Command safety

Commands must include:

- target device
- requested state
- issuer
- authorization
- expiry
- idempotency key
- acknowledgement
- execution result
- override status

## 8. Calibration

Calibration records should include:

- method
- reference instrument
- technician
- date
- result
- correction
- expiry
- environmental conditions

## 9. Low-cost nutrient sensors

They may support screening or trend detection, but laboratory correlation and local calibration are required before agronomic use. The platform must expose the data-quality grade.

## 10. Pilot design

Begin with:

- soil moisture
- rainfall
- temperature and humidity
- pump state
- flow meter
- gateway buffering

Measure value before expanding to advanced sensors.

## 11. Proposed event flow

`Device → Gateway → Ingestion → Validation → Time-Series Store → Rule Engine → Advisory/Alert`

Commands:

`Approved Rule/User → Command Service → Gateway → Safety Check → Actuator → Result`
