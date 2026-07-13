# BRD 53 — Market Intelligence and Buyer Workflows

## 1. Objective

Help farmers compare selling options using current market information, expected quality, logistics, payment terms, and risk.

## 2. Market-data sources

- government market prices
- mandi arrivals
- FPO prices
- buyer offers
- contract prices
- local trader quotes
- processor demand
- retail or direct-sale demand

## 3. Market record

- commodity
- variety
- grade
- market
- price type
- minimum, maximum, and modal price
- unit
- arrival quantity
- issue date
- valid date
- source
- quality flag

## 4. Buyer profile

- buyer identity
- organization
- commodities
- grades
- locations
- payment terms
- pickup or delivery
- quality requirements
- reputation or verification
- contract status

## 5. Offer comparison

Display:

- gross price
- grading deduction
- transport
- commission
- loading
- payment delay
- rejection risk
- expected net realization

## 6. Buyer workflow

`Requirement Published → Farmer/FPO Interest → Offer → Negotiation → Accepted → Scheduled → Delivered → Quality Confirmed → Paid → Closed`

## 7. Business requirements

- BR-233: The platform shall ingest market prices with source and date.
- BR-234: The platform shall support buyer profiles and produce requirements.
- BR-235: Offer comparison shall estimate net realization, not only headline price.
- BR-236: Buyer workflows shall retain quality, delivery, and payment terms.
- BR-237: Commercial offers shall remain separate from agronomic recommendations.
