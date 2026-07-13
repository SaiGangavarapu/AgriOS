# BRD 20 — Validation, Exception, and Lifecycle Rules

## 1. Objective

Define consistent business validation while allowing review of legitimate exceptions.

## 2. Validation classes

### Blocking validation

Used where proceeding would create legal, safety, or severe integrity risk.

### Warning validation

Allows continuation with acknowledgement.

### Review-required validation

Routes the record for human decision.

## 3. Examples

### Farmer

- invalid mobile format
- duplicate identity reference
- missing preferred language
- conflicting active profile

### Farm

- no active operator
- unsupported location
- duplicate farm declaration

### Field

- invalid polygon
- overlap
- zero or implausible area
- field outside configured pilot geography

### Tenure

- expired lease
- conflicting cultivator
- missing tenure basis

### Water source

- unknown availability
- unsafe water-quality result
- expired quality test

### Consent

- expired consent
- recipient mismatch
- purpose mismatch
- revoked consent

## 4. Record correction

Corrections must preserve:

- original value
- corrected value
- reason
- actor
- timestamp
- evidence

## 5. Merge and split

Supported controlled operations:

- merge duplicate farmer profiles
- split field
- merge fields
- transfer operational responsibility
- close expired tenure

## 6. Business requirements

- BR-066: Validation rules shall indicate severity and reason.
- BR-067: Legitimate exceptions shall support review instead of silent rejection.
- BR-068: Corrections shall be auditable.
- BR-069: Merge and split operations shall preserve lineage.
- BR-070: Lifecycle transitions shall be explicit and permission-controlled.
