# Farming Lifecycle and Operating Model

## 1. Purpose

This document defines the end-to-end farming lifecycle that AgriOS must support. The lifecycle is field-centric because one farmer may operate multiple fields with different soil, water, crops, and risks.

## 2. Pre-season planning

Capture:

- farmer goals
- available land
- field boundaries
- previous crops
- expected irrigation
- labour and machinery
- available capital
- debt obligations
- household food needs
- crop insurance
- buyer or contract commitments

Outputs:

- candidate crop plans
- investment range
- risk profile
- input calendar
- contingency alternatives

## 3. Field assessment

Activities:

- confirm boundary and area
- assess slope and drainage
- identify erosion and waterlogging
- inspect weeds and volunteer crops
- review prior input use
- collect soil samples
- test irrigation water when relevant

## 4. Crop and variety decision

Decision dimensions:

- agronomic suitability
- duration
- seed availability
- resistance or tolerance traits
- expected water demand
- market suitability
- household or fodder value
- rotation effect
- climate risk
- farmer capability

The platform should rank options rather than present one crop as universally best.

## 5. Land preparation

Possible operations:

- residue retention or incorporation
- primary tillage
- minimum or zero tillage
- bed or ridge preparation
- bunding
- drainage channels
- green-manure incorporation
- soil amendment
- pre-sowing irrigation

## 6. Seed and sowing

Track:

- seed source and lot
- certification status
- germination information
- seed treatment
- seed rate
- spacing
- sowing depth
- sowing method
- sowing date
- re-sowing decisions
- emergence count

## 7. Establishment stage

Monitor:

- emergence
- plant population
- early weeds
- soil moisture
- nutrient stress
- insect damage
- waterlogging
- crusting and compaction

## 8. Vegetative and reproductive stages

The platform must adjust advice by crop stage. Operations can include:

- irrigation
- split nutrient applications
- interculture
- weed control
- staking or training
- pest scouting
- disease scouting
- stress mitigation
- pollination support

## 9. Harvest

Record:

- maturity indicators
- harvest date
- weather suitability
- labour and machinery
- estimated and actual yield
- harvest loss
- quality grade
- moisture content

## 10. Post-harvest

Support:

- drying
- cleaning
- grading
- packing
- storage
- pest prevention
- lot traceability
- transport
- buyer selection
- price realization

## 11. Season closure

Calculate:

- total inputs
- labour
- water use
- yield
- sale revenue
- household consumption
- crop loss
- gross margin
- soil and field observations
- lessons for the next season

## 12. Core lifecycle states

`Planned → Approved → Land Preparation → Sown → Established → Growing → Reproductive → Harvest Ready → Harvested → Post-Harvest → Closed`

Exceptional states:

- Cancelled
- Failed Establishment
- Re-sowing Required
- Crop Loss
- Abandoned

## 13. Farmer-facing design

At any time the farmer should see:

- today’s tasks
- urgent warnings
- current crop stage
- next irrigation
- next nutrient action
- unresolved observation
- expected harvest window
