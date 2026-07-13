# BRD 36 — Irrigation Planning and Scheduling

## 1. Objective

Determine when, where, and how much water should be applied while avoiding stress, waterlogging, salinity, runoff, and energy waste.

## 2. Planning inputs

- crop and stage
- effective root depth
- field and zone
- soil texture
- available water capacity
- current soil moisture
- recent rainfall
- forecast rainfall
- evapotranspiration estimate
- irrigation method
- source availability
- source quality
- pump capacity
- flow rate
- energy cost
- field observations

## 3. Scheduling outputs

- irrigate, delay, or skip
- target application depth
- target volume
- expected runtime
- field or zone priority
- recommended start window
- rain lockout
- next reassessment time
- reason
- confidence

## 4. Irrigation methods

- surface
- basin
- furrow
- sprinkler
- drip
- micro-sprinkler
- manual localized application

Method-specific efficiency and operational rules shall be configurable.

## 5. Execution record

- schedule
- actual start and end
- source
- field or zone
- method
- estimated or measured volume
- pump runtime
- flow
- operator
- interruptions
- result
- cost

## 6. Water accounting

The platform should summarize:

- rainfall
- irrigation
- estimated crop use
- runoff
- drainage
- source use
- energy use
- water-use efficiency indicators

## 7. Business requirements

- BR-148: The platform shall generate stage-aware irrigation schedules.
- BR-149: Scheduling shall use observed and forecast weather where available.
- BR-150: Irrigation recommendations shall expose target depth, volume, runtime, and confidence.
- BR-151: The platform shall record actual irrigation execution.
- BR-152: Water accounting shall distinguish measured, estimated, and missing values.
