# BRD 58 — Validation, Reporting, and Audit

## 1. Validation

### Pest and disease

- unknown crop or stage
- insufficient observation evidence
- contradictory symptoms
- invalid severity
- unsupported threshold
- low-confidence automated classification

### Treatment

- unapproved product
- wrong crop or target
- unsafe dose
- invalid interval
- pre-harvest conflict
- weather conflict
- repeated resistance group
- missing PPE instruction

### Harvest and storage

- harvest before maturity
- quantity greater than available crop estimate without explanation
- invalid moisture
- negative stock
- storage over capacity
- lot-balance mismatch

### Market and finance

- stale price
- incompatible unit
- duplicate sale
- payment mismatch
- unverified buyer
- unsupported financial allocation

### Insurance and lending

- missing consent
- expired policy
- field mismatch
- unverifiable evidence
- excessive requested data scope

## 2. Reports and dashboards

- scouting due and completed
- active pest and disease cases
- treatment schedule
- treatment effectiveness
- harvest readiness
- harvest and loss
- storage inventory
- lot traceability
- market-price comparison
- buyer offers
- crop-cycle profitability
- insurance policies and claims
- financial-evidence sharing log

## 3. Audit events

- diagnosis change
- expert confirmation
- treatment approval
- safety override
- harvest correction
- lot split or merge
- grade change
- buyer-offer acceptance
- insurance evidence export
- lender evidence sharing
- consent revocation

## 4. Business requirements

- BR-258: The platform shall validate pest, treatment, harvest, storage, market, finance, insurance, and traceability data.
- BR-259: Reports shall distinguish observed, inferred, expert-confirmed, and self-declared values.
- BR-260: High-risk treatment and evidence-sharing actions shall be auditable.
- BR-261: Lot, stock, and quantity movements shall maintain balance integrity.
- BR-262: All reports and exports shall enforce role, consent, and confidentiality.
