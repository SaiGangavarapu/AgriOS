# BRD 33 — Validation, Reporting, and Audit

## 1. Validation

### Soil and water

- missing sample location
- missing depth
- unknown method
- inconsistent unit
- expired result
- implausible value
- duplicate sample

### Crop plan

- unsupported crop
- field-area mismatch
- water insufficiency
- invalid sowing window
- unavailable seed
- missing budget assumption

### Crop cycle

- overlapping allocation
- impossible stage transition
- harvest before sowing
- actual area greater than field allocation

### Operation

- invalid quantity
- duplicated application
- incompatible stage
- unsafe weather
- missing operator
- future completion date

## 2. Reports

- soil-test status by field
- water-quality status
- crop-plan comparison
- active crop cycles
- stage summary
- seed requirement
- pending operations
- overdue tasks
- harvest and yield
- season profitability
- field history

## 3. Audit

Audit:

- knowledge version used
- suitability result
- plan approval
- plan revision
- stage correction
- seed-lot use
- input application
- task rescheduling
- crop-loss declaration
- season closure correction

## 4. Business requirements

- BR-132: The platform shall validate soil, water, plan, cycle, and operation data.
- BR-133: Reports shall distinguish missing, estimated, and confirmed values.
- BR-134: Agronomic decisions shall retain the source knowledge version.
- BR-135: Corrections shall preserve original values.
- BR-136: Exported reports shall respect role and consent.
