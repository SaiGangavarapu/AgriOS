# BRD 27 — Crop-Cycle Management

## 1. Objective

Track one crop on one field from activation through season closure.

## 2. Crop-cycle profile

- field
- crop plan
- crop
- variety
- planned and actual sowing date
- area
- farmer or operator
- current stage
- status
- expected harvest
- actual harvest
- input and cost totals
- yield
- quality
- season outcome

## 3. Lifecycle

`Planned → Activated → Land Preparation → Sown → Established → Growing → Reproductive → Harvest Ready → Harvested → Post-Harvest → Closed`

Exceptional states:

- Cancelled
- Failed Establishment
- Re-sowing
- Partial Crop Loss
- Total Crop Loss
- Abandoned

## 4. Field allocation

Rules:

- one field may have multiple concurrent crop cycles only where intercropping or zoning is explicitly supported
- allocated area must not silently exceed field area
- crop-cycle geometry may represent part of a field
- historical cycles remain immutable except controlled corrections

## 5. Business requirements

- BR-102: The platform shall maintain crop-cycle lifecycle states.
- BR-103: Crop cycles shall reference field versions and plan versions.
- BR-104: Intercropping and partial-field allocation shall be supported.
- BR-105: Crop-loss states shall be explicit.
- BR-106: Historical cycles shall remain auditable.
