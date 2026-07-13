# Process Managers and Sagas

## 1. Crop Plan Activation Process

Steps:

1. CropPlanApproved
2. verify active field and cultivation authority
3. reserve seed allocation if configured
4. activate crop cycle
5. generate initial task calendar
6. request nutrient and irrigation plans
7. publish activation summary

Compensation:

- release seed allocation
- cancel generated tasks
- mark activation failed

## 2. Soil Test Publication Process

Steps:

1. SoilTestPublished
2. update soil profile
3. re-evaluate crop plans
4. re-evaluate nutrient plans
5. expire conflicting advisories
6. request expert review when critical

## 3. Weather Risk Advisory Process

Steps:

1. WeatherForecastUpdated or OfficialWeatherWarningReceived
2. map affected fields
3. evaluate crop-stage risk
4. generate advisory candidates
5. apply review policy
6. publish and notify
7. expire or supersede when forecast changes

## 4. Automated Irrigation Process

Steps:

1. irrigation recommendation approved
2. confirm consent and automation mode
3. confirm device health and calibration
4. evaluate safety interlocks
5. issue actuator command
6. confirm flow and execution
7. record irrigation
8. reconcile measured volume

Compensation:

- stop actuator
- create critical alert
- notify farmer
- open support case

## 5. Crop Health Escalation Process

Steps:

1. high-risk observation recorded
2. create expert case
3. gather evidence
4. assign specialist
5. record decision
6. update diagnosis
7. create or revise treatment plan
8. publish advisory

## 6. Harvest-to-Lot Process

Steps:

1. HarvestRecorded
2. create produce lot
3. link quality and grade
4. update economics
5. expose market availability
6. preserve lineage

## 7. Insurance Evidence Process

Steps:

1. LossEventRecorded
2. collect consented evidence
3. prepare package
4. farmer review
5. submit reference
6. track external decision
7. close evidence case
