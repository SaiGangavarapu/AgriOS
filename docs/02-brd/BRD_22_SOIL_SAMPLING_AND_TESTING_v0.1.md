# BRD 22 — Soil Sampling and Testing

## 1. Objective

Enable representative, traceable, and interpretable soil testing for field-specific crop, nutrient, and amendment decisions.

## 2. Sampling triggers

- new field registration
- pre-season planning
- test expiry
- suspected deficiency
- salinity concern
- amendment follow-up
- unexplained yield decline
- research or certification programme

## 3. Sample plan

The platform shall record:

- field
- sampling purpose
- sampling date window
- intended depth
- number of cores
- sampling pattern
- zones
- crop and stage
- recent irrigation
- recent fertilizer, manure, or amendment
- collector
- laboratory

## 4. Collection workflow

`Planned → Assigned → Collected → Sealed → Dispatched → Received by Laboratory → Tested → Reviewed → Published`

Exceptional states:

- Rejected
- Contaminated
- Lost
- Insufficient Quantity
- Recollection Required

## 5. Chain of custody

Record:

- unique sample identifier
- collector
- collection time
- geo-location
- container
- seal
- transfer events
- recipient
- condition
- exceptions

## 6. Laboratory result

Each result must include:

- parameter
- value
- unit
- analytical method
- detection limit where relevant
- interpretation range source
- test date
- laboratory
- quality flag
- reviewer

## 7. Interpretation

Interpretation must consider:

- method
- crop
- target yield
- soil type
- irrigation
- geography
- sampling depth
- result age

The system shall not use a universal low-medium-high threshold without method and jurisdiction context.

## 8. Expiry

Test validity should be configurable by:

- parameter
- crop system
- management intensity
- amendment history
- local guidance

## 9. Business requirements

- BR-076: The platform shall support planned and ad hoc soil sampling.
- BR-077: The platform shall maintain chain of custody.
- BR-078: Soil results shall include method, unit, source, and quality status.
- BR-079: Interpretation shall be context-specific.
- BR-080: Expired or low-quality tests shall reduce recommendation confidence.
- BR-081: The platform shall support recollection workflows.
