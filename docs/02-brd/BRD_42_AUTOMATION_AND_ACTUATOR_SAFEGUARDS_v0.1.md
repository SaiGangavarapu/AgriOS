# BRD 42 — Automation and Actuator Safeguards

## 1. Objective

Permit controlled pump, valve, or other actuator operation without compromising farmer safety, equipment, water resources, or crop health.

## 2. Automation modes

- advisory only
- farmer-confirmed action
- scheduled action
- rule-based action
- emergency stop
- manual local override

## 3. Command attributes

- actuator
- requested action
- issuer
- reason
- authorization
- issue time
- expiry
- idempotency key
- target duration or state
- safety-rule version
- acknowledgement
- execution result

## 4. Mandatory safeguards

- manual override
- maximum runtime
- minimum off-time
- dry-run protection
- flow confirmation
- tank-level check
- pressure check
- rain lockout
- leak detection
- command expiry
- failed-command alert
- safe recovery after power loss

## 5. Authorization

High-risk control may require:

- farmer opt-in
- verified device
- current calibration
- active field assignment
- approved automation rule
- no unresolved critical alert
- recent telemetry
- role authorization

## 6. Audit

The platform shall retain:

- requested command
- safety evaluation
- approved or blocked reason
- gateway receipt
- actuator response
- observed result
- manual override

## 7. Business requirements

- BR-178: Automation shall support advisory-only and confirmation modes.
- BR-179: Actuator commands shall be authorized, expiring, and idempotent.
- BR-180: Mandatory safety interlocks shall be enforced before execution.
- BR-181: Manual override shall always remain available at the field equipment.
- BR-182: All control decisions and results shall be auditable.
