# BRD 39 — IoT Device Registry and Installation

## 1. Objective

Maintain trustworthy records for devices, gateways, sensors, actuators, ownership, installation, field assignment, and lifecycle.

## 2. Device types

- weather station
- soil-moisture sensor
- soil-temperature sensor
- EC or pH sensor
- rain gauge
- water-flow meter
- pressure sensor
- tank-level sensor
- borewell-level sensor
- pump controller
- valve controller
- camera
- insect trap
- gateway

## 3. Device profile

- device identifier
- manufacturer
- model
- serial number
- firmware
- hardware version
- owner
- operator
- connectivity
- power source
- installation date
- status
- warranty
- maintenance plan
- calibration requirement

## 4. Assignment

A device may be assigned to:

- farm
- field
- management zone
- water source
- pump
- storage tank
- crop cycle

Assignment history must be preserved.

## 5. Installation workflow

`Procured → Registered → Provisioned → Installed → Commissioned → Active`

Exceptional states:

- Faulty
- Offline
- Maintenance
- Decommissioned
- Lost
- Replaced

## 6. Installation record

- installer
- location
- mounting
- depth
- orientation
- representative zone
- reference measurements
- connectivity test
- calibration result
- farmer handover
- photo evidence

## 7. Business requirements

- BR-163: The platform shall maintain device identity and lifecycle.
- BR-164: Device assignment history shall be preserved.
- BR-165: Installation shall capture placement and commissioning evidence.
- BR-166: Device ownership and operating responsibility shall be distinguishable.
- BR-167: Decommissioned devices shall remain auditable.
