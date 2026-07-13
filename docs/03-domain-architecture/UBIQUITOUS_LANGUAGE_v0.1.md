# Ubiquitous Language

## Farmer Registry

- **Farmer:** Person participating in agricultural production, whether or not they own land.
- **Farmer Profile:** Canonical operational record for a farmer.
- **Verification Level:** Strength of evidence supporting the farmer profile.
- **Canonical Farmer:** Surviving profile after duplicate merge.
- **Farmer Alias:** Historical or merged reference mapped to the canonical farmer.

## Farm and Field

- **Farm:** Operational grouping of fields, participants, water sources, and infrastructure.
- **Field:** Geospatial land unit used for planning and crop-cycle execution.
- **Field Boundary Version:** Immutable geometry version valid from a point in time.
- **Management Zone:** Subdivision of a field managed differently because of soil, water, elevation, or crop conditions.
- **Field Lineage:** History created by split, merge, or correction.

## Tenure

- **Owner:** Party asserting legal ownership.
- **Cultivator:** Party currently operating the land.
- **Cultivation Right:** Time-bounded authority to cultivate.
- **Tenure Arrangement:** Ownership, lease, sharecropping, or other land-use relationship.
- **Disputed Tenure:** Arrangement whose validity or scope is contested.

## Soil and Water

- **Sample Plan:** Instructions defining where, when, and how a sample is collected.
- **Sample:** Physical material collected from a field or water source.
- **Chain of Custody:** Recorded movement and handling of a sample.
- **Test Result:** Laboratory observation with method, unit, source, and quality metadata.
- **Interpretation:** Context-specific meaning assigned to test results.
- **Constraint:** Soil or water condition that limits crop, input, or irrigation options.

## Agronomy Knowledge

- **Knowledge Item:** Versioned agronomic statement or rule.
- **Applicability:** Geography, crop, soil, season, stage, and farming-system boundaries.
- **Evidence Grade:** Strength of support for a knowledge item.
- **Published Knowledge:** Approved immutable version available for operational use.
- **Superseded Knowledge:** Historical version replaced by a later approved version.

## Crop Planning

- **Candidate Crop:** Crop considered during suitability assessment.
- **Hard Constraint:** Condition that prevents recommendation.
- **Suitability Score:** Relative score after hard constraints are applied.
- **Crop-Plan Scenario:** One farming and economic option for a field and season.
- **Approved Crop Plan:** Selected and accepted plan version eligible to activate a crop cycle.

## Crop Cycle

- **Crop Cycle:** Actual cultivation lifecycle for a crop in a field or field zone.
- **Field Allocation:** Area and geometry assigned to a crop cycle.
- **Crop Stage:** Current biological development phase.
- **Crop Loss:** Partial or total production loss.
- **Season Closure:** Finalized production, cost, revenue, quality, and learning record.

## Nutrient Management

- **Nutrient Requirement:** Amount of a nutrient needed by the crop plan.
- **Nutrient Credit:** Nutrient contribution from soil, residues, manure, water, or prior crop.
- **Source Allocation:** How nutrient demand is met from available sources.
- **Product Conversion:** Conversion from nutrient requirement to commercial or farm input quantity.
- **Nutrient Pathway:** Conventional, integrated, organic, natural, or transition plan.

## Irrigation

- **Irrigation Requirement:** Water required after accounting for crop, soil, rainfall, and current moisture.
- **Irrigation Schedule:** Planned application window, depth, volume, and runtime.
- **Irrigation Execution:** Actual application record.
- **Rain Lockout:** Rule preventing or delaying irrigation because of rainfall.
- **Water Accounting:** Reconciliation of rainfall, irrigation, use, loss, and storage.

## Weather Intelligence

- **Observation:** Measured weather condition.
- **Forecast:** Predicted condition for a future valid time.
- **Warning:** Authoritative risk message.
- **Forecast Confidence:** Assessed reliability of a forecast for operational use.
- **Weather Product:** Provider-issued dataset or bulletin.

## IoT and Telemetry

- **Device:** Registered hardware unit.
- **Sensor:** Component producing observations.
- **Actuator:** Component receiving control commands.
- **Assignment:** Time-bounded link between device and field, source, zone, or equipment.
- **Calibration:** Comparison and correction against a reference.
- **Telemetry Reading:** Time-stamped sensor observation.
- **Quality State:** Valid, suspect, invalid, missing, estimated, corrected, or uncalibrated.

## Advisory

- **Advisory:** Action guidance for a target field, crop cycle, farmer, or device.
- **Evidence Reference:** Versioned source used to support an advisory.
- **Confidence:** Reliability assessment of the advisory.
- **Validity Window:** Time period during which the advisory applies.
- **Superseded Advisory:** Advisory replaced by a newer advisory.
- **Withdrawn Advisory:** Advisory explicitly cancelled.

## Crop Health

- **Scouting:** Structured inspection of crop-health conditions.
- **Observation:** Recorded symptom, sign, count, or distribution.
- **Differential Diagnosis:** List of plausible causes.
- **Confirmed Diagnosis:** Expert or laboratory-supported conclusion.
- **Treatment Plan:** Approved management sequence.
- **Effectiveness Review:** Follow-up evaluation after treatment.

## Harvest and Traceability

- **Harvest Event:** Recorded removal of produce from a crop cycle.
- **Produce Lot:** Traceable quantity of harvested produce.
- **Lot Transformation:** Split, merge, drying, cleaning, grading, packing, or processing handoff.
- **Lot Lineage:** Complete ancestry and transformation history.
- **Quantity Balance:** Reconciled amount entering and leaving a lot.

## Governance

- **Consent Grant:** Purpose-specific permission to use or share data.
- **Policy Version:** Immutable approved policy used for a transaction.
- **Audit Event:** Append-only record of a significant action.
- **Data Authority:** Context that owns the canonical state.
