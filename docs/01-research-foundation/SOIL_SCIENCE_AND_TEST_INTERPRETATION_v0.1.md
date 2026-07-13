# Soil Science and Soil-Test Interpretation

## 1. Soil as a managed system

Soil is a physical, chemical, and biological system. Crop recommendations based only on N, P, and K are incomplete.

## 2. Physical properties

### Texture

Relative proportions of sand, silt, and clay influence:

- water holding
- drainage
- aeration
- nutrient retention
- tillage behaviour
- compaction risk

### Structure

Aggregation affects infiltration, root growth, erosion, and gas exchange.

### Depth and rooting restrictions

Record:

- effective soil depth
- hardpan
- stones
- compacted layers
- seasonal water table

### Bulk density and compaction

High compaction can restrict roots even where nutrients are adequate.

## 3. Chemical properties

### pH

pH affects nutrient availability, toxicity, and microbial processes. Interpretation must be crop-specific and laboratory-method-specific.

### Electrical conductivity

EC is an indicator of soluble salts. It must be interpreted together with crop tolerance, irrigation water, texture, and sampling conditions.

### Organic carbon

Soil organic carbon influences:

- aggregation
- water retention
- microbial activity
- nutrient cycling
- resilience

### Macronutrients

- nitrogen
- phosphorus
- potassium
- sulphur
- calcium
- magnesium

### Micronutrients

- zinc
- boron
- iron
- manganese
- copper
- molybdenum

Micronutrients must not be prescribed merely because a crop commonly responds to them. Current test evidence and local guidance are required.

## 4. Biological indicators

Potential observations:

- soil respiration
- microbial biomass
- earthworm activity
- residue decomposition
- root health
- enzyme activity

These indicators are valuable but may lack standardized, affordable field measurement. AgriOS must record method and confidence.

## 5. Sampling workflow

Required data:

- field and GPS
- sample date
- depth
- number of cores
- collection pattern
- recent fertilizer or manure
- crop and stage
- irrigation condition
- collector
- laboratory
- method

Avoid sampling:

- directly from fertilizer bands
- manure piles
- field edges
- unusual wet spots
- recently amended points unless specifically investigating them

## 6. Test validity

A result may be invalid or lower confidence when:

- location is unknown
- depth is missing
- sample is not representative
- laboratory method is missing
- units are unclear
- result age exceeds configured validity
- field management changed materially

## 7. Interpretation model

The system should not hard-code a universal “low, medium, high” range. Thresholds may differ by:

- analytical method
- state or institution
- crop
- soil
- target yield
- irrigation
- sampling depth

## 8. Soil amendments

Potential amendments include:

- lime
- gypsum
- organic matter
- elemental sulphur
- crop-specific micronutrients

Rates require validated calculations, soil properties, product quality, and expert rules.

## 9. Sensor policy

Low-cost pH, EC, and nutrient sensors may be used for screening and trends. They require calibration and must not silently override laboratory results.

## 10. Data model implications

Key entities:

- SoilSample
- SoilTestOrder
- SoilTestResult
- SoilProperty
- LaboratoryMethod
- SoilProfile
- SoilConstraint
- SoilRecommendation
- AmendmentPlan
