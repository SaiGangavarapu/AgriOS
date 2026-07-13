# BRD 43 — Advisory Generation and Explanation

## 1. Objective

Create timely, understandable, explainable, and traceable advisories using field state, approved knowledge, rules, forecasts, observations, and expert input.

## 2. Advisory categories

- crop planning
- sowing
- nutrient
- irrigation
- weather risk
- pest or disease scouting
- harvest
- device
- data-quality
- expert follow-up
- market preparation

## 3. Required advisory content

- what to do
- where to do it
- when to do it
- how much or how
- why
- consequence of delay
- evidence
- confidence
- expiry
- reassessment trigger
- expert-review status

## 4. Generation layers

- deterministic safety rules
- calculation models
- risk models
- approved knowledge retrieval
- expert-authored advice
- LLM explanation and translation

The LLM shall not be treated as the source of agronomic truth.

## 5. Advisory lifecycle

`Draft → System Generated → Review Required/Not Required → Approved → Published → Acknowledged → Completed → Expired`

Exceptional states:

- Superseded
- Withdrawn
- Rejected
- Farmer Declined

## 6. Delivery channels

- in-app
- push notification
- SMS fallback
- voice playback
- field-officer task
- printable or shareable report

## 7. Business requirements

- BR-183: Advisories shall be field and crop-cycle specific where applicable.
- BR-184: Advisories shall include action, reason, confidence, evidence, and expiry.
- BR-185: The platform shall distinguish generated, reviewed, approved, and withdrawn advisories.
- BR-186: Farmer acknowledgement and completion shall be recorded.
- BR-187: Advisory delivery shall support local language and fallback channels.
