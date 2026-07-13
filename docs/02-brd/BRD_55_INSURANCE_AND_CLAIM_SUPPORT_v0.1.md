# BRD 55 — Insurance and Claim Support

## 1. Objective

Support consented insurance enrolment, policy records, event evidence, and claim preparation without making autonomous coverage or claim decisions.

## 2. Policy profile

- insurer
- scheme
- policy number
- farmer
- farm and field
- crop cycle
- insured area
- insured crop
- coverage period
- insured value
- premium
- exclusions
- status

## 3. Loss-event record

- event type
- date and period
- affected field
- crop stage
- estimated area
- observed damage
- weather evidence
- field observations
- images
- official notification
- witness
- expert assessment

## 4. Claim workflow

`Potential Loss → Farmer Notified → Evidence Collected → Claim Prepared → Farmer Approved → Submitted → Insurer Review → Decision → Payment/Closure`

## 5. Data controls

- explicit consent
- purpose limitation
- source provenance
- self-declared versus verified evidence
- insurer-specific data scope
- export audit

## 6. Business requirements

- BR-243: The platform shall maintain insurance-policy references.
- BR-244: Loss events shall link field, crop stage, weather, and evidence.
- BR-245: Claims shall require farmer approval before submission.
- BR-246: The platform shall distinguish declared, observed, expert-verified, and official evidence.
- BR-247: AgriOS shall not autonomously approve or reject claims.
