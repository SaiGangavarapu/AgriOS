# Verification — Complete SRS v1.0

Run from the AgriOS repository root:

```powershell
$requiredFiles = @(
  "RELEASE_SRS_V1_README.md",
  "WHAT_CHANGED.md",
  "VERIFY.md",
  "docs/04-srs/SRS_INDEX_v1.0.md",
  "docs/04-srs/SRS_DOCUMENT_MAP_v1.0.md",
  "docs/04-srs/SRS_VERSION_HISTORY_v1.0.md",
  "docs/04-srs/SRS_01_INTRODUCTION_v1.0.md",
  "docs/04-srs/SRS_02_ACTORS_v1.0.md",
  "docs/04-srs/SRS_03_USE_CASES_v1.0.md",
  "docs/04-srs/SRS_04_FARMER_MANAGEMENT_v1.0.md",
  "docs/04-srs/SRS_05_FARM_MANAGEMENT_v1.0.md",
  "docs/04-srs/SRS_06_FIELD_MANAGEMENT_v1.0.md",
  "docs/04-srs/SRS_07_SOIL_TESTING_v1.0.md",
  "docs/04-srs/SRS_08_WATER_TESTING_v1.0.md",
  "docs/04-srs/SRS_09_CROP_KNOWLEDGE_v1.0.md",
  "docs/04-srs/SRS_10_SEED_MANAGEMENT_v1.0.md",
  "docs/04-srs/SRS_11_CROP_PLANNING_v1.0.md",
  "docs/04-srs/SRS_12_CROP_LIFECYCLE_v1.0.md",
  "docs/04-srs/SRS_13_NUTRIENT_MANAGEMENT_v1.0.md",
  "docs/04-srs/SRS_14_FERTILIZER_FREE_FARMING_v1.0.md",
  "docs/04-srs/SRS_15_FERTILIZER_BASED_FARMING_v1.0.md",
  "docs/04-srs/SRS_16_IRRIGATION_v1.0.md",
  "docs/04-srs/SRS_17_WEATHER_AND_MONSOON_v1.0.md",
  "docs/04-srs/SRS_18_IOT_v1.0.md",
  "docs/04-srs/SRS_19_ADVISORY_AI_v1.0.md",
  "docs/04-srs/SRS_20_PEST_AND_DISEASE_v1.0.md",
  "docs/04-srs/SRS_21_HARVEST_v1.0.md",
  "docs/04-srs/SRS_22_STORAGE_v1.0.md",
  "docs/04-srs/SRS_23_MARKETPLACE_v1.0.md",
  "docs/04-srs/SRS_24_TRACEABILITY_v1.0.md",
  "docs/04-srs/SRS_25_ECONOMICS_v1.0.md",
  "docs/04-srs/SRS_26_INSURANCE_v1.0.md",
  "docs/04-srs/SRS_27_LENDING_v1.0.md",
  "docs/04-srs/SRS_28_REPORTING_v1.0.md",
  "docs/04-srs/SRS_29_SECURITY_AND_PRIVACY_v1.0.md",
  "docs/04-srs/SRS_30_OFFLINE_AND_SYNC_v1.0.md",
  "docs/04-srs/SRS_31_API_REQUIREMENTS_v1.0.md",
  "docs/04-srs/SRS_32_VALIDATION_RULES_v1.0.md",
  "docs/04-srs/SRS_33_ERROR_CATALOGUE_v1.0.md",
  "docs/04-srs/SRS_34_ACCEPTANCE_CRITERIA_v1.0.md",
  "docs/04-srs/SRS_35_TRACEABILITY_MATRIX_v1.0.md"
)

$missing = $requiredFiles | Where-Object { -not (Test-Path $_) }

if ($missing.Count -gt 0) {
  Write-Host "Complete SRS verification failed. Missing files:" -ForegroundColor Red
  $missing | ForEach-Object { Write-Host " - $_" -ForegroundColor Red }
  exit 1
}

Write-Host "Complete SRS v1.0 verification passed." -ForegroundColor Green
```
