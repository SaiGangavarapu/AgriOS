# Agronomy Evidence Governance

## 1. Objective

AgriOS recommendations may influence yield, input expenditure, soil condition, water use, food safety, and farmer income. Every recommendation must therefore be traceable to evidence and constrained by geographic, crop, season, and field context.

## 2. Evidence hierarchy

Preferred order:

1. Applicable law, notified regulation, and official safety restriction
2. Government agricultural department, IMD, ICAR, KVK, or state agricultural university guidance
3. Peer-reviewed research and recognized international institutions
4. Validated local field trials
5. Expert-reviewed operational guidance
6. Commercial supplier documentation
7. Farmer observations and community practice

Commercial material must never be presented as independent agronomic evidence.

## 3. Required source metadata

Each knowledge item must record:

- source institution
- title
- publication or revision date
- source URL or identifier
- crop applicability
- geographic applicability
- soil applicability
- farming-system applicability
- evidence grade
- reviewer
- valid-from date
- review-due date
- superseded-by reference

## 4. Evidence grades

- **A — Authoritative:** applicable regulation or current official recommendation
- **B — Strong:** replicated peer-reviewed research or multi-location institutional trials
- **C — Moderate:** limited-location trials or recognized extension guidance
- **D — Preliminary:** pilot evidence requiring validation
- **E — Observational:** farmer report or unverified field observation

## 5. Recommendation controls

A recommendation must contain:

- intended action
- action window
- quantity or method, when applicable
- field and crop context
- contraindications
- confidence
- evidence references
- rule or model version
- expert-review requirement
- expiry or reassessment trigger

## 6. High-risk recommendations

Mandatory expert or authority review is required for:

- pesticide prescription
- herbicide prescription
- micronutrient correction without current evidence
- soil amendment at potentially harmful rates
- autonomous irrigation control
- advice after flood, drought, salinity, or contamination events
- disease diagnosis with low confidence
- use of restricted, banned, or unregistered inputs

## 7. Source-of-truth policy

- Laboratory tests are authoritative for nutrient status unless invalid or expired.
- On-farm sensors provide trends and operational signals, not unquestioned laboratory equivalents.
- Official weather warnings take precedence for emergency communication.
- Forecast providers may be compared, but source and timestamp must remain visible.
- The LLM is an explanation and orchestration layer, not an agronomic source of truth.

## 8. Review workflow

`Draft → Technical Review → Agronomy Review → Approved → Published`

Published material may later become:

`Superseded`, `Withdrawn`, or `Expired`.

## 9. Initial authoritative-source families

The knowledge programme should prioritize:

- Indian Council of Agricultural Research
- India Meteorological Department
- Soil Health Card programme
- state agricultural universities
- Krishi Vigyan Kendras
- Directorate of Plant Protection, Quarantine and Storage
- Central and state agriculture departments
- Food and Agriculture Organization
- World Meteorological Organization
- peer-reviewed agricultural journals
