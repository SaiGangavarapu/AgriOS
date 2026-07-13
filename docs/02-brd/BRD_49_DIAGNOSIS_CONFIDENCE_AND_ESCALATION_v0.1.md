# BRD 49 — Diagnosis Confidence and Escalation

## 1. Objective

Represent uncertainty explicitly and route cases to the appropriate expert or diagnostic process.

## 2. Diagnosis sources

- farmer observation
- field-officer observation
- agronomist assessment
- laboratory result
- trap result
- image model
- weather-risk model
- combined system inference

## 3. Diagnostic status

`Suspected → Differential Review → Testing Required/Not Required → Expert Confirmed → Resolved`

Exceptional states:

- Inconclusive
- Mixed Cause
- Misdiagnosed
- Withdrawn

## 4. Confidence levels

- High
- Moderate
- Low
- Unknown
- Expert Review Required

## 5. Escalation routes

- agronomist
- entomologist
- plant pathologist
- soil scientist
- laboratory
- regulatory authority
- field visit

## 6. Required diagnostic record

- suspected issue
- alternatives
- evidence
- confidence
- missing evidence
- recommended next step
- reviewer
- decision date
- expiry or reassessment

## 7. Business requirements

- BR-213: The platform shall represent differential diagnoses.
- BR-214: Diagnostic confidence and evidence shall be visible.
- BR-215: Low-confidence and high-risk cases shall be escalated.
- BR-216: Laboratory and expert confirmation shall be distinguishable from AI inference.
- BR-217: Diagnosis corrections shall preserve history.
