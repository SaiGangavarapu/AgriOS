# BRD 57 — Produce Lot Traceability

## 1. Objective

Trace produce from field and crop cycle through harvest, post-harvest operations, storage, transport, delivery, and sale.

## 2. Lot creation

A lot may be created by:

- harvest event
- field zone
- harvest date
- grade
- variety
- farming system
- certification status

## 3. Lot attributes

- lot identifier
- source field
- crop cycle
- crop and variety
- harvest date
- quantity
- grade
- moisture
- farming-system claim
- certification
- storage
- owner
- status

## 4. Lot transformations

- split
- merge
- grade change
- drying loss
- cleaning loss
- repacking
- processing handoff
- transfer of ownership

Lineage must be preserved.

## 5. Traceability outputs

- source field
- crop plan
- seed lot
- key input applications
- harvest
- post-harvest operations
- storage
- transport
- buyer
- quality checks

Disclosure must be purpose-specific and consented.

## 6. Business requirements

- BR-253: Harvest output shall create produce lots.
- BR-254: Lot split, merge, and transformation shall preserve lineage.
- BR-255: Quantity balances shall be validated.
- BR-256: Traceability views shall respect consent and commercial confidentiality.
- BR-257: Unsupported production or certification claims shall be blocked.
